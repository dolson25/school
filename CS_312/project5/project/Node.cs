using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;


namespace TSP
{
    class Node
    {
        double key;
        double[,] costMatrix;
        int[] pathArray;
        int edgeCount;

        public Node(double k, double[,] cM, int[] pA, int e)
        {
            key = k;
            costMatrix = cM;
            pathArray = pA;
            edgeCount = e;
        }

        public Node(double k)
        {
            key = k;
        }

        public void setKey(double k)
        {
            key = k;
        }

        public double getKey()
        {
            return key;
        }

        public double[,] getMatrix()
        {
            return costMatrix;
        }

        public int[] getPathArray()
        {
            return pathArray;
        }

        public int getEdgeCount()
        {
            return edgeCount;
        }

    }
}
