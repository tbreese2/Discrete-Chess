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
    
    //trying new approach to storing boards, stored in this array instead
    public final long[][] pieces = new long[2][7];
    public final long[][] attacks = new long[2][7];
    
    //constants for accessing
    public static final int ALL = 0;
    public static final int EMPTY = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    
    
    //board information vars
    public int colorsTurn, colorsTurnInverse;
    
    //store checked and pinned pieces
    //checkedPieces are pieces that check the king from enemy pieces
    //pinnedPieces are pieces that cannot be moved by player
    //discovered pieces are pieces that if moved, a check will be created
    public long checkingPieces, pinnedPieces, discoveredPieces;
    
    //bitboards to store all pieces and empty spaces
    public long allPieces, emptySpaces;
    
    //EFFECTS: creates a new engine board without peices
    //NOTE: the engineboard class is designed to use bitboard
    //which are much faster for computations
    public EngineBoard() {
        //initialize all bitboards to be empty
        for(int r = 0; r < pieces.length; r++) {
            for(int c = 0; c < pieces[r].length; c++) {
                pieces[r][c] = 0l;
            }
        }
        checkingPieces = 0L;
        discoveredPieces = 0l;
        pinnedPieces = 0L;
    }
    
    //REQUIRES: input string must be in FEN postion format,
    //see https://www.chess.com/terms/fen-chess
    //for more details
    //MODIFES: EngineBoard
    //EFFECTS: sets engine board according to inputed string
    public void setBoard(String FEN, int colorToPlay) {
        
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
       for(r = 7; r >= 0; r--) {
           for(c = 0; c < 8; c++) {
                int i = r * 8 + c;
                long location = Bits.ALL_BITS[i];
                switch(tempBoard[r][c]) {
                    case "P": 
                        pieces[WHITE][PAWN] |= location;
                        break;
                    case "N": 
                        pieces[WHITE][KNIGHT] |= location;
                        break;
                    case "B": 
                        pieces[WHITE][BISHOP] |= location;
                        break;
                    case "R": 
                        pieces[WHITE][ROOK] |= location;
                        break;
                    case "Q": 
                        pieces[WHITE][QUEEN] |= location;
                        break;
                    case "K": 
                        pieces[WHITE][KING] |= location;
                        break;
                    case "p": 
                        pieces[BLACK][PAWN] |= location;
                        break;
                    case "n": 
                        pieces[BLACK][KNIGHT] |= location;
                        break;
                    case "b": 
                        pieces[BLACK][BISHOP] |= location;
                        break;
                    case "r": 
                        pieces[BLACK][ROOK] |= location;
                        break;
                    case "q": 
                        pieces[BLACK][QUEEN] |= location;
                        break;
                    case "k": 
                        pieces[BLACK][KING] |= location;
                        break;
               }
           }
       }
       
       //set boards based off 12 core bitboards
       pieces[BLACK][ALL] = pieces[BLACK][PAWN] | pieces[BLACK][KNIGHT] | pieces[BLACK][BISHOP]
            | pieces[BLACK][ROOK] | pieces[BLACK][QUEEN] | pieces[BLACK][KING];
       
       pieces[WHITE][ALL] = pieces[WHITE][PAWN] | pieces[WHITE][KNIGHT] | pieces[WHITE][BISHOP]
            | pieces[WHITE][ROOK] | pieces[WHITE][QUEEN] | pieces[WHITE][KING];
       
       allPieces = pieces[colorsTurn][ALL] | pieces[colorsTurn][ALL];
	emptySpaces = ~allPieces;
    }
    
    //EFFECTS: returns combined bitboard in matrix form
    public String toString() {
        long AllPieces = pieces[BLACK][ALL] | pieces[WHITE][ALL];
        return Bits.toBinaryStringMatrix(AllPieces);
    }
    
    
    //EFFECTS: returns combined bitboard in  matrix form
    public String toNonMatrixString() {
        long AllPieces = pieces[BLACK][ALL] | pieces[WHITE][ALL];
        return Bits.toBinaryString(AllPieces);
    }
     
    //MODIFIES: this
    //EFFECTS: updates checks, pins and discovered attacks
    public void setCheckingPinnedAndDiscoPieces() {
    
    }
}
