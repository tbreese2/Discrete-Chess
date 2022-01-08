/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine;

/**
 *
 * @author tylerbreese
 */
public class EngineBoard {
    
    //basic bitboards
    private long WhitePawns;
    private long WhiteBishops;
    private long WhiteRooks;
    private long WhiteQueens;
    private long WhiteKing;
    
    private long BlackPawns;
    private long BlackBishops;
    private long BlackRooks;
    private long BlackQueens;
    private long BlackKind;
    
    //combined bitboards
    private long WhitePieces;
    private long BlackPieces;
    
    //board information vars
    private boolean whiteToMove;
    
    //EFFECTS: creates a new engine board without peices
    //NOTE: the engineboard class is designed to use bitboard
    //which are much faster for computations
    public EngineBoard() {
        //initialize all bitboards to be empty
        WhitePawns = 0L;
        WhiteBishops = 0L;
        WhiteRooks = 0L;
        WhiteQueens = 0L;
        WhiteKing = 0L;
    
        BlackPawns = 0L;
        BlackBishops = 0L;
        BlackRooks = 0L;
        BlackQueens = 0L;
        BlackKind = 0L;
    
        //combined bitboards
        WhitePieces = 0L;
        BlackPieces = 0L;
    }
    
    //REQUIRES: input string must be in FEN postion format,
    //see https://www.chess.com/terms/fen-chess
    //for more details
    //MODIFES: EngineBoard
    //EFFECTS: sets engine board according to inputed string
    public void setBoard(String FEN, boolean whiteToPlay) {
        
       //first convert FEN to array
       String[][] tempBoard = new String[8][8];
       int r = 7;
       int c = 0;
       for (int i = 0; i < FEN.length(); i++) {
            Boolean flag = Character.isDigit(FEN.charAt(i));
            if(flag) {
                for(int b = 0; b < Character.getNumericValue(FEN.charAt(i)); b++){
                    tempBoard[r][c] = "";
                    c++;
                }
            } else if(FEN.charAt(i) == '/') {
                r--;
                c = 0;
            } else {
                tempBoard[r][c] = String.valueOf(FEN.charAt(i));
                c++;
            } 
        }
       
       //now use array to set every single bitboard
       
    }
    
}
