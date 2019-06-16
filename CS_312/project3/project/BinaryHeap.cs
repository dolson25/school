using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace NetworkRouting
{
    class BinaryHeap
    {
        HeapNode[] myQueue;
        int queueSize;
        Node[] nodeInfo;  //a pointer to an array in the solveButton_click function


        //one path constructor
        public BinaryHeap(int size, Node[] info, int start)
        {
            nodeInfo = info;
            myQueue = new HeapNode[size];

            //put the start node on the queue
            myQueue[1] = new HeapNode(0, start);
            nodeInfo[start].setKey(0);
            nodeInfo[start].setHeapIndex(1);

            queueSize = 1;
        }

        //all paths constructor
        public BinaryHeap(int size, Node[] info,  int start, int toMakeTwoConstructors)
        {
            nodeInfo = info;
            myQueue = new HeapNode[size];

            //put the start node on the queue
            myQueue[1] = new HeapNode(0, start);
            nodeInfo[start].setKey(0);
            nodeInfo[start].setHeapIndex(1);


            //insert all the rest of the nodes
            int j = 0;
            for (int i = 2; i < myQueue.Count(); i++)
            {
                if (j != start)
                {
                    myQueue[i] = new HeapNode(double.MaxValue, j);
                    nodeInfo[j].setHeapIndex(i);
                }
                else
                {
                    j++;
                    myQueue[i] = new HeapNode(double.MaxValue, j);
                    nodeInfo[j].setHeapIndex(i);
                }
                j++;
            }

            queueSize = size - 1;
        }


        public void insert(HeapNode n)
        {
            myQueue[queueSize + 1] = n;
            queueSize++;
            bubbleUp(n, queueSize);
        }

        public HeapNode deleteMin()
        {
            HeapNode n;
            if (queueSize == 0)
                return null;
            else if (queueSize == 1)
            {
                queueSize = 0;
                HeapNode temp = myQueue[1];
                myQueue[1] = new HeapNode(double.MaxValue, -1);

                //pointer array stuff
                //-1 means it's not on the queue anymore
                nodeInfo[temp.getPointListIndex()].setHeapIndex(-1);

                return temp;
            }
            else
            {
                n = myQueue[1];

                HeapNode temp = myQueue[queueSize];
                myQueue[queueSize] = new HeapNode(double.MaxValue, -1);
                queueSize--;
                siftDown(temp, 1);

                //pointer array stuff
                nodeInfo[n.getPointListIndex()].setHeapIndex(-1);

                return n;
            }
        }

        public void siftDown(HeapNode n, int i)
        {
            int c = minChild(i);

            while ((c != 0) && (myQueue[c].getKey() < n.getKey()))
            {
                myQueue[i] = myQueue[c];

                //pointer array stuff
                nodeInfo[myQueue[i].getPointListIndex()].setHeapIndex(i);

                i = c;
                c = minChild(i);
            }

            myQueue[i] = n;

            //pointer array stuff
            nodeInfo[n.getPointListIndex()].setHeapIndex(i);
        }

        public int minChild(int i)
        {
            if (2 * i > queueSize)
                return 0;
            else
            {
                if ((2 * i + 1) > queueSize)
                {
                    return 2 * i;
                }
                if (myQueue[2 * i].getKey() < myQueue[2 * i + 1].getKey())
                {
                    return 2 * i;
                }

                return ((2 * i) + 1);
            }


        }

        public void bubbleUp(HeapNode n, int i)
        {
            int p = (i / 2);

            while ((i != 1) && (myQueue[p].getKey() > n.getKey()))
            {
                myQueue[i] = myQueue[p];

                //pointer array stuff
                nodeInfo[myQueue[i].getPointListIndex()].setHeapIndex(i);

                i = p;
                p = (i / 2);
            }

            myQueue[i] = n;

            //pointer array stuff
            nodeInfo[n.getPointListIndex()].setHeapIndex(i);
        }

        public int getQueueSize()
        {
            return queueSize;
        }

        public void setQueueSize(int s)
        {
            queueSize = s;
        }

        public void decreaseKey(int index, double newKey)
        {
            myQueue[index].setKey(newKey);
            bubbleUp(myQueue[index], index);
        }

        public void printQueue()
        {
            for (int i = 0; i < myQueue.Length; i++)
            {
                System.Console.Write(i + " " + myQueue[i].getKey() + "   " + myQueue[i].getPointListIndex() + "\n");
            }

        }


    }
}
