using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;


namespace NetworkRouting
{
    class Node
    {
        PointF point;
        double key;
        int heapIndex;
        int prevNodeIndex;

        public Node(PointF p, double k, int hi)
        {
            point = p;
            key = k;
            heapIndex = hi;
            prevNodeIndex = -1;
        }

        public void setKey(double k)
        {
            key = k;
        }

        public double getKey()
        {
            return key;
        }

        public PointF getPoint()
        {
            return point;
        }

        public void setPrevNode(int i)
        {
            prevNodeIndex = i; 
        }

        public int getPrevNode()
        {
            return prevNodeIndex;
        }

        public void setHeapIndex(int i)
        {
            heapIndex = i;
        }

        public int getHeapIndex()
        {
            return heapIndex;
        }

    }
}
