using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace NetworkRouting
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void generateButton_Click(object sender, EventArgs e)
        {
            int randomSeed = int.Parse(randomSeedBox.Text);
            int size = int.Parse(sizeBox.Text);

            Random rand = new Random(randomSeed);
            seedUsedLabel.Text = "Random Seed Used: " + randomSeed.ToString();

            this.adjacencyList = generateAdjacencyList(size, rand);
            List<PointF> points = generatePoints(size, rand);
            resetImageToPoints(points);
            this.points = points;
            startNodeIndex = -1;
            stopNodeIndex = -1;
            sourceNodeBox.Text = "";
            targetNodeBox.Text = "";

            Refresh();
        }

        // Generates the distance matrix.  Values of -1 indicate a missing edge.  Loopbacks are at a cost of 0.
        private const int MIN_WEIGHT = 1;
        private const int MAX_WEIGHT = 100;
        private const double PROBABILITY_OF_DELETION = 0.35;

        private const int NUMBER_OF_ADJACENT_POINTS = 3;

        private List<HashSet<int>> generateAdjacencyList(int size, Random rand)
        {
            List<HashSet<int>> adjacencyList = new List<HashSet<int>>();

            for (int i = 0; i < size; i++)
            {
                HashSet<int> adjacentPoints = new HashSet<int>();
                while (adjacentPoints.Count < 3)
                {
                    int point = rand.Next(size);
                    if (point != i) adjacentPoints.Add(point);
                }
                adjacencyList.Add(adjacentPoints);
            }

            return adjacencyList;
        }

        private List<PointF> generatePoints(int size, Random rand)
        {
            List<PointF> points = new List<PointF>();

            for (int i = 0; i < size; i++)
            {
                points.Add(new PointF((float)(rand.NextDouble() * pictureBox.Width), (float)(rand.NextDouble() * pictureBox.Height)));
            }
            return points;
        }

        private void resetImageToPoints(List<PointF> points)
        {
            pictureBox.Image = new Bitmap(pictureBox.Width, pictureBox.Height);
            Graphics graphics = Graphics.FromImage(pictureBox.Image);

            foreach (PointF point in points)
            {
                graphics.DrawEllipse(new Pen(Color.Blue), point.X, point.Y, 2, 2);
            }

            this.graphics = graphics;
            pictureBox.Invalidate();

        }

        // These variables are instantiated after the "Generate" button is clicked
        private List<PointF> points = new List<PointF>();
        private Graphics graphics;
        private List<HashSet<int>> adjacencyList;

        // Use this to generate routing tables for every node
        private void solveButton_Click(object sender, EventArgs e)
        {
            allPaths(onePath(points.Count), points.Count()); //all of project 3, in one nifty line  :)   
        }

        public bool onePath(int numPoints)
        {
            //start a timer
            System.Diagnostics.Stopwatch timeOne = new System.Diagnostics.Stopwatch();
            timeOne.Start();

            //these indices correspond with the points, keep track of points, keys, and place in the queue
            Node[] nodeInfoOne = new Node[numPoints];
            for (int i = 0; i < numPoints; i++)
            {
                nodeInfoOne[i] = new Node(points[i], double.MaxValue, -1);
            }

            BinaryHeap bhOne = new BinaryHeap(numPoints + 1, nodeInfoOne, startNodeIndex);

            //need this just for One Path
            bool foundEnd = false;

            //this will run until the stop node is found, or through all the nodes if never found
            while (bhOne.getQueueSize() > 0)
            {
                HeapNode n = bhOne.deleteMin();
                int i = n.getPointListIndex();

                //we can stop our looping 
                if (i == stopNodeIndex)
                {
                    //we already know its distance and previous pointer, we're done
                    foundEnd = true;
                    break;
                }

                //find out all the other nodes this one is connected to, decrease keys if applicable
                PointF a = points[i];
                foreach (int edge in adjacencyList[i])
                {
                    PointF b = points[edge];
                    double dist = Math.Sqrt(Math.Pow(a.X - b.X, 2) + Math.Pow(a.Y - b.Y, 2));
                    if ((n.getKey()) + dist < nodeInfoOne[edge].getKey())
                    {
                        //-------------------------------------------------------------------
                        //insert onto the queue if not already in there, decrease key if it is
                        //-------------------------------------------------------------------
                        if (nodeInfoOne[edge].getHeapIndex() != -1)
                        {
                            nodeInfoOne[edge].setKey(n.getKey() + dist);
                            bhOne.decreaseKey(nodeInfoOne[edge].getHeapIndex(), n.getKey() + dist);

                            //the previous node changes when key is decreased  
                            nodeInfoOne[edge].setPrevNode(i);
                        }
                        else
                        {
                            bhOne.insert(new HeapNode(n.getKey() + dist, edge));
                            nodeInfoOne[edge].setKey(n.getKey() + dist);
                            nodeInfoOne[edge].setPrevNode(i);
                        }
                    }
                }
            }

            if (!foundEnd)
            {
                pathCostBox.Text = "unreachable";
                //report time
                onePathTime = timeOne.Elapsed.Milliseconds;
                onePathTime = onePathTime / 1000;
                oneTimeBox.Text = onePathTime.ToString();

                return false;
            }
            else
            {
                //draw the path
                int currentIndexOne = stopNodeIndex;
                double pathLeft = nodeInfoOne[stopNodeIndex].getKey();
                while (currentIndexOne != startNodeIndex)
                {
                    int nextIndex = nodeInfoOne[currentIndexOne].getPrevNode();
                    PointF a = points[currentIndexOne];
                    PointF b = points[nextIndex];
                    graphics.DrawLine(new Pen(Color.Black), a, b);

                    //draw the edge length
                    double distance = pathLeft - nodeInfoOne[nextIndex].getKey();
                    pathLeft -= distance;
                    graphics.DrawString(Math.Round(distance,2).ToString(), new Font(this.Font,FontStyle.Regular), 
                                new SolidBrush(Color.Red),new PointF((a.X + b.X) / 2, (a.Y + b.Y) / 2));
                    
                    currentIndexOne = nextIndex;
                }

                //report time
                onePathTime = timeOne.Elapsed.Milliseconds;
                onePathTime = onePathTime / 1000;
                oneTimeBox.Text = onePathTime.ToString();

                //report cost
                pathCostBox.Text = nodeInfoOne[stopNodeIndex].getKey().ToString();
                return true;
            }
        }

        public void allPaths(bool foundEnd, int numPoints)
        {
            //start timer
            System.Diagnostics.Stopwatch timeAll = new System.Diagnostics.Stopwatch();
            timeAll.Start();

            //these indices correspond with the points, keep track of points, keys, and place in the queue
            Node[] nodeInfoAll = new Node[numPoints];
            for (int i = 0; i < numPoints; i++)
            {
                nodeInfoAll[i] = new Node(points[i], double.MaxValue, -1);
            }

            BinaryHeap bhAll = new BinaryHeap(numPoints + 1, nodeInfoAll, startNodeIndex, 0);

            while (bhAll.getQueueSize() > 0)
            {
                HeapNode n = bhAll.deleteMin();
                int i = n.getPointListIndex();

                //find out all the other nodes this is connected to, decrease keys
                PointF a = points[i];
                foreach (int edge in adjacencyList[i])
                {
                    PointF b = points[edge];
                    double dist = Math.Sqrt(Math.Pow(a.X - b.X, 2) + Math.Pow(a.Y - b.Y, 2));
                    if ((n.getKey()) + dist < nodeInfoAll[edge].getKey())
                    {
                        nodeInfoAll[edge].setKey(n.getKey() + dist);
                        bhAll.decreaseKey(nodeInfoAll[edge].getHeapIndex(), n.getKey() + dist);

                        nodeInfoAll[edge].setPrevNode(i);
                    }
                }
            }

            //we already know if there's a path from onePath, still ran algorithm to measure time
            if (foundEnd)
            {
                //drawPath
                int currentIndexAll = stopNodeIndex;
                double pathLeft = nodeInfoAll[stopNodeIndex].getKey();

                while (currentIndexAll != startNodeIndex)
                {
                    int nextIndex = nodeInfoAll[currentIndexAll].getPrevNode();
                    PointF a = points[currentIndexAll];
                    PointF b = points[nextIndex];
                    graphics.DrawLine(new Pen(Color.Black), points[currentIndexAll], points[nextIndex]);

                    //draw the edge length
                    double distance = pathLeft - nodeInfoAll[nextIndex].getKey();
                    pathLeft -= distance;
                    graphics.DrawString(Math.Round(distance, 2).ToString(), new Font(this.Font, FontStyle.Regular),
                                new SolidBrush(Color.Red), new PointF((a.X + b.X) / 2, (a.Y + b.Y) / 2));

                    currentIndexAll = nextIndex;
                }

                Refresh();
            }

            //report time
            allPathTime = timeAll.Elapsed.Milliseconds;
            allPathTime = allPathTime / 1000;
            Console.Write("here");
            allTimeBox.Text = allPathTime.ToString();

            differenceBox.Text = ((allPathTime - onePathTime) * 100 / allPathTime).ToString();

        }

        //I added these two variables to calculate the difference box
        private double onePathTime;
        private double allPathTime;

        private Boolean startStopToggle = true;
        private int startNodeIndex = -1;
        private int stopNodeIndex = -1;
        private void pictureBox_MouseDown(object sender, MouseEventArgs e)
        {
            if (points.Count > 0)
            {
                Point mouseDownLocation = new Point(e.X, e.Y);
                int index = ClosestPoint(points, mouseDownLocation);
                if (startStopToggle)
                {
                    startNodeIndex = index;
                    sourceNodeBox.Text = "" + index;
                }
                else
                {
                    stopNodeIndex = index;
                    targetNodeBox.Text = "" + index;
                }
                startStopToggle = !startStopToggle;

                resetImageToPoints(points);
                paintStartStopPoints();
            }
        }

        private void paintStartStopPoints()
        {
            if (startNodeIndex > -1)
            {
                Graphics graphics = Graphics.FromImage(pictureBox.Image);
                graphics.DrawEllipse(new Pen(Color.Green, 6), points[startNodeIndex].X, points[startNodeIndex].Y, 1, 1);
                this.graphics = graphics;
                pictureBox.Invalidate();
            }

            if (stopNodeIndex > -1)
            {
                Graphics graphics = Graphics.FromImage(pictureBox.Image);
                graphics.DrawEllipse(new Pen(Color.Red, 2), points[stopNodeIndex].X - 3, points[stopNodeIndex].Y - 3, 8, 8);
                this.graphics = graphics;
                pictureBox.Invalidate();
            }
        }

        private int ClosestPoint(List<PointF> points, Point mouseDownLocation)
        {
            double minDist = double.MaxValue;
            int minIndex = 0;

            for (int i = 0; i < points.Count; i++)
            {
                double dist = Math.Sqrt(Math.Pow(points[i].X-mouseDownLocation.X,2) + Math.Pow(points[i].Y - mouseDownLocation.Y,2));
                if (dist < minDist)
                {
                    minIndex = i;
                    minDist = dist;
                }
            }

            return minIndex;
        }

        private void label4_Click(object sender, EventArgs e)
        {

        }
    }
}
