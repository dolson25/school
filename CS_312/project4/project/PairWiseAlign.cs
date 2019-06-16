using System;
using System.Collections.Generic;
using System.Text;

namespace GeneticsLab
{
    class PairWiseAlign
    {
        
        /// <summary>
        /// Align only 5000 characters in each sequence.
        /// </summary>
        private int MaxCharactersToAlign = 5000; 

        /// <summary>
        /// this is the function you implement.
        /// </summary>
        /// <param name="sequenceA">the first sequence</param>
        /// <param name="sequenceB">the second sequence, may have length not equal to the length of the first seq.</param>
        /// <param name="resultTableSoFar">the table of alignment results that has been generated so far using pair-wise alignment</param>
        /// <param name="rowInTable">this particular alignment problem will occupy a cell in this row the result table.</param>
        /// <param name="columnInTable">this particular alignment will occupy a cell in this column of the result table.</param>
        /// <returns>the alignment score for sequenceA and sequenceB.  The calling function places the result in entry rowInTable,columnInTable
        /// of the ResultTable</returns>
        public int Align(GeneSequence sequenceA, GeneSequence sequenceB, ResultTable resultTableSoFar, int rowInTable, int columnInTable)
        {
            string a;
            string b;

            //limit the number of characters in each string to 5000....O(1)
            if (sequenceA.Sequence.Length > 5000)
                a = sequenceA.Sequence.Substring(0, 5000);
            else
                a = sequenceA.Sequence;

            if (sequenceB.Sequence.Length > 5000)
                b = sequenceB.Sequence.Substring(0, 5000);
            else
                b = sequenceB.Sequence;


            
            int[] topArray = new int[a.Length + 1];

            //top Array begins as the top row of our table and is hard coded....O(n)
            for (int i = 0; i < topArray.Length; i++)
            {
                topArray[i] = i * 5;
            }

            //bottom Array is where calculated scores are put
            int[] bottomArray = new int[a.Length + 1];

            int rowCount = 1;

            //outer loop pulls out one character at a time, represented by one row in the table
            //O(n) * inside the loop
            //So the total is O(n^2)
            foreach(char letter in b)
            {
                 //the first column is hard coded...O(1)
                 bottomArray[0] = rowCount * 5;
                 rowCount++;

                 //inner loop iterates through each column, calculates the score
                 //the inside calculations are constant, so O(n)
                 for(int i = 1; i < a.Length + 1; i++)
                 {
                     int diff;  //whether or not the characters in the row/col match

                     if(letter == a[i-1])
                        diff = -3;
                     else
                        diff = 1;

                     //the score algorithm based on dynammic progamming and Needleman-Wunsch
                     int score = scoreMin(diff + topArray[i - 1], 5 + topArray[i], 5 + bottomArray[i - 1]);
                     bottomArray[i] = score;
                 }

                 //the pointers to the arrays are switched so the next row can be calculated based on
                          //the row above it.  Bottom Array will be written over in the next loop
                 //Using the two array method gives us O(n) space complexity
                 int[] tempArray = topArray;
                 topArray = bottomArray;
                 bottomArray = tempArray;
             }

            //At this point the last index in topArray has the alignment score
            return topArray[a.Length];             
        }

        //my own min function that takes constant time
        public int scoreMin(int a, int b, int c)
        {
            if(a <= b)
            {
                if (a <= c)
                    return a;
                return c;
            }
            else
            {
                if (b <= c)
                    return b;
                return c;
            }
        }
    }
}
