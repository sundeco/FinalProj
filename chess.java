import java.util.Arrays;
import java.util.ArrayList;
/**
 * 1/28/19: First working version without en passant, castling, or mate detection. Defeated Ivan's chess AI easy.
 * 1/29/19: Gained +15 advantage against chess.com 800 rating computer. Loses to me, brother, dad. Added alpha beta pruning-8 deep in opening.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class chess {
    public static void main(String args[]) {
        int[][] starting = new int[][] {
                {-3,-5,-4,-2,-1,-4,-5,-3},
                {-6,-6,-6,-6,-6,-6,-6,-6},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {6,6,6,6,6,6,6,6},
                {3,5,4,2,1,4,5,3},
            };
        int[][] board = new int[][] {
                {0,0,0,0,-1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0},
                {2,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
            };
        board = starting;
        int gameState = 1; //0=opening, 1=midgame, 2=endgame
        printBoard(board);
        String firstMove = "";
        for(int i = 5; i < 18; i++) {
            firstMove = testAI(board, i, gameState, -10000000, 10000000, firstMove);
            System.out.println(testAI(board, i, gameState, -10000000, 10000000, firstMove));
        }
    }

    public static String testAI(int[][] board, int depth, int gameState, double alpha, double beta, String firstMove) {
        String bestMove = "";
        double bestValue = -1000000;
        ArrayList <Integer> moves = new ArrayList<Integer>();
        if (!firstMove.equals("")) {
            moves.add(Integer.parseInt(firstMove.substring(7,8)));
            moves.add(Integer.parseInt(firstMove.substring(9,10)));
            moves.add(Integer.parseInt(firstMove.substring(0,1)));
            moves.add(Integer.parseInt(firstMove.substring(2,3)));
            moves.add(Integer.parseInt(firstMove.substring(11,12)));
        }
        moves = giveMoves(moves, board, 1);
        for(int k = 0; k < moves.size()/5; k++) {
            int movetorow = moves.get(5*k);
            int movetocolumn = moves.get(5*k+1);
            int i = moves.get(5*k+2);
            int j = moves.get(5*k+3);
            int x = moves.get(5*k+4);
            int y = board[movetorow][movetocolumn];
            if(move(board, i, j, movetorow, movetocolumn)) {
                //printBoard(board);
                //System.out.println(depth);
                double v = minimax(board, depth-1, false, gameState, alpha, beta);
                if(bestValue < v) {
                    bestValue = v;
                    bestMove = i + " " + j + " to " + movetorow + " " + movetocolumn + " " + x;
                }
                alpha = Math.max(alpha, bestValue);
                if(alpha >= beta) {
                    board[i][j] = x; //undo the move
                    board[movetorow][movetocolumn] = y;
                    break;
                }
                //System.out.println(bestValue);
                board[i][j] = x; //undo the move
                board[movetorow][movetocolumn] = y;
            }
        }
        return bestMove;
    }

    public static double minimax(int[][] board, int depth, boolean maximizingPlayer, int gameState, double alpha, double beta) {
        if(depth == 0) {
            return evalPosition(board, gameState);
        }
        if(maximizingPlayer) {
            double bestValue = -10000000;
            ArrayList<Integer> moves = new ArrayList<Integer>();
            moves = giveMoves(moves, board, 1);
            if(moves.size() == 0) {
                return evalPosition(board, gameState);
            }
            for(int k = 0; k < moves.size()/5; k++) {
                int movetorow = moves.get(5*k);
                int movetocolumn = moves.get(5*k+1);
                int i = moves.get(5*k+2);
                int j = moves.get(5*k+3);
                int x = moves.get(5*k+4);
                int y = board[movetorow][movetocolumn];
                if(move(board, i, j, movetorow, movetocolumn)) {
                    //printBoard(board);
                    //System.out.println(depth);
                    double v = minimax(board, depth-1, false, gameState, alpha, beta);
                    bestValue = Math.max(bestValue, v);
                    alpha = Math.max(alpha, bestValue);
                    if(alpha >= beta) {
                        board[i][j] = x; //undo the move
                        board[movetorow][movetocolumn] = y;
                        break;
                    }
                    //System.out.println(bestValue);
                    board[i][j] = x; //undo the move
                    board[movetorow][movetocolumn] = y;
                }
            }
            //System.out.println(bestValue);
            return bestValue;
        }
        else {
            //System.out.println(1);
            double bestValue = 10000000;
            ArrayList<Integer> moves = new ArrayList<Integer>();
            moves = giveMoves(moves, board, -1);
            if(moves.size() == 0) {
                return evalPosition(board, gameState);
            }
            for(int k = 0; k < moves.size()/5; k++) {
                int movetorow = moves.get(5*k);
                int movetocolumn = moves.get(5*k+1);
                int i = moves.get(5*k+2);
                int j = moves.get(5*k+3);
                int x = moves.get(5*k+4);
                int y = board[movetorow][movetocolumn];
                if(move(board, i, j, movetorow, movetocolumn)) {
                    //printBoard(board);
                    //System.out.println(depth);
                    double v = minimax(board, depth-1, true, gameState, alpha, beta);
                    bestValue = Math.min(bestValue, v);
                    beta = Math.min(beta, bestValue);
                    if(alpha >= beta) {
                        board[i][j] = x; //undo the move
                        board[movetorow][movetocolumn] = y;
                        break;
                    }
                    //System.out.println(bestValue);
                    board[i][j] = x; //undo the move
                    board[movetorow][movetocolumn] = y;
                }
            }
            //System.out.println(bestValue);
            return bestValue;
        }
    }

    public static double evalPosition(int[][] board, int state) {
        double score = 0;
        double move = 0.01;
        double centerControl = 0.03;
        double kingSafety = 0.2;
        double bishopPair = 0.5;
        double inactivePiece = 0.1;
        boolean isWKing = false;
        boolean isBKing = false;
        int wkingX = 0;
        int wkingY = 0;
        int bkingX = 0;
        int bkingY = 0;
        int bbishops = 0;
        int wbishops = 0;
        double [] pawnScore = {0, 0, 0.01, 0.02, 0.08, 0.16};
        double [] openingPawnScore = {0, 0, 0.01, 0.02, 0.04, 0.08};
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                switch(board[i][j]) {
                    case -1: isBKing = true; bkingX = i; bkingY = j;
                    break;
                    case 1: isWKing = true; wkingX = i; wkingY = j; break;
                    case -2: score = score - 9;
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 2: score = score + 9.5; 
                    if(state < 2 && i == 0) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -3: score = score - 5;
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 3: score = score + 5.3;
                    if(state < 2 && i == 0) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -4: score = score - 3; bbishops++; 
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 4: score = score + 3.2; wbishops++; 
                    if(state < 2 && i == 0) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -5: score = score - 3; 
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 5: score = score + 3.2;
                    if(state < 2 && i == 0) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -6: score = score - 1; break;
                    case 6: score = score + 1.05; break;
                }
            }
        }
        if(bbishops == 2) {
            score = score - bishopPair;
        }
        if(wbishops == 2) {
            score = score + bishopPair;
        }
        if(!isWKing) {
            return -100000;
        }
        else if(!isBKing) {
            return 10000;
        }
        if(state == 1) {//midgame
            if(bkingX == 0 || bkingX == 7) {
                score -= kingSafety;
            }
            if(bkingY == 0 || bkingY == 7) {
                score -= kingSafety;
            }
            if(wkingX == 0 || wkingX == 7) {
                score += kingSafety;
            }
            if(wkingY == 0 || wkingY == 7) {
                score += kingSafety;
            }
            return score;
        }
        else if(state == 0){ //opening
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board[i][j] <= 1) {
                        continue;
                    }
                    int x = board[i][j];
                    //have each piece make their move, dostuff, then undo the move
                    if(x == 2) { //queen
                        int a = 0; 
                        while(i > a) {
                            a++;
                            if(board[i-a][j] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i-a][j] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7) {
                            a++;
                            if(board[i+a][j] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i+a][j] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j > a) {
                            a++;
                            if(board[i][j-a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i][j-a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j + a < 7) {
                            a++;
                            if(board[i][j+a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i][j+a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0; 
                        while(i > a && j > a) {
                            a++;
                            if(board[i-a][j-a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i-a][j-a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7 && j > a) {
                            a++;
                            if(board[i+a][j-a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i+a][j-a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0; 
                        while(i+a < 7 && j+a < 7) {
                            a++;
                            if(board[i+a][j+a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i+a][j+a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i > a && j +a < 7) {
                            a++;
                            if(board[i-a][j+a] < 0) {
                                score = score + move/2;
                                break;
                            }
                            else if(board[i-a][j+a] == 0) {
                                score = score + move/2;
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else if(x == 3) { //rook
                        int a = 0; 
                        while(i > a) {
                            a++;
                            if(board[i-a][j] < 0) {
                                score = score + move*0.7;
                                break;
                            }
                            else if(board[i-a][j] == 0) {
                                score = score + move*0.7;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7) {
                            a++;
                            if(board[i+a][j] < 0) {
                                score = score + move*0.7;
                                break;
                            }
                            else if(board[i+a][j] == 0) {
                                score = score + move*0.7;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j > a) {
                            a++;
                            if(board[i][j-a] < 0) {
                                score = score + move*0.7;
                                break;
                            }
                            else if(board[i][j-a] == 0) {
                                score = score + move*0.7;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j + a < 7) {
                            a++;
                            if(board[i][j+a] < 0) {
                                score = score + move*0.7;
                                break;
                            }
                            else if(board[i][j+a] == 0) {
                                score = score + move*0.7;
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else if(x == 4) { //bishop
                        int a = 0; 
                        while(i > a && j > a) {
                            a++;
                            if(board[i-a][j-a] < 0) {
                                score = score + move;
                                break;
                            }
                            else if(board[i-a][j-a] == 0) {
                                score = score + move;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7 && j > a) {
                            a++;
                            if(board[i+a][j-a] < 0) {
                                score = score + move;
                                break;
                            }
                            else if(board[i+a][j-a] == 0) {
                                score = score + move;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0; 
                        while(i+a < 7 && j+a < 7) {
                            a++;
                            if(board[i+a][j+a] < 0) {
                                score = score + move;
                                break;
                            }
                            else if(board[i+a][j+a] == 0) {
                                score = score + move;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i > a && j +a < 7) {
                            a++;
                            if(board[i-a][j+a] < 0) {
                                score = score + move;
                                break;
                            }
                            else if(board[i-a][j+a] == 0) {
                                score = score + move;
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else if(x == 5) {
                        if(j < 6 && i != 0) {
                            score = score + move*1.5;
                        }
                        if(j < 6 && i != 7) {                        
                            score = score + move*1.5;
                        }
                        if(j > 1 && i != 0) {
                            score = score + move*1.5;
                        }
                        if(j > 1 && i != 7) {
                            score = score + move*1.5;
                        }
                        if(j != 7 && i > 1) {
                            score = score + move*1.5;
                        }
                        if(j != 7 && i < 6) {
                            score = score + move*1.5;
                        }
                        if(j != 0 && i > 1) {
                            score = score + move*1.5;
                        }
                        if(j != 0 && i < 6) {
                            score = score + move*1.5;
                        }
                    }
                    else { //pawn
                        score = score + openingPawnScore[6-i];
                        if(board[i-1][j] == 0) {
                            score = score + move;
                        }
                        if(i == 6) {
                            if(board[5][j] == 0 && board[4][j] == 0) {
                                score = score + move;
                            }
                        }
                        if(j!= 0 && board[i-1][j-1] < 0) {
                            score = score + move;
                        }
                        if(j != 7 && board[i-1][j+1] < 0) {
                            score = score + move;
                        }
                    }
                }
            }
            return score;
        }
        else { //endgame
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board[i][j] == -6) {
                        score = score - 3*pawnScore[i-1];
                    }
                    else if(board[i][j] == 6) {
                        score = score + 3*pawnScore[6-i];
                    }
                }
            }
            return score;
        }
    }
    
    public static ArrayList<Integer> giveMoves(ArrayList<Integer> moves, int[][] board, int player) {
        for(int i = 0; i <  8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] * player <= 0) {
                    continue;
                }
                int x = board[i][j];
                //have each piece make their move, dostuff, then undo the move
                if(x == 1) {
                    if(j != 7) {
                        moves.add(i);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(i != 7) {
                        moves.add(i+1);
                        moves.add(j);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 0) {
                        moves.add(i);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(i != 0) {
                        moves.add(i-1);
                        moves.add(j);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 7 && i != 0) {
                        moves.add(i-1);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 7 && i != 7) {
                        moves.add(i+1);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 0 && i != 0) {
                        moves.add(i-1);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 0 && i != 7) {
                        moves.add(i+1);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                }
                else if(x == 2) { //queen
                    int a = 0; 
                    while(i > a) {
                        a++;
                        if(board[i-a][j] != 0) {
                            moves.add(i-a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i + a < 7) {
                        a++;
                        if(board[i+a][j] != 0) {
                            moves.add(i+a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i+a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(j > a) {
                        a++;
                        if(board[i][j-a] != 0) {
                            moves.add(i);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(j + a < 7) {
                        a++;
                        if(board[i][j+a] != 0) {
                            moves.add(i);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0; 
                    while(i > a && j > a) {
                        a++;
                        if(board[i-a][j-a] != 0) {
                            moves.add(i-a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i + a < 7 && j > a) {
                        a++;
                        if(board[i+a][j-a] != 0) {
                            moves.add(i+a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i+a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0; 
                    while(i+a < 7 && j+a < 7) {
                        a++;
                        if(board[i+a][j+a] != 0) {
                            moves.add(i+a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else  {
                            moves.add(i+a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i > a && j +a < 7) {
                        a++;
                        if(board[i-a][j+a] != 0) {
                            moves.add(i-a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                }
                else if(x == 3) { //rook
                    int a = 0; 
                    while(i > a) {
                        a++;
                        if(board[i-a][j] != 0) {
                            moves.add(i-a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i + a < 7) {
                        a++;
                        if(board[i+a][j] != 0) {
                            moves.add(i+a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i+a);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(j > a) {
                        a++;
                        if(board[i][j-a] != 0) {
                            moves.add(i);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(j + a < 7) {
                        a++;
                        if(board[i][j+a] != 0) {
                            moves.add(i);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                }
                else if(x == 4) { //bishop
                    int a = 0; 
                    while(i > a && j > a) {
                        a++;
                        if(board[i-a][j-a] != 0) {
                            moves.add(i-a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i + a < 7 && j > a) {
                        a++;
                        if(board[i+a][j-a] != 0) {
                            moves.add(i+a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i+a);
                            moves.add(j-a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0; 
                    while(i+a < 7 && j+a < 7) {
                        a++;
                        if(board[i+a][j+a] != 0) {
                            moves.add(i+a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i+a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    a = 0;
                    while(i > a && j +a < 7) {
                        a++;
                        if(board[i-a][j+a] != 0) {
                            moves.add(i-a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                            break;
                        }
                        else {
                            moves.add(i-a);
                            moves.add(j+a);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                }
                else if(x == 5) {
                    if(j < 6 && i != 0) {
                        moves.add(i-1);
                        moves.add(j+2);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j < 6 && i != 7) {                        
                        moves.add(i+1);
                        moves.add(j+2);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j > 1 && i != 0) {
                        moves.add(i-1);
                        moves.add(j-2);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j > 1 && i != 7) {
                        moves.add(i+1);
                        moves.add(j-2);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 7 && i > 1) {
                        moves.add(i-2);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 7 && i < 6) {
                        moves.add(i+2);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 0 && i > 1) {
                        moves.add(i-2);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 0 && i < 6) {
                        moves.add(i+2);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                }
                else { //pawn
                    if(player == 1) {
                        if(board[i-1][j] == 0) {
                        moves.add(i-1);
                        moves.add(j);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(i == 6) {
                        if(board[5][j] == 0 && board[4][j] == 0) {
                            moves.add(4);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                    }
                    if(j!= 0 && board[i-1][j-1] < 0) {
                        moves.add(i-1);
                        moves.add(j-1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    if(j != 7 && board[i-1][j+1] < 0) {
                        moves.add(i-1);
                        moves.add(j+1);
                        moves.add(i);
                        moves.add(j);
                        moves.add(x);
                    }
                    }
                    else {
                        if(board[i+1][j] == 0) {
                            moves.add(i+1);
                            moves.add(j);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                        if(i == 1) {
                            if(board[2][j] == 0 && board[3][j] == 0) {
                                moves.add(3);
                                moves.add(j);
                                moves.add(i);
                                moves.add(j);
                                moves.add(x);
                            }
                        }
                        if(j!= 0 && board[i+1][j-1] > 0) {
                            moves.add(i+1);
                            moves.add(j-1);
                            moves.add(i);
                            moves.add(j);
                            moves.add(x);
                        }
                        if(j != 7 && board[i+1][j+1] > 0) {
                            moves.add(i+1);
                            moves.add(j+1);
                            moves.add(i);
                            moves.add(j); 
                            moves.add(x);
                        }
                    }
                }
            }
        }
        return moves;
    }

    public static void printBoard(int[][] board) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                int x = board[i][j];
                String y = "";
                if(x == 0) {
                    y = "-----";
                }
                else if(x < 0) {
                    y = y + "b";
                }
                else {
                    y = y + "w";
                }
                if(Math.abs(x) == 1) {
                    y = y + "king";
                }
                else if(Math.abs(x) == 2) {
                    y = y + "quen";
                }
                else if(Math.abs(x) == 3) {
                    y = y + "rook";
                }
                else if(Math.abs(x) == 4) {
                    y = y + "bshp";
                }
                else if(Math.abs(x) == 5) {
                    y = y + "kngt";
                }
                else if(Math.abs(x) == 6) {
                    y = y + "pawn";
                }
                System.out.print(y + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean move(int[][] board, int startrow, int startcolumn, int endrow, int endcolumn) {
        //move a piece to the given location on the board, return whether the move is legal
        if (board[endrow][endcolumn] * board[startrow][startcolumn] > 0){
            return false;
        }
        else {
            if(board[startrow][startcolumn] == 6 && endrow == 0) {
                board[endrow][endcolumn] = 2;
            }
            else if(board[startrow][startcolumn] == -6 && endrow == 7) {
                board[endrow][endcolumn] = -2;
            }
            else {
                board[endrow][endcolumn] = board[startrow][startcolumn];
            }
            board[startrow][startcolumn] = 0;
            return true;
        }
    }

    public static int[][] duplicate(int[][] board) {
        int[][] result = new int[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }
}
