using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.IO;


namespace GeneticsLab
{
    public partial class MainForm : Form
    {
        ResultTable m_resultTable;
        GeneSequence[] m_sequences;
        const int NUMBER_OF_SEQUENCES = 10;
        const string GENOME_FILE = "genomes.txt";

        int[,] pointerArray;

        public MainForm()
        {
            InitializeComponent();

            statusMessage.Text = "Loading Database...";

            // load database here

            try
            {
                m_sequences = loadFile("../../" + GENOME_FILE);
            }
            catch (FileNotFoundException e)
            {
                try // Failed, try one level down...
                {
                    m_sequences = loadFile("../" + GENOME_FILE);
                }
                catch (FileNotFoundException e2)
                {
                    // Failed, try same folder
                    m_sequences = loadFile(GENOME_FILE);
                }
            }

            m_resultTable = new ResultTable(this.dataGridViewResults, NUMBER_OF_SEQUENCES);

            statusMessage.Text = "Loaded Database.";

        }

        private GeneSequence[] loadFile(string fileName)
        {
            StreamReader reader = new StreamReader(fileName);
            string input = "";

            try
            {
                input = reader.ReadToEnd();
            }
            catch
            {
                Console.WriteLine("Error Parsing File...");
                return null;
            }
            finally
            {
                reader.Close();
            }

            GeneSequence[] temp = new GeneSequence[NUMBER_OF_SEQUENCES];
            string[] inputLines = input.Split('\r');

            for (int i = 0; i < NUMBER_OF_SEQUENCES; i++)
            {
                string[] line = inputLines[i].Replace("\n","").Split('#');
                temp[i] = new GeneSequence(line[0], line[1]);
            }
            return temp;
        }

        private void fillMatrix()
        {
            PairWiseAlign processor = new PairWiseAlign();
            for (int x = 0; x < NUMBER_OF_SEQUENCES; ++x)
            {
                for (int y = 0; y < NUMBER_OF_SEQUENCES; ++y)
                {
                    m_resultTable.SetCell(x, y, processor.Align(m_sequences[x], m_sequences[y],m_resultTable,x,y));
                }
            }
        }

        private void processButton_Click(object sender, EventArgs e)
        {
            statusMessage.Text = "Processing...";
            Stopwatch timer = new Stopwatch();
            timer.Start();
                   fillMatrix();
            timer.Stop();
            statusMessage.Text = "Done.  Time taken: " + timer.Elapsed;

        }


        private void dataGridViewResults_CellMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            int r = e.RowIndex;
            int c = e.ColumnIndex;

            string a;
            string b;

            //for long strings, just pull out the first 100 characters...O(1)
            if (m_sequences[r].Sequence.Length > 100)
                a = m_sequences[r].Sequence.Substring(0, 100);
            else
                a = m_sequences[r].Sequence;

            if (m_sequences[c].Sequence.Length > 100)
                b = m_sequences[c].Sequence.Substring(0, 100);
            else
                b = m_sequences[c].Sequence;


            //two 2D arrays are created.  One has the entire score table.  The other has integers that
                 //indicate the alignment (1=Left, 2=Diagonal, 3=Up)
            //Keeping track of the alignment makes this O(n^2) space complexity
            int[,] scoreArray = new int[b.Length + 1, a.Length + 1];
            pointerArray = new int[b.Length + 1, a.Length + 1];

            //the top row and first column of our score and pointer tables are hard coded...O(n)
            for (int i = 0; i < b.Length + 1; i++)
            {
                scoreArray[i, 0] = i * 5;
                pointerArray[i, 0] = 3;
            }
            for (int j = 0; j < a.Length + 1; j++)
            {
                scoreArray[0, j] = j * 5;
                pointerArray[0, j] = 1;
            }



            //this is extremely similar to the score algorithm, and is O(n^2) 
            for(int i = 1; i < b.Length + 1; i++)
            {
                for (int j = 1; j < a.Length + 1; j++)
                {
                    int diff;

                    if (b[i-1] == a[j - 1])
                        diff = -3;
                    else
                        diff = 1;

                    int min = extractionMin(5 + scoreArray[i,j-1], diff + scoreArray[i-1,j-1], 5 + scoreArray[i - 1,j], i,j);
                    scoreArray[i, j] = min;
                }
            }

            //now instead of retrieving the scores, we create two alignment strings
            StringBuilder sba = new StringBuilder();
            StringBuilder sbb = new StringBuilder();

            int row = b.Length;
            int col = a.Length;

            //backtrack from the bottom right of the table to the top left
            //I used string builder insert which may slow things a bit, but this part
                     //is certainly in O(n^2)
            while((row != 0) | (col != 0))
            {
                //pointer was diagonal, add a char to each string
                if(pointerArray[row,col] == 2)
                {
                    sba.Insert(0,a[col - 1]);
                    sbb.Insert(0, b[row - 1]);
                    row--;
                    col--;
                }
                //other two options involve a "-" and a char
                else if(pointerArray[row,col] == 1)
                {
                    sbb.Insert(0,'-');
                    sba.Insert(0,a[col - 1]);
                    col--;
                }
                else
                {
                    sba.Insert(0, '-');
                    sbb.Insert(0, b[row - 1]);
                    row--;
                }
            }

            //display the two strings
            textBox1.Text = sba.ToString() + Environment.NewLine + sbb.ToString();
        }

        //this min function adds on the functionality of inserting into the pointer array
        //still constant time
        public int extractionMin(int a, int b, int c, int row, int col)
        {
            if (a <= b)
            {
                if (a <= c)
                {
                    pointerArray[row, col] = 1;
                    return a;
                }
                pointerArray[row, col] = 3;
                return c;
            }
            else
            {
                if (b <= c)
                {
                    pointerArray[row, col] = 2;
                    return b;
                }
                pointerArray[row, col] = 3;
                return c;
            }
        }

        
    }
}