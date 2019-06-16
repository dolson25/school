using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;


namespace NetworkRouting
{
    class HeapNode
    {
        double key;
        int pointListIndex;

        public HeapNode(double k, int pli)
        {
            key = k;
            pointListIndex = pli;
        }

        public void setKey(double k)
        {
            key = k;
        }

        public double getKey()
        {
            return key;
        }

        public int getPointListIndex()
        {
            return pointListIndex;
        }
    }
}
