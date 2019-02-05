import java.util.Arrays;
import java.util.ArrayList;
/**
 * 1/28/19: First working version without en passant, castling, or mate detection. Defeated Ivan's chess AI easy.
 * 1/29/19: Gained +15 advantage against chess.com 800 rating computer. Loses to me, brother, dad. Added alpha beta pruning-7 deep in opening.
 * 1/30/19: Defeated 1250 chess.com AI, 5 blunders and 4 mistakes in 90 moves.
 * 1/31/19: Capturing heuristics: runs 8 deep, implmemented stalemate/checkmate, defeated Ivan's chess AI medium. 6 inaccuracies in 62 moves.
 * 2/1/19: Defeated chess.com 1550 AI, 2 blunders, 4 mistakes in 68 moves. Implemented castling
 * @author Jason Hu
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
                {-3,0,0,0,-1,0,0,-3},
                {-6,-6,-6,-6,-6,-6,-6,-6},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {6,6,6,6,6,6,6,6},
                {3,0,0,0,1,0,0,3},
            };
        //board = starting;
        int gameState = 1; //0=opening, 1=midgame, 2=endgame
        printBoard(board);
        String firstMove = "";
        for(int i = 3; i < 7; i++) {
            firstMove = testAI(board, i, gameState, -10000000, 10000000, firstMove);
            System.out.println(GameVersion.toNotation(firstMove));
            System.out.println(tester.count);
        }
    }

    public static String testAI(int[][] board, int depth, int gameState, double alpha, double beta, String firstMove) {
        String bestMove = "";
        double bestValue = -1000000;
        ArrayList <int[]> moves = new ArrayList<int[]>();
        if (!firstMove.equals("")) {
            int a = (Integer.parseInt(firstMove.substring(7,8)));
            int b = (Integer.parseInt(firstMove.substring(9,10)));
            int c = (Integer.parseInt(firstMove.substring(0,1)));
            int d = (Integer.parseInt(firstMove.substring(2,3)));
            int e = (Integer.parseInt(firstMove.substring(11,12)));
            int[] bruh = {a,b,c,d,e};
            moves.add(bruh);
        }
        moves = giveMoves(moves, board, 1);
        moves.add(new int[] {8,8,8,8,8});
        moves.add(new int[] {9,9,9,9,9});
        for(int k = 0; k < moves.size()-2; k++) {
            int movetorow = moves.get(k)[0];
            int movetocolumn = moves.get(k)[1];
            int i = moves.get(k)[2];
            int j = moves.get(k)[3];
            int x = moves.get(k)[4];
            int y = board[movetorow][movetocolumn];
            if(move(board, i, j, movetorow, movetocolumn)) {
                if(inCheck(board, 1)) {
                    board[i][j] = x;
                    board[movetorow][movetocolumn] = y;
                    continue;
                }
                double v = minimax(board, depth-1, false, gameState, alpha, beta);
                if(bestValue < v) {
                    bestValue = v;
                    bestMove = i + " " + j + " to " + movetorow + " " + movetocolumn + " " + x;
                }
                alpha = Math.max(alpha, bestValue);
                board[i][j] = x; //undo the move
                board[movetorow][movetocolumn] = y;
                if(alpha >= beta) {
                    break;
                }
            }
        }
        for(int i = moves.size()-2; i < moves.size(); i++) {
            int a = moves.get(i)[0];
            if(move(board, a, a, a, a)) {
                if(inCheck(board, 1)) {
                    board[7][4] = 1;
                    if(a == 8) {
                        board[7][5] = 0;
                        board[7][6] = 0;
                        board[7][7] = 3;
                    }
                    else {
                        board[7][3] = 0;
                        board[7][2] = 0;
                        board[7][1] = 0;
                        board[7][0] = 3;
                    }
                    continue;
                }
                double v = minimax(board, depth-1, false, gameState, alpha, beta);
                if(bestValue < v) {
                    bestValue = v;
                    bestMove = "castled";
                }
                alpha = Math.max(alpha, bestValue);
                board[7][4] = 1;
                if(a == 8) {
                    board[7][5] = 0;
                    board[7][6] = 0;
                    board[7][7] = 3;
                }
                else {
                    board[7][3] = 0;
                    board[7][2] = 0;
                    board[7][1] = 0;
                    board[7][0] = 3;
                }
                if(alpha >= beta) {
                    break;
                }
            }
        }
        if(bestValue == -1000000) {
            if(inCheck(board, 1)) {
                return "Checkmate by Black";
            }
            else {
                return "Stalemate";
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
            ArrayList<int[]> moves = new ArrayList<int[]>();
            moves = giveMoves(moves, board, 1);
            moves.add(new int[] {8,8,8,8,8});
            moves.add(new int[] {9,9,9,9,9});
            for(int k = 0; k < moves.size()-2; k++) {
                int movetorow = moves.get(k)[0];
                int movetocolumn = moves.get(k)[1];
                int i = moves.get(k)[2];
                int j = moves.get(k)[3];
                int x = moves.get(k)[4];
                int y = board[movetorow][movetocolumn];
                if(move(board, i, j, movetorow, movetocolumn)) {
                    if(inCheck(board, 1)) {
                        board[i][j] = x;
                        board[movetorow][movetocolumn] = y;
                        continue;
                    }
                    double v = minimax(board, depth-1, false, gameState, alpha, beta);
                    bestValue = Math.max(bestValue, v);
                    alpha = Math.max(alpha, bestValue);
                    board[i][j] = x; //undo the move
                    board[movetorow][movetocolumn] = y;
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            for(int i = moves.size()-2; i < moves.size(); i++) {
                int a = moves.get(i)[0];
                if(move(board, a, a, a, a)) {
                    if(inCheck(board, 1)) {
                        board[7][4] = 1;
                        if(a == 8) {
                            board[7][5] = 0;
                            board[7][6] = 0;
                            board[7][7] = 3;
                        }
                        else {
                            board[7][3] = 0;
                            board[7][2] = 0;
                            board[7][1] = 0;
                            board[7][0] = 3;
                        }
                        continue;
                    }
                    double v = minimax(board, depth-1, false, gameState, alpha, beta);
                    bestValue = Math.max(v, bestValue);
                    alpha = Math.max(alpha, bestValue);
                    board[7][4] = 1;
                    if(a == 8) {
                        board[7][5] = 0;
                        board[7][6] = 0;
                        board[7][7] = 3;
                    }
                    else {
                        board[7][3] = 0;
                        board[7][2] = 0;
                        board[7][1] = 0;
                        board[7][0] = 3;
                    }
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            if(bestValue == -10000000) {
                if(inCheck(board, 1)) {
                    return bestValue;
                }
                else {
                    return 0;
                }
            }
            return bestValue;
        }
        else {
            double bestValue = 10000000;
            String bestMove = "";
            ArrayList<int[]> moves = new ArrayList<int[]>();
            moves = giveMoves(moves, board, -1);
            moves.add(new int[] {10,10,10,10,10});
            moves.add(new int[] {11,11,11,11,11});
            for(int k = 0; k < moves.size()-2; k++) {
                int movetorow = moves.get(k)[0];
                int movetocolumn = moves.get(k)[1];
                int i = moves.get(k)[2];
                int j = moves.get(k)[3];
                int x = moves.get(k)[4];
                int y = board[movetorow][movetocolumn];
                if(move(board, i, j, movetorow, movetocolumn)) {
                    if(inCheck(board, -1)) {
                        board[i][j] = x;
                        board[movetorow][movetocolumn] = y;
                        continue;
                    }
                    double v = minimax(board, depth-1, true, gameState, alpha, beta);
                    bestValue = Math.min(bestValue, v);
                    beta = Math.min(beta, bestValue);
                    board[i][j] = x; //undo the move
                    board[movetorow][movetocolumn] = y;
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            for(int i = moves.size()-2; i < moves.size(); i++) {
                int a = moves.get(i)[0];
                if(move(board, a, a, a, a)) {
                    if(inCheck(board, -1)) {
                        board[0][4] = -1;
                        if(a == 10) {
                            board[0][5] = 0;
                            board[0][6] = 0;
                            board[0][7] = -3;
                        }
                        else {
                            board[0][3] = 0;
                            board[0][2] = 0;
                            board[0][1] = 0;
                            board[0][0] = -3;
                        }
                        continue;
                    }
                    double v = minimax(board, depth-1, true, gameState, alpha, beta);
                    bestValue = Math.min(bestValue, v);
                    beta = Math.min(alpha, bestValue);
                    board[0][4] = -1;
                    if(a == 10) {
                        board[0][5] = 0;
                        board[0][6] = 0;
                        board[0][7] = -3;
                    }
                    else {
                        board[0][3] = 0;
                        board[0][2] = 0;
                        board[0][1] = 0;
                        board[0][0] = -3;
                    }
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            if(bestValue == 10000000) {
                if(inCheck(board, -1)) {
                    return bestValue;
                }
                else {
                    return 0;
                }
            }
            return bestValue;
        }
    }

    public static double evalPosition(int[][] board, int state) {
        tester.count++;
        double loneKing = 0.01;
        double doublePawn = 0.005;
        double kingEdge = 0.002;
        double pastpawn = 0.04;
        double score = 0;
        double move = 0.002;
        double centerControl = 0.01;
        double kingSafety = 0.04;
        double bishopPair = 0.1;
        double inactivePiece = 0.02;
        int wkingX = 0;
        int wkingY = 0;
        int bkingX = 0;
        int bkingY = 0;
        int bbishops = 0;
        int wbishops = 0;
        double [] pawnScore = {0, 0, 0.002, 0.004, 0.016, 0.03};
        double [] openingPawnScore = {0, 0, 0.002, 0.004, 0.008, 0.016};
        if(state == 1) {
            inactivePiece = 0.04;
        }
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                switch(board[i][j]) {
                    case -1: bkingX = i; bkingY = j; break;
                    case 1: wkingX = i; wkingY = j; break;
                    case -2: score = score - 9;
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 2: score = score + 9; 
                    if(state < 2 && i == 7) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -3: score = score - 5;
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece;
                    }
                    break;
                    case 3: score = score + 5;
                    if(state < 2 && i == 7) {
                        score = score - inactivePiece;
                    }
                    break;
                    case -4: score = score - 3; bbishops++; 
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece*2;
                    }
                    break;
                    case 4: score = score + 3; wbishops++; 
                    if(state < 2 && i == 7) {
                        score = score - inactivePiece*2;
                    }
                    break;
                    case -5: score = score - 3; 
                    if(state < 2 && i == 0) {
                        score = score + inactivePiece*2;
                    }
                    break;
                    case 5: score = score + 3;
                    if(state < 2 && i == 7) {
                        score = score - inactivePiece*2;
                    }
                    break;
                    case -6: score = score - 1; break;
                    case 6: score = score + 1; break;
                }
            }
        }
        if(bbishops == 2) {
            score = score - bishopPair;
        }
        if(wbishops == 2) {
            score = score + bishopPair;
        }
        if(state == 0) {
            ArrayList<int[]> moves = new ArrayList<int[]>();
            giveMoves(moves, board, 1);
            for(int[] thing : moves) {
                if(thing[4] == 4) {
                    score = score + move;
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score + centerControl;
                    }
                }
                else if(thing[4] == 5) {
                    score = score + move*1.5;
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score + centerControl;
                    }
                }
                else if(thing[4] == 6) {
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score + centerControl;
                    }
                }
            }
            ArrayList<int[]> moves2 = new ArrayList<int[]>();
            giveMoves(moves, board, -1);
            for(int[] thing : moves2) {
                if(thing[4] == -4) {
                    score = score - move;
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score - centerControl;
                    }
                }
                else if(thing[4] == -5) {
                    score = score + move*1.5;
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score - centerControl;
                    }
                }
                else if(thing[4] == -6) {
                    if((thing[0] == 3 || thing[0] == 4) && (thing[1] == 3 || thing[1] == 4)) {
                        score = score - centerControl;
                    }
                }
            }
            return score;
        }
        else if(state == 1) {//midgame
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
            } //opening
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
                                score = score + move/5;
                                break;
                            }
                            else if(board[i-a][j] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7) {
                            a++;
                            if(board[i+a][j] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i+a][j] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j > a) {
                            a++;
                            if(board[i][j-a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i][j-a] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j + a < 7) {
                            a++;
                            if(board[i][j+a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i][j+a] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0; 
                        while(i > a && j > a) {
                            a++;
                            if(board[i-a][j-a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i-a][j-a] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7 && j > a) {
                            a++;
                            if(board[i+a][j-a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i+a][j-a] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0; 
                        while(i+a < 7 && j+a < 7) {
                            a++;
                            if(board[i+a][j+a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i+a][j+a] == 0) {
                                score = score + move/5;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i > a && j +a < 7) {
                            a++;
                            if(board[i-a][j+a] < 0) {
                                score = score + move/5;
                                break;
                            }
                            else if(board[i-a][j+a] == 0) {
                                score = score + move/5;
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
                                score = score + move/3;
                                break;
                            }
                            else if(board[i-a][j] == 0) {
                                score = score + move/3;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(i + a < 7) {
                            a++;
                            if(board[i+a][j] < 0) {
                                score = score + move/3;
                                break;
                            }
                            else if(board[i+a][j] == 0) {
                                score = score + move/3;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j > a) {
                            a++;
                            if(board[i][j-a] < 0) {
                                score = score + move/3;
                                break;
                            }
                            else if(board[i][j-a] == 0) {
                                score = score + move/3;
                            }
                            else {
                                break;
                            }
                        }
                        a = 0;
                        while(j + a < 7) {
                            a++;
                            if(board[i][j+a] < 0) {
                                score = score + move/3;
                                break;
                            }
                            else if(board[i][j+a] == 0) {
                                score = score + move*0.;
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
            int totalpawns = 0;
            for(int j = 0; j < 8; j++) {
                int wpawns = 0;
                int bpawns = 0;
                for(int i = 0; i < 8; i++) {
                    if(Math.abs(board[i][j]) == 1) {
                        score = score + board[i][j]*Math.min(i, 7-i)*kingEdge;
                        score = score + board[i][j]*Math.min(j, 7-j)*kingEdge;
                    }
                    if(board[i][j] == -6) {
                        score = score - 1.5*pawnScore[i-1];
                        bpawns++;
                        if(bpawns >= 2) {
                            score = score + doublePawn;
                        }
                        totalpawns++;
                    }
                    else if(board[i][j] == 6) {
                        score = score + 1.5*pawnScore[6-i];
                        wpawns++;
                        if(wpawns >= 2) {
                            score = score - doublePawn;
                        }
                        totalpawns++;
                    }
                }
                if(wpawns >= 1 && bpawns == 0) {
                    score = score + wpawns*pastpawn;
                }
                else if(bpawns >= 1 && wpawns == 0) {
                    score = score - bpawns*pastpawn;
                }
            }
            if(totalpawns == 0) {
                if(Math.abs(score) <= 3.5) {
                    return 0;
                }
                else if (score > 0) {
                    score = score - Math.min(bkingX, 7-bkingX)*kingEdge*3;
                    score = score - Math.min(bkingY, 7-bkingY)*kingEdge*3;
                }
            }
            return score;
        }
    }

    public static boolean inCheck(int[][] board, int player) {
        ArrayList<int[]> bruh = new ArrayList<int[]>();
        giveMoves(bruh, board, 0-player);
        for(int[] move : bruh) {
            if(board[move[0]][move[1]] == player) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<int[]> giveMoves(ArrayList<int[]> moves, int[][] board, int player) {
        for(int i = 0; i <  8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] * player <= 0) {
                    continue;
                }
                int x = board[i][j];
                //have each piece make their move, dostuff, then undo the move
                if(x == 1*player) {
                    if(j != 7) {
                        addMoves(moves, board, i, j+1, i, j, player);
                    }
                    if(i != 7) {
                        addMoves(moves, board, i+1, j, i, j, player);
                    }
                    if(j != 0) {
                        addMoves(moves, board, i, j-1, i, j, player);
                    }
                    if(i != 0) {
                        addMoves(moves, board, i-1, j, i, j, player);
                    }
                    if(j != 7 && i != 0) {
                        addMoves(moves, board, i-1, j+1, i, j, player);
                    }
                    if(j != 7 && i != 7) {
                        addMoves(moves, board, i+1, j+1, i, j, player);
                    }
                    if(j != 0 && i != 0) {
                        addMoves(moves, board, i-1, j-1, i, j, player);
                    }
                    if(j != 0 && i != 7) {
                        addMoves(moves, board, i+1, j-1, i, j, player);
                    }
                }
                else if(x == 2*player) { //queen
                    int a = 0; 
                    while(i > a) {
                        a++;
                        addMoves(moves, board, i-a, j, i, j, player);
                        if(board[i-a][j] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i + a < 7) {
                        a++;
                        addMoves(moves, board, i+a, j, i, j, player);
                        if(board[i+a][j] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(j > a) {
                        a++;
                        addMoves(moves, board, i, j-a, i, j, player);
                        if(board[i][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(j + a < 7) {
                        a++;
                        addMoves(moves, board, i, j+a, i, j, player);
                        if(board[i][j+a] != 0) {
                            break;
                        }
                    }
                    a = 0; 
                    while(i > a && j > a) {
                        a++;
                        addMoves(moves, board, i-a, j-a, i, j, player);
                        if(board[i-a][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i + a < 7 && j > a) {
                        a++;
                        addMoves(moves, board, i+a, j-a, i, j, player);
                        if(board[i+a][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0; 
                    while(i+a < 7 && j+a < 7) {
                        a++;
                        addMoves(moves, board, i+a, j+a, i, j, player);
                        if(board[i+a][j+a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i > a && j +a < 7) {
                        a++;
                        addMoves(moves, board, i-a, j+a, i, j, player);
                        if(board[i-a][j+a] != 0) {
                            break;
                        }
                    }
                }
                else if(x == 3*player) { //rook
                    int a = 0; 
                    while(i > a) {
                        a++;
                        addMoves(moves, board, i-a, j, i, j, player);
                        if(board[i-a][j] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i + a < 7) {
                        a++;
                        addMoves(moves, board, i+a, j, i, j, player);
                        if(board[i+a][j] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(j > a) {
                        a++;
                        addMoves(moves, board, i, j-a, i, j, player);
                        if(board[i][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(j + a < 7) {
                        a++;
                        addMoves(moves, board, i, j+a, i, j, player);
                        if(board[i][j+a] != 0) {
                            break;
                        }
                    }
                }
                else if(x == 4*player) { //bishop
                    int a = 0; 
                    while(i > a && j > a) {
                        a++;
                        addMoves(moves, board, i-a, j-a, i, j, player);
                        if(board[i-a][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i + a < 7 && j > a) {
                        a++;
                        addMoves(moves, board, i+a, j-a, i, j, player);
                        if(board[i+a][j-a] != 0) {
                            break;
                        }
                    }
                    a = 0; 
                    while(i+a < 7 && j+a < 7) {
                        a++;
                        addMoves(moves, board, i+a, j+a, i, j, player);
                        if(board[i+a][j+a] != 0) {
                            break;
                        }
                    }
                    a = 0;
                    while(i > a && j +a < 7) {
                        a++;
                        addMoves(moves, board, i-a, j+a, i, j, player);
                        if(board[i-a][j+a] != 0) {
                            break;
                        }
                    }
                }
                else if(x == 5*player) {
                    if(j < 6 && i != 0) {
                        addMoves(moves, board, i-1, j+2, i, j, player);
                    }
                    if(j < 6 && i != 7) {  
                        addMoves(moves, board, i+1, j+2, i, j, player);
                    }
                    if(j > 1 && i != 0) {
                        addMoves(moves, board, i-1, j-2, i, j, player);
                    }
                    if(j > 1 && i != 7) {
                        addMoves(moves, board, i+1, j-2, i, j, player);
                    }
                    if(j != 7 && i > 1) {
                        addMoves(moves, board, i-2, j+1, i, j, player);
                    }
                    if(j != 7 && i < 6) {
                        addMoves(moves, board, i+2, j+1, i, j, player);
                    }
                    if(j != 0 && i > 1) {
                        addMoves(moves, board, i-2, j-1, i, j, player);
                    }
                    if(j != 0 && i < 6) {
                        addMoves(moves, board, i+2, j-1, i, j, player);
                    }
                }
                else if(x == 6*player) { //pawn
                    if(player == 1) {
                        if(board[i-1][j] == 0) {
                            addMoves(moves, board, i-1, j, i, j, player);
                        }
                        if(i == 6) {
                            if(board[5][j] == 0 && board[4][j] == 0) {
                                addMoves(moves, board, 4, j, i, j, player);
                            }
                        }
                        if(j!= 0 && board[i-1][j-1] < 0) {
                            addMoves(moves, board, i-1, j-1, i, j, player);
                        }
                        if(j != 7 && board[i-1][j+1] < 0) {
                            addMoves(moves, board, i-1, j+1, i, j, player);
                        }
                    }
                    else {
                        if(board[i+1][j] == 0) {
                            addMoves(moves, board, i+1, j, i, j, player);
                        }
                        if(i == 1) {
                            if(board[2][j] == 0 && board[3][j] == 0) {
                                addMoves(moves, board, 3, j, i, j, player);
                            }
                        }
                        if(j!= 0 && board[i+1][j-1] > 0) {
                            addMoves(moves, board, i+1, j-1, i, j, player);
                        }
                        if(j != 7 && board[i+1][j+1] > 0) {
                            addMoves(moves, board, i+1, j+1, i, j, player);
                        }
                    }
                }
            }
        }
        return moves;
    }

    public static void addMoves(ArrayList<int[]> moves, int[][] board, int endrow, int endcolumn, int startrow, int startcolumn, int player) {
        int[] thing = {endrow, endcolumn, startrow, startcolumn, board[startrow][startcolumn]};
        if(board[endrow][endcolumn] * player < 0) {
            if(moves.size() >= 1) {
                moves.add(1, thing); //capturing a piece: put it in the front of the moves list
            }
            else {
                moves.add(thing);
            }
        }
        else {
            moves.add(thing);
        }
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
        if(startrow == 8) {
            if(board[7][4] != 1 || board[7][5] != 0 || board[7][6] != 0 || board[7][7] != 3) {
                return false;
            }
            board[7][4] = 0;
            board[7][5] = 3;
            board[7][6] = 1;
            board[7][7] = 0;
            return true;
        }
        else if(startrow == 9) {
            if(board[7][4] != 1 || board[7][3] != 0 || board[7][2] != 0 || board[7][1] != 0 || board[7][0] != 3) {
                return false;
            }
            board[7][4] = 0;
            board[7][3] = 3;
            board[7][2] = 1;
            board[7][1] = 0;
            board[7][0] = 0;
            return true;
        }
        else if(startrow == 10) {
            if(board[0][4] != -1 || board[0][5] != 0 || board[0][6] != 0 || board[0][7] != -3) {
                return false;
            }
            board[0][4] = 0;
            board[0][5] = -3;
            board[0][6] = -1;
            board[0][7] = 0;
            return true;
        }
        else if(startrow == 11) {
            if(board[0][4] != -1 || board[0][3] != 0 || board[0][2] != 0 || board[0][1] != 0 || board[0][0] != -3) {
                return false;
            }
            board[0][4] = 0;
            board[0][3] = -3;
            board[0][2] = -1;
            board[0][1] = 0;
            board[0][0] = 0;
            return true;
        }
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
