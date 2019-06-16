Assignment: Laziness and Infinite Data
Name: David Olson & Jonathan Francis

Instructions: 
* Open this file in DrRacket (or your favorite plain-text editor) and add your answers 
  at the end of each line for each question. If you want to add more explanation or 
  justification, you may add one or more lines under the question.  Remember to add 
  your name as well.  Once complete, submit this to Learning Suite as a plain text file.
* For the questions asking about function correctness, indicate Yes (Y) or No (N)
  depending on whether the function meets ALL specifications.
* For each of the documentation questions, indicate Yes (Y) or No (N) based on whether
  the function has both contract and purpose statements.
* For questions asking whether a list is infinite, you only need to answer Yes (Y) 
  or No (N).
* For each of the test case questions, indicate the line number of the corresponding
  test (or tests) using "L" and the number of the line.  For example, a test on
  line 61 of the file would be "L61".  If you don't have a particular test, put "N".
* If you need to add any more explanation of justification, just add it on a line
  underneath the respective question.
  
Function: take-while
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there an empty case test?                                                               L 123
 * Is there a non-empty case test?                                                            L 125
 * Is there an infinite input case test?                                                      L 131

Function: build-infinite-list
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there at least one test?                                                                L 136
 * Is there a second test to show generalization?                                             L 138

Function: prime?
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there a test for a reasonably large prime number?                                       L 145
 * Is there a test for a reasonably large composite number?                                   L 146

Function: primes
 * Is an infinite list?                                                                       Y
 * Is there a test showing a reasonably large number of prime numbers taken from the list?    L 151

Function: prime?/fast
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there a test for a large(ish) prime number?                                             L 156
 * Is there a test for a large(ish) composite number?                                         L 157

Function: primes/fast
 * Is an infinite list?                                                                       Y
 * Is there a test showing a reasonably large number of prime numbers taken from the list?    L 162

Function: build-table
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there at least one test?                                                                L 165
 * Is there a second test to show generalization?                                             L 167

Function: lcs-length
 * Is the function correct?                                                                   Y
 * Is the function documented correctly (i.e. contract and purpose statement)?                Y
 * Is there a reasonably large test?                                                          L 170
 * Is there a second test to show generalization?                                             L 171

