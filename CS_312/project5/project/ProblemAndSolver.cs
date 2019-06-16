using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Diagnostics;

namespace TSP
{

    class ProblemAndSolver
    {
        //number of cities
        int l;

        //global help variables used instead of passing around objects with variables
        int maxI = 0;
        int maxJ = 0;
        double maxInclude = 0;
        double maxExclude = 0;
        double[,] includeMatrix;

        //truly global variables
        int[] bestPathSoFar = null;
        int maxQueueSize = 1;
        int numBSSFupdates = 0;
        int numStatesCreated = 1;
        int numStatesPruned = 0;
        double bssF = double.PositiveInfinity;
        int heapSize = 10000; //I changed this around depending on the size of the input
        int queueLimit = 200; //I changed this to see if I could get a better answer


        private class TSPSolution
        {


            /// <summary>
            /// we use the representation [cityB,cityA,cityC] 
            /// to mean that cityB is the first city in the solution, cityA is the second, cityC is the third 
            /// and the edge from cityC to cityB is the final edge in the path.  
            /// You are, of course, free to use a different representation if it would be more convenient or efficient 
            /// for your node data structure and search algorithm. 
            /// </summary>
            public ArrayList
                Route;

            public TSPSolution(ArrayList iroute)
            {
                Route = new ArrayList(iroute);
            }


            /// <summary>
            /// Compute the cost of the current route.  
            /// Note: This does not check that the route is complete.
            /// It assumes that the route passes from the last city back to the first city. 
            /// </summary>
            /// <returns></returns>
            public double costOfRoute()
            {
                // go through each edge in the route and add up the cost. 
                int x;
                City here;
                double cost = 0D;

                for (x = 0; x < Route.Count - 1; x++)
                {
                    here = Route[x] as City;
                    cost += here.costToGetTo(Route[x + 1] as City);
                }

                // go from the last city to the first. 
                here = Route[Route.Count - 1] as City;
                cost += here.costToGetTo(Route[0] as City);
                return cost;
            }
        }

        #region Private members 

        /// <summary>
        /// Default number of cities (unused -- to set defaults, change the values in the GUI form)
        /// </summary>
        // (This is no longer used -- to set default values, edit the form directly.  Open Form1.cs,
        // click on the Problem Size text box, go to the Properties window (lower right corner), 
        // and change the "Text" value.)
        private const int DEFAULT_SIZE = 25;

        private const int CITY_ICON_SIZE = 5;

        // For normal and hard modes:
        // hard mode only
        private const double FRACTION_OF_PATHS_TO_REMOVE = 0.20;

        /// <summary>
        /// the cities in the current problem.
        /// </summary>
        private City[] Cities;
        /// <summary>
        /// a route through the current problem, useful as a temporary variable. 
        /// </summary>
        private ArrayList Route;
        /// <summary>
        /// best solution so far. 
        /// </summary>
        private TSPSolution bssf; 

        /// <summary>
        /// how to color various things. 
        /// </summary>
        private Brush cityBrushStartStyle;
        private Brush cityBrushStyle;
        private Pen routePenStyle;


        /// <summary>
        /// keep track of the seed value so that the same sequence of problems can be 
        /// regenerated next time the generator is run. 
        /// </summary>
        private int _seed;
        /// <summary>
        /// number of cities to include in a problem. 
        /// </summary>
        private int _size;

        /// <summary>
        /// Difficulty level
        /// </summary>
        private HardMode.Modes _mode;

        /// <summary>
        /// random number generator. 
        /// </summary>
        private Random rnd;
        #endregion

        #region Public members
        public int Size
        {
            get { return _size; }
        }

        public int Seed
        {
            get { return _seed; }
        }
        #endregion

        #region Constructors
        public ProblemAndSolver()
        {
            this._seed = 1; 
            rnd = new Random(1);
            this._size = DEFAULT_SIZE;

            this.resetData();
        }

        public ProblemAndSolver(int seed)
        {
            this._seed = seed;
            rnd = new Random(seed);
            this._size = DEFAULT_SIZE;

            this.resetData();
        }

        public ProblemAndSolver(int seed, int size)
        {
            this._seed = seed;
            this._size = size;
            rnd = new Random(seed); 
            this.resetData();
        }
        #endregion

        #region Private Methods

        /// <summary>
        /// Reset the problem instance.
        /// </summary>
        private void resetData()
        {

            Cities = new City[_size];
            Route = new ArrayList(_size);
            bssf = null;

            if (_mode == HardMode.Modes.Easy)
            {
                for (int i = 0; i < _size; i++)
                    Cities[i] = new City(rnd.NextDouble(), rnd.NextDouble());
            }
            else // Medium and hard
            {
                for (int i = 0; i < _size; i++)
                    Cities[i] = new City(rnd.NextDouble(), rnd.NextDouble(), rnd.NextDouble() * City.MAX_ELEVATION);
            }

            HardMode mm = new HardMode(this._mode, this.rnd, Cities);
            if (_mode == HardMode.Modes.Hard)
            {
                int edgesToRemove = (int)(_size * FRACTION_OF_PATHS_TO_REMOVE);
                mm.removePaths(edgesToRemove);
            }
            City.setModeManager(mm);

            cityBrushStyle = new SolidBrush(Color.Black);
            cityBrushStartStyle = new SolidBrush(Color.Red);
            routePenStyle = new Pen(Color.Blue,1);
            routePenStyle.DashStyle = System.Drawing.Drawing2D.DashStyle.Solid;
        }

        #endregion

        #region Public Methods

        /// <summary>
        /// make a new problem with the given size.
        /// </summary>
        /// <param name="size">number of cities</param>
        //public void GenerateProblem(int size) // unused
        //{
        //   this.GenerateProblem(size, Modes.Normal);
        //}

        /// <summary>
        /// make a new problem with the given size.
        /// </summary>
        /// <param name="size">number of cities</param>
        public void GenerateProblem(int size, HardMode.Modes mode)
        {
            this._size = size;
            this._mode = mode;
            resetData();
        }

        /// <summary>
        /// return a copy of the cities in this problem. 
        /// </summary>
        /// <returns>array of cities</returns>
        public City[] GetCities()
        {
            City[] retCities = new City[Cities.Length];
            Array.Copy(Cities, retCities, Cities.Length);
            return retCities;
        }

        /// <summary>
        /// draw the cities in the problem.  if the bssf member is defined, then
        /// draw that too. 
        /// </summary>
        /// <param name="g">where to draw the stuff</param>
        public void Draw(Graphics g)
        {
            float width  = g.VisibleClipBounds.Width-45F;
            float height = g.VisibleClipBounds.Height-45F;
            Font labelFont = new Font("Arial", 10);

            // Draw lines
            if (bssf != null)
            {
                // make a list of points. 
                Point[] ps = new Point[bssf.Route.Count];
                int index = 0;
                foreach (City c in bssf.Route)
                {
                    if (index < bssf.Route.Count -1)
                        g.DrawString(" " + index +"("+c.costToGetTo(bssf.Route[index+1]as City)+")", labelFont, cityBrushStartStyle, new PointF((float)c.X * width + 3F, (float)c.Y * height));
                    else 
                        g.DrawString(" " + index +"("+c.costToGetTo(bssf.Route[0]as City)+")", labelFont, cityBrushStartStyle, new PointF((float)c.X * width + 3F, (float)c.Y * height));
                    ps[index++] = new Point((int)(c.X * width) + CITY_ICON_SIZE / 2, (int)(c.Y * height) + CITY_ICON_SIZE / 2);
                }

                if (ps.Length > 0)
                {
                    g.DrawLines(routePenStyle, ps);
                    g.FillEllipse(cityBrushStartStyle, (float)Cities[0].X * width - 1, (float)Cities[0].Y * height - 1, CITY_ICON_SIZE + 2, CITY_ICON_SIZE + 2);
                }

                // draw the last line. 
                g.DrawLine(routePenStyle, ps[0], ps[ps.Length - 1]);
            }

            // Draw city dots
            foreach (City c in Cities)
            {
                g.FillEllipse(cityBrushStyle, (float)c.X * width, (float)c.Y * height, CITY_ICON_SIZE, CITY_ICON_SIZE);
            }

        }

        /// <summary>
        ///  return the cost of the best solution so far. 
        /// </summary>
        /// <returns></returns>
        public double costOfBssf ()
        {
            if (bssf != null)
                return (bssf.costOfRoute());
            else
                return -1D; 
        }

        /// <summary>
        ///  solve the problem.  This is the entry point for the solver when the run button is clicked
        /// right now it just picks a simple solution. 
        /// </summary>
        public void solveProblem()
        {
            Stopwatch timer = new Stopwatch();
            timer.Start();

            l = Cities.Length;

            includeMatrix = new double[l, l];

            double[,] costMatrix = new double[l, l];


            //make the initial cost matrix
            for (int i = 0; i < l; i++)
            {
                for (int j = 0; j < l; j++)
                {
                    if (i == j)
                        costMatrix[i, j] = double.PositiveInfinity;
                    else
                        costMatrix[i, j] = Cities[i].costToGetTo(Cities[j]);
                }
            }



            //use greedy algorithm to get an initial bssf
            int startIndex = 0;
            while(bssF == double.PositiveInfinity)
            {
                bssF = greedy(costMatrix, startIndex);
                startIndex ++;
            }

            //reduce the original cost Matrix
            double cost = reduceMatrix(costMatrix);



            //---------------------------------------------------------
            //put a starter node on the heap with the reduced cost and the matrix, 
            //       an empty path, and and edgeCount of 0
            //---------------------------------------------------------- 
            BinaryHeap bh = new BinaryHeap(heapSize);

            int[] pathArray = new int[l];

            for (int i = 0; i < l; i++)
            {
                pathArray[i] = -1;
            }

            bh.insert(new Node(cost, costMatrix, pathArray, 0));



            //while queue is not empty
            while (bh.getQueueSize() > 0)
            {
                //since we increase the number of states whether or not they are put on the queue
                numStatesCreated += 2;

                //pop the lowest cost node off the queue
                Node n = bh.deleteMin();

                int[] nodePath = n.getPathArray();


                //this should be a valid path, we successfully included enough edges
                if (n.getEdgeCount() == l)
                {
                    //update bssf, path, 
                    bssF = n.getKey();
                    bestPathSoFar = n.getPathArray();

                    //keep track of updates
                    numBSSFupdates++;


                   
                    BinaryHeap temp = new BinaryHeap(heapSize);

                    int preCount = bh.getQueueSize();
                    
                    // go through the queue and get rid of too high LB's
                    while (bh.getQueueSize() > 0)
                    {
                        Node node = bh.deleteMin();

                        if (node.getKey() < bssF)
                        {
                            temp.insert(node);
                        }

                        bh = temp;
                    }

                    //when I tried to increment numStatesPruned in the while loop it was optimized out
                    //  or something, so this is where I find out how many states I pruned
                    int postCount = bh.getQueueSize();
                    numStatesPruned += preCount - postCount;
                }
                else
                {
                    //was not a complete path.  We now go to the method that makes an include and exclude state
                    splitDecision(n.getMatrix(), nodePath, n.getEdgeCount());
                    
                    
                    //check to see if an include improved the bssf
                    //if not do nothing, don't put it on the queue
                    if (((n.getKey() + maxInclude) < bssF))
                    {
                        int[] includePath = new int[l];

                        //must make a copy of the path array since our queue might be expanding
                        for (int i = 0; i < includePath.Length; i++)
                        {
                            includePath[i] = nodePath[i];
                        }

                        //includes one edge
                        includePath[maxI] = maxJ;

                        //we must also make a copy of the cost matrix
                        double[,] copyIncludeMatrix = new double[l,l];

                        if (includeMatrix != null)
                        {
                            for (int i = 0; i < l; i++)
                            {
                                for (int j = 0; j < l; j++)
                                {
                                    copyIncludeMatrix[i, j] = includeMatrix[i, j];
                                }
                            }
                        }

                        Node includeNode = new Node(n.getKey() + maxInclude, copyIncludeMatrix, includePath, n.getEdgeCount() + 1);
                        bh.insert(includeNode);
                    }
                    else
                    {
                        numStatesPruned++;
                    }

                    //only insert the exclude state if the lower bound is less than bssf
                    if ((n.getKey() + maxExclude < bssF) && (bh.getQueueSize() < queueLimit))
                    {
                        //save some time by just using the parents' path and almost the parent's cost matrix
                        n.getMatrix()[maxI, maxJ] = double.PositiveInfinity;
                        reduceMatrix(n.getMatrix());

                        Node excludeNode = new Node(n.getKey() + maxExclude, n.getMatrix(), nodePath, n.getEdgeCount());
                        bh.insert(excludeNode);                        
                    }
                    else
                    {
                        numStatesPruned++;
                    }

                }

                //keep track of the highest number of stored states
                if (bh.getQueueSize() > maxQueueSize)
                    maxQueueSize = bh.getQueueSize();

                //time out after 30 seconds
                if (timer.ElapsedMilliseconds > 30000)
                {
                    numStatesPruned = numStatesCreated - bh.getQueueSize();
                    break;
                }
            }


            //------------------------------------------------------------------
            //report the route and bssf just like in the demo, and time elapsed
            //------------------------------------------------------------------
            Route = new ArrayList();

            int city = 0;
            int count = 0;
            Route.Add(Cities[0]);

            while (count != l)
            {
                city = bestPathSoFar[city];
                Route.Add(Cities[city]);
                count++;
            }

            // call this the best solution so far.  bssf is the route that will be drawn by the Draw method. 
            bssf = new TSPSolution(Route);

            // update the cost of the tour. 
            //some values we needed for the table
            Program.MainForm.tbCostOfTour.Text = " " + bssf.costOfRoute() + "   " + maxQueueSize + "    "
            + numBSSFupdates + "    " + numStatesCreated + "    " + numStatesPruned + "    ";

            //report the time elapsed
            timer.Stop();
            Program.MainForm.tbElapsedTime.Text = timer.Elapsed.ToString();

            // do a refresh. 
            Program.MainForm.Invalidate();

        }



        public void splitDecision(double[,] costMatrix, int[] path, int edgeCount)
        {
            includeMatrix = new double[l, l];

            //the special case where there can only be one edge left
            if (edgeCount == (l - 1))
            {
                for (int i = 0; i < l; i++)
                {
                    for (int j = 0; j < l; j++)
                    {
                        //we found that one edge
                        if (costMatrix[i, j] == 0)
                        {
                            maxI = i;
                            maxJ = j;
                            maxInclude = 0;
                            maxExclude = double.PositiveInfinity;
                            includeMatrix = null;  //trivial at this point
                        }
                    }
                }
            }
            else
            {
                //---------------------------------------------------
                //now we begin choosing which edge to include/exclude
                //---------------------------------------------------

                double maxDiff = double.NegativeInfinity;
                maxInclude = double.PositiveInfinity;

                //search through the entire cost matrix of the node
                for (int i = 0; i < l; i++)
                {
                    for (int j = 0; j < l; j++)
                    {
                        //any zeroes we come across we evaulate the include,exclude,difference etc.
                        if (costMatrix[i, j] == 0)
                        {
                            //I made a copy each time so I could change the values without fretting
                            //I know, a bit of a slowdown
                            double[,] copyMatrix = new double[l, l];
                            for (int a = 0; a < l; a++)
                            {
                                for (int b = 0; b < l; b++)
                                {
                                    copyMatrix[a, b] = costMatrix[a, b];
                                }
                            }

                            //here we make the Ith row and Jth column all negatives
                            for (int a = 0; a < l; a++)
                            {
                                copyMatrix[a, j] = double.PositiveInfinity;
                            }

                            for (int b = 0; b < l; b++)
                            {
                                copyMatrix[i, b] = double.PositiveInfinity;
                            }


                            //get rid of loop edges here
                            //not necessary once there is only one edge left
                            if (edgeCount < (l - 2))
                            {
                                //more optimal than a complete  copy
                                path[i] = j;

                                //-----------------------------------------------------------------------------
                                //The next 3 while loops compose an algorithm probably similar to the one in
                                //   the slides, but I kind of went at it on my own.  The first two loops find
                                //   the start and end node of the longest path created by adding an edge.  the
                                //   final loop moves along the path from start to end, removing any edges between
                                //   any two points in the path.  Maybe a little overkill.
                                //-----------------------------------------------------------------------------

                                int start = i;
                                int end = j;

                                bool prev = true;
                                bool next = true;

                                while (prev)
                                {
                                    prev = false;

                                    for (int node = 0; node < l; node++)
                                    {
                                        if (path[node] == start)
                                        {
                                            start = node;
                                            prev = true;
                                            node = l;
                                        }
                                    }
                                }

                                while (next)
                                {
                                    next = false;

                                    if (path[end] != -1)
                                    {
                                        end = path[end];
                                        next = true;
                                    }
                                }


                                while (start != end)
                                {
                                    int left = start;
                                    int right = end;

                                    while (left != right)
                                    {
                                        copyMatrix[left, right] = double.PositiveInfinity;
                                        copyMatrix[right, left] = double.PositiveInfinity;
                                        left = path[left];
                                    }

                                    start = path[start];
                                }

                                //fix the path array to how it was
                                path[i] = -1;

                            }


                            //now we are ready to find out the cost of the include matrix
                            double include = reduceMatrix(copyMatrix);

                            //the exclude score is a lot easier to find!
                            double exclude = minCol(costMatrix, i, j) + minRow(costMatrix, i, j);

                            double diff = exclude - include;


                            //mainly checking if we've improved exclude - include
                            //    however when diff is infinity or equal to the maxDiff, I choose the edge
                            //     that has the lowest include cost
                            if ((diff >= maxDiff) || (diff == double.PositiveInfinity) )
                            {
                                if (((diff == double.PositiveInfinity || diff == maxDiff) && (include < maxInclude))
                                                        || ((diff != double.PositiveInfinity) && diff != maxDiff))
                                {
                                    //set the necessary global variables so solveProblem function can see them
                                    //upon function return they represent what edge was chosen as include/exclude
                                    maxDiff = diff;
                                    maxI = i;
                                    maxJ = j;
                                    maxInclude = include;
                                    maxExclude = exclude;

                                    for (int ii = 0; ii < l; ii++)
                                    {
                                        for (int jj = 0; jj < l; jj++)
                                        {
                                            includeMatrix[ii, jj] = copyMatrix[ii, jj];
                                        }
                                    }
                                }////
                            }////////
                        }////////////
                    }////////////////
                }////////O///O///////
            }//////////////-/////////
        }///////////////\_____///////


        

        //--------------------------------------------------
        //hope these two functions are self-explanatory
        //--------------------------------------------------
        double minCol(double[,] costMatrix, int i, int j)
        {
            double min = double.PositiveInfinity;

            for(int x = 0; x < l; x++)
            {
                if (x != i && costMatrix[x, j] < min)
                    min = costMatrix[x, j];
            }

            return min;
        }

        double minRow(double[,] costMatrix, int i, int j)
        {
            double min = double.PositiveInfinity;

            for (int y = 0; y < l; y++)
            {
                if (y != j && costMatrix[i, y] < min)
                    min = costMatrix[i, y];
            }

            return min;
        }

        //your standard reduce function, go through the rows then columns and reduce if min is not zero
        double reduceMatrix(double[,] costMatrix)
        {
            double lowerBound = 0;

            for(int i = 0; i < l; i++)
            {
                double min = double.PositiveInfinity;

                for(int j = 0; j < l; j++)
                {
                    if (costMatrix[i, j] < min)
                        min = costMatrix[i, j];
                }

                if(min != 0 && min != double.PositiveInfinity)
                {
                    for (int j = 0; j < l; j++)
                    {
                        if (costMatrix[i, j] != 0 && costMatrix[i, j] != double.PositiveInfinity)
                            costMatrix[i, j] -= min;
                    }

                    lowerBound += min;        
                }
            }


            for (int j = 0; j < l; j++)
            {
                double min = double.PositiveInfinity;

                for (int i = 0; i < l; i++)
                {
                    if (costMatrix[i, j] < min)
                        min = costMatrix[i, j];
                }

                if (min != 0 && min != double.PositiveInfinity)
                {
                    for (int i = 0; i < l; i++)
                    {
                        if (costMatrix[i, j] != 0 && costMatrix[i, j] != double.PositiveInfinity)
                            costMatrix[i, j] -= min;
                    }

                    lowerBound += min;
                }
            }

            return lowerBound;
        }

        //find the greedy solution.  It's usually decent
        public double greedy(double[,] costMatrix, int index)
        {
            double[,] copyMatrix = new double[l, l];
            for (int a = 0; a < l; a++)
            {
                for (int b = 0; b < l; b++)
                {
                    copyMatrix[a, b] = costMatrix[a, b];
                }
            }

            int[] greedyPathArray = new int[l];

            for (int i = 0; i < l; i++)
            {
                greedyPathArray[i] = -1;
            }


            double greedyCost = 0;

            for (int count = 0; count < l; count++)
            {
                double min = double.PositiveInfinity;
                int minCol = 0;

                for (int y = 0; y < l; y++)
                {
                    if (copyMatrix[index, y] < min)
                    {
                        min = copyMatrix[index, y];
                        minCol = y;
                    }
                }

                if (min == double.PositiveInfinity)
                {
                    return min;
                }
                else
                {
                    greedyCost += min;

                    for (int i = 0; i < l; i++)
                    {
                        copyMatrix[i, minCol] = double.PositiveInfinity;
                    }

                    greedyPathArray[index] = minCol;

                    if (count != l - 2)
                    {
                        for (int i = 0; i < l; i++)
                        {
                            if (greedyPathArray[i] != -1)
                            {
                                copyMatrix[minCol, i] = double.PositiveInfinity;
                            }
                        }
                    }
                }

                index = minCol;
            }

            bestPathSoFar = greedyPathArray;
            return greedyCost;
        }
        #endregion
    }

}
