
import sys
import socket
import time
from random import randint
from pdb import set_trace as debugger
import copy

t1 = 0.0    # the amount of time remaining to player 1
t2 = 0.0    # the amount of time remaining to player 2
level = 3

state = [[0 for x in range(8)] for y in range(8)] # state[0][0] is the bottom left corner of the board (on the GUI)

# You should modify this function
# validMoves is a list of valid locations that you could place your "stone" on this turn
# Note that "state" is a global variable 2D list that shows the state of the game
def move(round, validMoves):
    # just return a random move
    #myMove = randint(0,len(validMoves)-1)
    if round < 4:
        return randint(0,len(validMoves)-1)

    # for i in range(len(validMoves)):
    #     if (validMoves[i][0] == 0 or validMoves[i][1] == 0) and (validMoves[i][0] == 7 or validMoves[i][1] == 7):
    #         debugger()
    # pprint(state)
    bestBoard, bestValue, bestMove = evaluateBoard(copy.deepcopy(state), 0, 0, -9999999, 9999999)
    
    return bestMove

def evaluateBoard(newState, depth, aMove, alpha, beta):
    # Get players turn
    turn = 1 if depth % 2 == 1 else 2
    if depth > level:
        # Stop once we are too deep
        return newState, getBoardHeuristic(newState), aMove

    #TODO: Change 2 to player eventually
    validMoves = getValidMoves(10, newState, turn)
    if depth == 0:
        # pprint(newState)
        print "newValidMoves", validMoves
    if len(validMoves) == 0:
        return newState, getBoardHeuristic(newState), aMove

    # Store the best solution so far
    bestBoard = None
    bestBoardValue = -9999999 if turn == 2 else 9999999
    bestMove = 0
    for move in range(len(validMoves)):
        if turn == 2:
            # Time to maximize!
            newBoard = createNewBoardState(newState, validMoves[move], turn)
            curBoard, curBoardValue, curMove = evaluateBoard(newBoard, depth+1, move, alpha, beta)
            if curBoardValue > bestBoardValue:
                bestBoard, bestBoardValue, bestMove = curBoard, curBoardValue, move

            alpha = max(alpha, bestBoardValue)
            if beta <= alpha:
                break
        else:
            # Time to minimize!
            newBoard = createNewBoardState(newState, validMoves[move], turn)
            curBoard, curBoardValue, curMove = evaluateBoard(newBoard, depth+1, move, alpha, beta)
            if curBoardValue < bestBoardValue:
                bestBoard, bestBoardValue, bestMove = curBoard, curBoardValue, move

            beta = min(beta, bestBoardValue)
            if beta <= alpha:
                break


    return bestBoard, bestBoardValue, bestMove


def changeColors(row, col):
    incx = 0
    incy = 0
    
    for i in xrange(-1, 2):
        for j in xrange(-1, 2):
            if (incx == 0) and (incy == 0):
                continue
            checkDirection(row, col, incx, incy, 2)


def createNewBoardState(boardState, move, player):
    newState = copy.deepcopy(boardState)
    row = move[0]
    col = move[1]
    newState[row][col] = player
    for incx in range(-1,2):
        for incy in range(-1,2):
            if ((incx == 0) and (incy == 0)):
                continue

            if (checkDirection(row,col,incx,incy,player, newState)):
                changeThisDirection(row, col, incx, incy, player-1, newState)

    return newState

# Returns heuristic of benefit to player 2
def getBoardHeuristic(newState):
    value = 0
    for i in xrange(8):
        for j in xrange(8):
            modifier = 0
            if i == 0 or j == 0 or i == 7 or j == 7:
                modifier += 2
                if (i == 0 or j == 0) and (i == 7 or j == 7):
                    modifier = 7
            if newState[i][j] == 2:
                value += (modifier)
            elif newState[i][j] == 1:
                value -= (modifier)
    # debugger()
    return value
         

#establishes a connection with the server
def initClient(me,thehost):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    server_address = (thehost, 3333+me)
    print >> sys.stderr, 'starting up on %s port %s' % server_address
    sock.connect(server_address)
    
    info = sock.recv(1024)
    
    print info

    return sock

def pprint(mat):
    for i in mat:
        for j in i:
            print j,
        print '\n'

# reads messages from the server
def readMessage(sock):
    mensaje = sock.recv(1024).split("\n")
    #print mensaje

    turn = int(mensaje[0])
    #print "Turn: " + str(turn)

    if (turn == -999):
        time.sleep(1)
        sys.exit()

    round = int(mensaje[1])
    #print "Round: " + str(round)
    t1 = float(mensaje[2])  # update of the amount of time available to player 1
    #print t1
    t2 = float(mensaje[3])  # update of the amount of time available to player 2
    #print t2

    count = 4
    for i in range(8):
        for j in range(8):
            state[i][j] = int(mensaje[count])
            count = count + 1
        #print state[i]

    return turn, round

def changeThisDirection(row, col, incx, incy, turn, newState):
    sequence = [0 for x in range(8)]
    
    seqLen = 0
    for i in xrange(1, 9):
        r = row+incy*i
        c = col+incx*i
    
        if r < 0 or r > 7 or c < 0 or c > 7:
            break
    
        sequence[seqLen] = newState[r][c]
        seqLen += 1
    
    count = 0
    for i in xrange(seqLen):
        if turn == 0:
            if sequence[i] == 2:
                count += 1
            else:
                if sequence[i] == 1 and count > 0:
                    count = 20
                break
        else:
            if sequence[i] == 1:
                count += 1
            else:
                if (sequence[i] == 2) and (count > 0):
                    count = 20
                break

    if count > 10:
        if turn == 0:
            i = 1
            r = row+incy*i
            c = col+incx*i
            while newState[r][c] == 2:
                newState[r][c] = 1
                i += 1
                r = row+incy*i
                c = col+incx*i
        else:
            i = 1
            r = row+incy*i
            c = col+incx*i
            while newState[r][c] == 1:
                newState[r][c] = 2
                i += 1
                r = row+incy*i
                c = col+incx*i


def checkDirection(row,col,incx,incy,me, boardState):
    sequence = []
    for i in range(1,8):
        r = row+incy*i
        c = col+incx*i
    
        if ((r < 0) or (r > 7) or (c < 0) or (c > 7)):
            break

        sequence.append(boardState[r][c])

    count = 0
    for i in range(len(sequence)):
        if (me == 1):
            if (sequence[i] == 2):
                count = count + 1
            else:
                if ((sequence[i] == 1) and (count > 0)):
                    return True
                break
        else:
            if (sequence[i] == 1):
                count = count+ 1
            else:
                if ((sequence[i] == 2) and (count > 0)):
                    return True
                break
    
    return False

def couldBe(row, col, me, boardState):
    for incx in range(-1,2):
        for incy in range(-1,2):
            if ((incx == 0) and (incy == 0)):
                continue

            if (checkDirection(row,col,incx,incy,me, boardState)):
                return True

    return False

# generates the set of valid moves for the player; returns a list of valid moves (validMoves)
def getValidMoves(round, boardState, me):
    validMoves = []

    if (round < 4):
        if (state[3][3] == 0):
            validMoves.append([3, 3])
        if (state[3][4] == 0):
            validMoves.append([3, 4])
        if (state[4][3] == 0):
            validMoves.append([4, 3])
        if (state[4][4] == 0):
            validMoves.append([4, 4])
    else:
        for i in range(8):
            for j in range(8):
                if (boardState[i][j] == 0):
                    if (couldBe(i, j, me, boardState)):
                        validMoves.append([i, j])

    return validMoves


# main function that (1) establishes a connection with the server, and then plays whenever it is this player's turn
def playGame(me, thehost):
    
    # create a random number generator
    
    sock = initClient(me, thehost)
    
    while (True):
        print "Read"
        status = readMessage(sock)
    
        if (status[0] == me):
            print "Move";
            validMoves = getValidMoves(status[1], state, me)
            print validMoves
            
            myMove = move(status[1], validMoves)
            while myMove > len(validMoves) - 1:
                myMove -= 1
            sel = str(validMoves[myMove][0]) + "\n" + str(validMoves[myMove][1]) + "\n";
            print "<" + sel + ">"
            sock.send(sel);
            print "sent the message"
        else:
            print "It isn't my turn"


    return



# call: python RandomGuy.py [ipaddress] [player_number]
#   ipaddress is the ipaddress on the computer the server was launched on.  Enter "localhost" if it is on the same computer
#   player_number is 1 (for the black player) and 2 (for the white player)
if __name__ == "__main__":

    print 'Number of arguments:', len(sys.argv), 'arguments.'
    print 'Argument List:', str(sys.argv)

    print str(sys.argv[1])
    
    playGame(int(sys.argv[2]), sys.argv[1])

