

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.math.*;
import java.text.*;

class RandomGuy {

    public Socket s;
	public BufferedReader sin;
	public PrintWriter sout;
    Random generator = new Random();

    double t1, t2;
    int me;
    int otherTurn;
    int boardState;
    int state[][] = new int[8][8]; // state[0][0] is the bottom left corner of the board (on the GUI)
    int turn = -1;
    int round;
    
    int validMoves[] = new int[64];
    int numValidMoves;

    //you need to know the first action corresponding to first max value
    int action;

    boolean debug = false;
    boolean firstRoundGlobal = true;
    
    
    // main function that (1) establishes a connection with the server, and then plays whenever it is this player's turn
    public RandomGuy(int _me, String host) {
        me = _me;

        if(me == 1)
            otherTurn = 2;
        else
            otherTurn = 1;

        initClient(host);

        int myMove;
        
        while (true) {
            System.out.println("Read");
            readMessage();
            
            if (turn == me) {
                System.out.println("Move");
                getValidMoves(round, state, me);
                
                myMove = move();
                
                String sel = validMoves[myMove] / 8 + "\n" + validMoves[myMove] % 8;
                
                System.out.println("Selection: " + validMoves[myMove] / 8 + ", " + validMoves[myMove] % 8);
                
                sout.println(sel);
            }
        }
    }
    
    // You should modify this function
    // validMoves is a list of valid locations that you could place your "stone" on this turn
    // Note that "state" is a global variable 2D list that shows the state of the game
    private int move() {
        //return myMove;
        firstRoundGlobal = true;

        int finalMaxValue = 0;
        if(round < 10)
            finalMaxValue = maxValue(round,state,Integer.MIN_VALUE, Integer.MAX_VALUE,17);
        else
            finalMaxValue = maxValue(round,state,Integer.MIN_VALUE, Integer.MAX_VALUE,0);
        System.out.println("ACTION: " + action);
        printValidMoves(validMoves, numValidMoves);
        return action;
    }

    private int maxValue(int r, int[][]  st, int alpha, int beta, int depth){

        if(debug){
            System.out.println("Round: " + r);
            System.out.println("Depth: " + depth);
            System.out.println("Computer's move, get maxValue");
            System.out.println("State:");
            printState(st);
        }

        boolean firstNode = false;

        if(firstRoundGlobal){
            firstNode = true;
            firstRoundGlobal = false;
        }


        //get numValidMoves and valid moves [] for this state
        ValidMovesReturn theMoves = otherGetValidMoves(r, st, me);
        int numMoves = theMoves.nvMoves;
        int[] moves = theMoves.vMoves;

        if(true) {
            if (arrayContains(moves, numMoves,0)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,0);
                return Integer.MAX_VALUE;
            }

            if (arrayContains(moves, numMoves,7)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,7);
                return Integer.MAX_VALUE;
            }

            if (arrayContains(moves, numMoves,56)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,56);
                return Integer.MAX_VALUE;
            }

            if (arrayContains(moves, numMoves,63)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,63);
                return Integer.MAX_VALUE;
            }


            //sides without next to corners
            if (arrayContains(moves, numMoves,2)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,2);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,3)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,3);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,4)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,4);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,5)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,5);
                return Integer.MAX_VALUE / 2;
            }


            //sides without next to corners
            if (arrayContains(moves, numMoves,58)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,58);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,59)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,59);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,60)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,60);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,61)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,61);
                return Integer.MAX_VALUE / 2;
            }

            //sides without next to corners
            if (arrayContains(moves, numMoves,16)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,16);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,24)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,24);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,32)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,32);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,40)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,40);
                return Integer.MAX_VALUE / 2;
            }

            //sides without next to corners
            if (arrayContains(moves, numMoves,23)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,23);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,31)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,31);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,39)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,39);
                return Integer.MAX_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,47)) {
                if(firstNode)
                    action = indexOf(moves, numMoves,47);
                return Integer.MAX_VALUE / 2;
            }
        }

            //if(firstNode){
              //  action = i;
                //System.out.println("RETURNING: " + Integer.MAX_VALUE);
                //System.out.println("WITH ACTION OF: " + action);
                //System.out.println("AT DEPTH: " + depth);
                //System.out.println("THE MOVE WAS: " + possibleMove);
                //System.out.println("OUT OF THESE VALID MOVES:");
                //printValidMoves(moves,numMoves);

            //}



        //}

        if(debug){
            System.out.println("Valid Moves:");
            printValidMoves(moves, numMoves);
        }

        //if terminal state
        if((numMoves == 0) || (depth > 18)){
            //if(numMoves == 0) {
            //return utility(state)
            return countMyPieces(st);
        }

        //start with a very low max
        int maxMove = Integer.MIN_VALUE;

        //for each possible action
        for(int i = 0; i < numMoves; i++){
            int possibleMove = moves[i];

            //make the new state
            int iCoord = possibleMove / 8;
            int jCoord = possibleMove % 8;

            int[][] newState = new int [8][8];

            for(int j = 0; j < 8; j++){
                for(int k = 0; k < 8; k++){
                    newState[j][k] = st[j][k];
                }
            }

            newState[iCoord][jCoord] = me;
            changeColors(iCoord,jCoord,me,newState);

            //hand that new state to the min function
            int nextRound = r + 1;
            int thisMinValue = minValue(nextRound, newState, alpha, beta, depth + 1);

            //now see if it is your new max
            maxMove = Math.max(maxMove, thisMinValue);

            if(firstNode){
                if(maxMove == thisMinValue){
                    action = i;
                }
            }

            if(maxMove >= beta)
                return maxMove;

            alpha = Math.max(alpha, maxMove);

        }

        if(debug){
            System.out.println("MAX VALUE: " + maxMove);
        }

        return maxMove;
    }

    //private int minValue(int r, int[][]  st, int depth){
    private int minValue(int r, int[][]  st, int alpha, int beta, int depth){

        if(debug){
            System.out.println("Round: " + r);
            System.out.println("Human's move, get minValue");
            System.out.println("State:");
            printState(st);
        }

        //get numValidMoves and valid moves [] for this state
        ValidMovesReturn theMoves = otherGetValidMoves(r, st, otherTurn);
        int numMoves = theMoves.nvMoves;
        int[] moves = theMoves.vMoves;

        if(true) {
            if (arrayContains(moves, numMoves,0)) {
                return Integer.MIN_VALUE;
            }

            if (arrayContains(moves, numMoves,7)) {
                return Integer.MIN_VALUE;
            }

            if (arrayContains(moves, numMoves,56)) {
                return Integer.MIN_VALUE;
            }

            if (arrayContains(moves, numMoves,63)) {
                return Integer.MIN_VALUE;
            }


            //sides without next to corners
            if (arrayContains(moves, numMoves,2)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,3)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,4)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,5)) {
                return Integer.MIN_VALUE / 2;
            }


            //sides without next to corners
            if (arrayContains(moves, numMoves,58)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,59)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,60)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,61)) {
                return Integer.MIN_VALUE / 2;
            }

            //sides without next to corners
            if (arrayContains(moves, numMoves,16)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,24)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,32)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,40)) {
                return Integer.MIN_VALUE / 2;
            }

            //sides without next to corners
            if (arrayContains(moves, numMoves,23)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,31)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,39)) {
                return Integer.MIN_VALUE / 2;
            }

            if (arrayContains(moves, numMoves,47)) {
                return Integer.MIN_VALUE / 2;
            }
        }

        if(debug){
            System.out.println("Valid Moves:");
            System.out.println("");
            printValidMoves(moves, numMoves);
            System.out.println("");
        }
        //if terminal state
        if((numMoves == 0) || (depth > 18)){
        //if(numMoves == 0) {
            //return utility(state)
            return countMyPieces(st);
        }

        //start with a very high min
        int minMove = Integer.MAX_VALUE;

        //for each possible action
        for(int i = 0; i < numMoves; i++){
            int possibleMove = moves[i];

            //make the new state
            int iCoord = possibleMove / 8;
            int jCoord = possibleMove % 8;

            int[][] newState = new int[8][8];

            for(int j = 0; j < 8; j++){
                for(int k = 0; k < 8; k++){
                    newState[j][k] = st[j][k];
                }
            }

            newState[iCoord][jCoord] = otherTurn;
            changeColors(iCoord,jCoord,otherTurn,newState);

            //hand that new state to the min function
            int nextRound = r + 1;
            //int thisMaxValue = maxValue(nextRound, newState, depth + 1);
            int thisMaxValue = maxValue(nextRound, newState, alpha, beta, depth + 1);

            //now see if it is your new min
            minMove = Math.min(minMove, thisMaxValue);

            if(minMove <= alpha)
                return minMove;

            beta = Math.min(beta, minMove);
        }

        if(debug){
            System.out.println("MIN VALUE: " + minMove);
        }

        return minMove;
    }

    private int countMyPieces(int[][] st){
        int countMe = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (st[i][j] == me)
                    countMe ++;
            }
        }

        return countMe;
    }

    public int[][] checkDirection2(int row, int col, int incx, int incy, int turn, int[][] st) {
        int sequence[] = new int[7];
        int seqLen;
        int i, r, c;

        seqLen = 0;
        for (i = 1; i < 8; i++) {
            r = row+incy*i;
            c = col+incx*i;

            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
                break;

            sequence[seqLen] = st[r][c];
            seqLen++;
        }

        int count = 0;
        for (i = 0; i < seqLen; i++) {
            if (turn == 1) {
                if (sequence[i] == 2)
                    count ++;
                else {
                    if ((sequence[i] == 1) && (count > 0))
                        count = 20;
                    break;
                }
            }
            else {
                if (sequence[i] == 1)
                    count ++;
                else {
                    if ((sequence[i] == 2) && (count > 0))
                        count = 20;
                    break;
                }
            }
        }

        if (count > 10) {
            if (turn == 1) {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (st[r][c] == 2) {
                    st[r][c] = 1;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
            else {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (st[r][c] == 1) {
                    st[r][c] = 2;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
        }

        return st;
    }

    public void changeColors(int row, int col, int turn, int[][] st) {
        int incx, incy;

        for (incx = -1; incx < 2; incx++) {
            for (incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;

                checkDirection2(row, col, incx, incy, turn, st);
            }
        }
    }

    // generates the set of valid moves for the player; returns a list of valid moves (validMoves)
    private ValidMovesReturn otherGetValidMoves(int round, int st[][], int turn) {
        int i, j;

        int[] thisValidMoves = new int[64];

        int thisNumValidMoves = 0;

        if (round < 4) {
            if (st[3][3] == 0) {
                thisValidMoves[thisNumValidMoves] = 3*8 + 3;
                thisNumValidMoves ++;
            }
            if (st[3][4] == 0) {
                thisValidMoves[thisNumValidMoves] = 3*8 + 4;
                thisNumValidMoves ++;
            }
            if (st[4][3] == 0) {
                thisValidMoves[thisNumValidMoves] = 4*8 + 3;
                thisNumValidMoves ++;
            }
            if (st[4][4] == 0) {
                thisValidMoves[thisNumValidMoves] = 4*8 + 4;
                thisNumValidMoves ++;
            }
        }
        else {
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (st[i][j] == 0) {
                        if (couldBe(st, i, j, turn)) {
                            thisValidMoves[thisNumValidMoves] = i*8 + j;
                            thisNumValidMoves ++;
                        }
                    }
                }
            }
        }

        return new ValidMovesReturn(thisNumValidMoves,thisValidMoves);
    }

    // generates the set of valid moves for the player; returns a list of valid moves (validMoves)
    private void getValidMoves(int round, int state[][], int turn) {
        int i, j;
        
        numValidMoves = 0;
        if (round < 4) {
            if (state[3][3] == 0) {
                validMoves[numValidMoves] = 3*8 + 3;
                numValidMoves ++;
            }
            if (state[3][4] == 0) {
                validMoves[numValidMoves] = 3*8 + 4;
                numValidMoves ++;
            }
            if (state[4][3] == 0) {
                validMoves[numValidMoves] = 4*8 + 3;
                numValidMoves ++;
            }
            if (state[4][4] == 0) {
                validMoves[numValidMoves] = 4*8 + 4;
                numValidMoves ++;
            }
            if(debug)
                System.out.println("Valid Moves:");
            for (i = 0; i < numValidMoves; i++) {
                if(debug)
                    System.out.println(validMoves[i] / 8 + ", " + validMoves[i] % 8);
            }
        }
        else {
            //System.out.println("Valid Moves:");
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        if (couldBe(state, i, j, turn)) {
                            validMoves[numValidMoves] = i*8 + j;
                            numValidMoves ++;
                            //System.out.println(i + ", " + j);
                        }
                    }
                }
            }
        }
    }


    
    private boolean checkDirection(int state[][], int row, int col, int incx, int incy, int turn) {
        int sequence[] = new int[7];
        int seqLen;
        int i, r, c;
        
        seqLen = 0;
        for (i = 1; i < 8; i++) {
            r = row+incy*i;
            c = col+incx*i;
        
            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
                break;
        
            sequence[seqLen] = state[r][c];
            seqLen++;
        }
        
        int count = 0;
        for (i = 0; i < seqLen; i++) {
            if (turn == 1) {
                if (sequence[i] == 2)
                    count ++;
                else {
                    if ((sequence[i] == 1) && (count > 0))
                        return true;
                    break;
                }
            }
            else {
                if (sequence[i] == 1)
                    count ++;
                else {
                    if ((sequence[i] == 2) && (count > 0))
                        return true;
                    break;
                }
            }
        }
        
        return false;
    }
    
    private boolean couldBe(int state[][], int row, int col, int turn) {
        int incx, incy;
        
        for (incx = -1; incx < 2; incx++) {
            for (incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;
            
                if (checkDirection(state, row, col, incx, incy, turn))
                    return true;
            }
        }
        
        return false;
    }
    
    public void readMessage() {
        int i, j;
        String status;
        try {
            //System.out.println("Ready to read again");
            turn = Integer.parseInt(sin.readLine());
            
            if (turn == -999) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                
                System.exit(1);
            }
            
            //System.out.println("Turn: " + turn);
            round = Integer.parseInt(sin.readLine());
            t1 = Double.parseDouble(sin.readLine());
            System.out.println(t1);
            t2 = Double.parseDouble(sin.readLine());
            System.out.println(t2);
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    state[i][j] = Integer.parseInt(sin.readLine());
                }
            }
            sin.readLine();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        System.out.println("Round: " + round);
        for (i = 7; i >= 0; i--) {
            for (j = 0; j < 8; j++) {
                System.out.print(state[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void initClient(String host) {
        int portNumber = 3333+me;
        
        try {
			s = new Socket(host, portNumber);
            sout = new PrintWriter(s.getOutputStream(), true);
			sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            String info = sin.readLine();
            System.out.println(info);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void printState(int[][] st) {
        int i, j;

        for (i = 7; i >= 0; i--) {
            for (j = 0; j < 8; j++) {
                System.out.print(st[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printValidMoves(int[] vms, int numMoves){
        for(int i = 0; i < numMoves; i++){
            int vmrow = vms[i] / 8;
            int vmcol = vms[i] % 8;
            System.out.println(vmrow + ", " + vmcol);
        }
    }

    private boolean arrayContains(int[] array, int numMoves, int value){
        for(int i = 0; i < numMoves; i++){
            if(array[i]==value)
                return true;
        }

        return false;
    }

    private int indexOf(int[] array, int numMoves, int value){
        for(int i = 0; i < numMoves; i++){
            if(array[i]==value)
                return i;
        }
        return 0;
    }

    
    // compile on your machine: javac *.java
    // call: java RandomGuy [ipaddress] [player_number]
    //   ipaddress is the ipaddress on the computer the server was launched on.  Enter "localhost" if it is on the same computer
    //   player_number is 1 (for the black player) and 2 (for the white player)
    public static void main(String args[]) {
        new RandomGuy(Integer.parseInt(args[1]), args[0]);
    }

    private class ValidMovesReturn{
        public int nvMoves;
        public int[] vMoves;

        public ValidMovesReturn(int nvm, int[] vm){
            nvMoves = nvm;
            vMoves = vm;
        }
    }
    
}
