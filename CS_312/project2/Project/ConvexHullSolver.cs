using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Linq;
using _1_convex_hull;


namespace _2_convex_hull
{
    class ConvexHullSolver
    {
        System.Drawing.Graphics g;
        System.Windows.Forms.PictureBox pictureBoxView;

        public ConvexHullSolver(System.Drawing.Graphics g, System.Windows.Forms.PictureBox pictureBoxView)
        {
            this.g = g;
            this.pictureBoxView = pictureBoxView;
        }

        public void Refresh()
        {
            // Use this especially for debugging and whenever you want to see what you have drawn so far
            pictureBoxView.Refresh();
        }

        public void Pause(int milliseconds)
        {
            // Use this especially for debugging and to animate your algorithm slowly
            pictureBoxView.Refresh();
            System.Threading.Thread.Sleep(milliseconds);
        }


        //worst case O(nlogn)
        public void Solve(List<System.Drawing.PointF> pointList)
        {
            //sort points
            pointList = pointList.OrderByDescending(p => p.X).ToList();

            //call the recursive convexHull function, retrieve final list
            Hull finalHUll = convexHUll(pointList);
            pointList = finalHUll.getList();

            //prepare list and draw it
            PointF[] pointArray = pointList.ToArray();
            g.DrawPolygon(new Pen(Color.Red), pointArray);

        }

        //worst case O(logn) by itself
        public Hull convexHUll(List<System.Drawing.PointF> pointList)
        {
            //less than four is the size we are done dividing, and we make our small hulls
            if (pointList.Count() < 4)
            {
                if (pointList.Count == 2)
                {
                    return new Hull(pointList, 1);
                }
                else
                {
                    //order the three points correctly, return the correct rightmost index
                    if (slope(pointList[0], pointList[2]) > slope(pointList[0], pointList[1]))
                    {
                        List<PointF> temp = new List<PointF>();
                        temp.Add(pointList[0]);
                        temp.Add(pointList[2]);
                        temp.Add(pointList[1]);

                        return new Hull(temp, 1);
                    }
                    else
                    {
                        return new Hull(pointList, 2);
                    }
                }
            }
            else
            {
                //since size is 4 or bigger, we divide in half, and merge the results
                int secondHalf = pointList.Count() - pointList.Count() / 2;

                return combine(convexHUll(pointList.GetRange(0, pointList.Count() / 2)),
                               convexHUll(pointList.GetRange(pointList.Count() / 2, secondHalf)));
            }
        }


        //worst case O(n)
        public Hull combine(Hull left, Hull right)
        {
            //extract lists from hulls
            List<PointF> l = left.getList();
            List<PointF> r = right.getList();

            //set booleans, they come in handy
            bool bothDone = false;
            bool rightDone = false;
            bool leftDone = false;

            //those 4 ints abreviate topleftindex,toprightindex,bottomleftindex,and bottomrightindex
            int tli = left.getRightIndex();
            int tri = 0;
            int bli = left.getRightIndex();
            int bri = 0;


            //find top edge, repeat this loop until no change in left or right index
            while (!bothDone)
            {
                bothDone = true;
                rightDone = false;
                leftDone = false;

                //move right index "up" until no advantage in slope change
                while (!rightDone)
                {
                    int newIndex;

                    if (tri == 0)
                        newIndex = r.Count() - 1;
                    else
                        newIndex = tri - 1;

                    if (slope(l[tli], r[tri]) > slope(l[tli], r[newIndex]))
                    {
                        tri = newIndex;
                        bothDone = false;
                    }
                    else
                    {
                        rightDone = true;
                    }
                }

                //move left index "up" until no advantage in slope change
                while (!leftDone)
                {
                    int newIndex;

                    if (tli == (l.Count() - 1))
                        newIndex = 0;
                    else
                        newIndex = tli + 1;

                    if (slope(l[tli], r[tri]) < slope(l[newIndex], r[tri]))
                    {
                        tli = newIndex;
                        bothDone = false;
                    }
                    else
                    {
                        leftDone = true;
                    }
                }

            }


            
            bothDone = false;
            //find bottom edge, repeat this loop until no change in left or right index
            while (!bothDone)
            {
                bothDone = true;
                rightDone = false;
                leftDone = false;

                //move right index "down" until no advantage in slope change
                while (!rightDone)
                {
                    if (bri == (r.Count() - 1))
                        rightDone = true;
                    else
                    {
                        if (slope(l[bli], r[bri]) < slope(l[bli], r[bri + 1]))
                        {
                            bri++;
                            bothDone = false;
                        }
                        else
                        {
                            rightDone = true;
                        }
                    }
                }

                //move left index "down" until no advantage in slope change
                while (!leftDone)
                {
                    if (bli == 0)
                        leftDone = true;
                    else
                    {
                        if (slope(l[bli], r[bri]) > slope(l[bli - 1], r[bri]))
                        {
                            bli--;
                            bothDone = false;
                        }
                        else
                        {
                            leftDone = true;
                        }
                    }
                }
            }

            //pass in the indeces we found
            return getNewHull(bli, bri, tri, tli, right.getRightIndex(), l, r);
        }

        //worst case O(n)
        public Hull getNewHull(int bli, int bri, int tri, int tli, int rightIndex, List<PointF> l, List<PointF> r)
        {
            List<PointF> combinedList = new List<PointF>();

            //this will be the rightmost index of the combined hull
            int newRightIndex = 0;

            //this keeps track of what index to add to the new list
            int addCount = 0;

            //add from leftmost of left hull until the bottom left index
            while (addCount != (bli + 1))
            {
                combinedList.Add(l[addCount]);
                addCount++;
            }

            addCount = bri;

            //both of these while loops below add from bottom right index around the right hull 
                        //to the top right index.  It is because of a fringe case there is an if/else
            //also the new rightmost index is discovered in this loop
            if (tri == 0)
            {
                while (addCount != r.Count())
                {
                    combinedList.Add(r[addCount]);
                    if (addCount == rightIndex)
                        newRightIndex = combinedList.Count() - 1;
                    addCount++;
                }

                combinedList.Add(r[0]);

            }
            else
            {
                while (addCount != (tri + 1))
                {
                    combinedList.Add(r[addCount]);
                    if (addCount == rightIndex)
                        newRightIndex = combinedList.Count() - 1;
                    addCount++;
                }
            }

            addCount = tli;

            if (addCount != 0)
            {
                //add from the top left index the rest of the left hull
                while (addCount != l.Count())
                {
                    combinedList.Add(l[addCount]);
                    addCount++;
                }
            }

            return new Hull(combinedList, newRightIndex);
        }


        //pretty self explanatory, this came in handy
        public double slope(PointF p1, PointF p2)
        {
            return (p2.Y - p1.Y) / (p2.X - p1.X); 
        }



    }
}
