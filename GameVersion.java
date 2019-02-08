import java.util.Scanner;
/**
 * The game version of the chess engine.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameVersion {
    public static void main(String args[]) {
        int[][] board = new int[][] {
                {-3,-5,-4,-2,-1,-4,-5,-3},
                {-6,-6,-6,-6,-6,-6,-6,-6},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {6,6,6,6,6,6,6,6},
                {3,5,4,2,1,4,5,3},
            };
        int move = 1;
        int gameState = 0;
        int depth = 0;
        chess.printBoard(board);
        Scanner keyboard = new Scanner(System.in);
        tester.count = 0;
        while(true) {
            int anyPiece = 0;
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    if(board[i][j] != 0) {
                        anyPiece++;
                    }
                }
            }
            if(anyPiece > 15) {
                depth = 7;
            }
            else if(anyPiece > 10) {
                depth = 8;
            }
            else if(anyPiece > 6) {
                depth = 8;
            }
            else if(anyPiece > 4) {
                depth = 8;
            }
            else if(anyPiece == 4) {
                depth = 9;
            }
            else if(anyPiece == 3) {
                depth = 9;
            }
            if(move <= 15) {
                gameState = 0;
            }
            else {
                int pieces = 0;
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 8; j++) {
                        if(Math.abs(board[i][j]) >= 2 && Math.abs(board[i][j]) < 6) {
                            pieces++;
                        }
                    }
                }
                if(pieces > 4) {
                    gameState = 1;
                }
                else {
                    gameState = 2;
                }
            }
            String compMove = "";
            if(move == 1) {
                compMove = "6 4 to 4 4";
            }
            else {
                String firstMove = "";
                for(int i = 4; i < 20; i++) {
                    firstMove = chess.testAI(board, i, gameState, -10000000, 10000000, firstMove);
                    if(tester.count > 1000000) {
                        depth = i;
                        break;
                    }
                }
                compMove = firstMove;
            }
            System.out.println("Computer moved: " + toNotation(compMove));
            System.out.println("Depth = " + depth);
            int startrow = Integer.parseInt(compMove.substring(0,1));
            int startcol = Integer.parseInt(compMove.substring(2,3));
            int endrow = Integer.parseInt(compMove.substring(7,8));
            int endcol = Integer.parseInt(compMove.substring(9,10));
            chess.move(board, startrow, startcol, endrow, endcol);
            chess.printBoard(board);
            tester.count = 0;

            System.out.println("Enter your move: ");
            String playerMove2 = keyboard.nextLine();
            if(playerMove2.equals("castle kingside")) {
                chess.move(board, 0, 4, 0, 6);
                chess.move(board, 0, 7, 0, 5);
                chess.printBoard(board);
                continue;
            }
            else if(playerMove2.equals("castle queenside")) {
                chess.move(board,0,4,0,2);
                chess.move(board,0,0,0,3);
                chess.printBoard(board);
                continue;
            }
            String playerMove = fromNotation(playerMove2);
            startrow = Integer.parseInt(playerMove.substring(0,1));
            startcol = Integer.parseInt(playerMove.substring(2,3));
            endrow = Integer.parseInt(playerMove.substring(7,8));
            endcol = Integer.parseInt(playerMove.substring(9,10));
            chess.move(board, startrow, startcol, endrow, endcol);
            chess.printBoard(board);
            move++;
        }
    }

    public static String toNotation(String indices) {
        if(indices.equals("")) {
            return "Game Over";
        }
        if(indices.equals("castled")) {
            return "Castled";
        }
        int startrow = Integer.parseInt(indices.substring(0,1));
        int startcol = Integer.parseInt(indices.substring(2,3));
        int endrow = Integer.parseInt(indices.substring(7,8));
        int endcol = Integer.parseInt(indices.substring(9,10));
        String result = "";
        switch (startcol) {
            case 0: result = result + "a"; break;
            case 1: result = result + "b"; break;
            case 2: result = result + "c"; break;
            case 3: result = result + "d"; break;
            case 4: result = result + "e"; break;
            case 5: result = result + "f"; break; 
            case 6: result = result + "g"; break; 
            case 7: result = result + "h"; break; 
        }
        result = result + (8-startrow);
        result = result + " to ";
        switch (endcol) {
            case 0: result = result + "a"; break;
            case 1: result = result + "b"; break;
            case 2: result = result + "c"; break; 
            case 3: result = result + "d"; break;
            case 4: result = result + "e"; break;
            case 5: result = result + "f"; break;
            case 6: result = result + "g"; break;
            case 7: result = result + "h"; break;
        }
        result = result + (8-endrow);
        return result;
    }

    public static String fromNotation(String notation) {
        String result = "";
        char firstChar = notation.charAt(0);
        char secondChar = notation.charAt(6);
        int x = Integer.parseInt(notation.substring(1,2));
        int y = Integer.parseInt(notation.substring(7,8));
        result = result + (8-x);
        result = result + " ";
        switch (firstChar) {
            case 'a': result = result + "0"; break;
            case 'b': result = result + "1"; break;
            case 'c': result = result + "2"; break;
            case 'd': result = result + "3"; break;
            case 'e': result = result + "4"; break;
            case 'f': result = result + "5"; break;
            case 'g': result = result + "6"; break;
            case 'h': result = result + "7"; break;
        }
        result = result + " to ";
        result = result + (8-y);
        result = result + " ";
        switch (secondChar) {
            case 'a': result = result + "0"; break;
            case 'b': result = result + "1"; break;
            case 'c': result = result + "2"; break;
            case 'd': result = result + "3"; break;
            case 'e': result = result + "4"; break;
            case 'f': result = result + "5"; break;
            case 'g': result = result + "6"; break;
            case 'h': result = result + "7"; break;
        }
        return result;
    }
}
