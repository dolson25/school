using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace TSP
{
    class BinaryHeap
    {
        Node[] myQueue;
        int queueSize;

        public BinaryHeap(int size)
        {
            myQueue = new Node[size];
            for (int i = 0; i < size; i++)
            {
                myQueue[i] = new Node(double.MaxValue);
            }
            queueSize = 0;
        }


        public void insert(Node n)
        {
            myQueue[queueSize + 1] = n;
            queueSize++;
            bubbleUp(n, queueSize);
        }

        public Node deleteMin()
        {
            Node n;
            if (queueSize == 0)
                return null;
            else if (queueSize == 1)
            {
                queueSize = 0;
                Node temp = myQueue[1];
                myQueue[1] = new Node(double.MaxValue);
                return temp;
            }
            else
            {
                n = myQueue[1];

                Node temp = myQueue[queueSize];
                myQueue[queueSize] = new Node(double.MaxValue);
                queueSize--;
                siftDown(temp, 1);
                return n;
            }
        }

        public void siftDown(Node n, int i)
        {
            int c = minChild(i);

            while ((c != 0) && (myQueue[c].getKey() < n.getKey()))
            {
                myQueue[i] = myQueue[c];
                i = c;
                c = minChild(i);
            }

            myQueue[i] = n;
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

        public void bubbleUp(Node n, int i)
        {
            int p = (i / 2);

            while ((i != 1) && (myQueue[p].getKey() > n.getKey()))
            {
                myQueue[i] = myQueue[p];
                i = p;
                p = (i / 2);
            }

            myQueue[i] = n;
        }

        public int getQueueSize()
        {
            return queueSize;
        }

        public void printQueue()
        {
            for (int i = 0; i < myQueue.Length; i++)
            {
                System.Console.Write(i + " " + myQueue[i].getKey() + "\n");
            }

        }


    }
}
