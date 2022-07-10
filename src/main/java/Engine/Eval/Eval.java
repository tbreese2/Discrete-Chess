/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Eval;

import static Engine.EngineValues.BISHOP;
import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.EMPTY;
import static Engine.EngineValues.KING;
import static Engine.EngineValues.KNIGHT;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.WHITE;
import Engine.MoveGen.Bitboard;

import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.MoveGen.MoveUtil;

//main eval function for engine
public class Eval {
    //
    private final static int[] pieceValues = {100, 315, 325, 515, 1000};
    
    //EFFECTS: given chess board
    //returns the engines evaluation of the board
    public static int boardEval(final ChessBoard board) {
        //todo:
        //passed pawns, mobility and king safety
        return Material(board);
    }
    
    //EFFECTS: helper function
    //returns piece count evaltion of the board
    private static int Material(final ChessBoard board) {
       int materialCount = ((Long.bitCount(board.pieces[WHITE][PAWN]) * pieceValues[PAWN - 1])
                        + (Long.bitCount(board.pieces[WHITE][KNIGHT]) * pieceValues[KNIGHT - 1])
                        + (Long.bitCount(board.pieces[WHITE][BISHOP]) * pieceValues[BISHOP - 1])
                        + (Long.bitCount(board.pieces[WHITE][ROOK]) * pieceValues[ROOK - 1])
                        + (Long.bitCount(board.pieces[WHITE][QUEEN]) * pieceValues[QUEEN - 1])) -
                
                ((Long.bitCount(board.pieces[BLACK][PAWN]) * pieceValues[PAWN - 1])
                        + (Long.bitCount(board.pieces[BLACK][KNIGHT]) * pieceValues[KNIGHT - 1])
                        + (Long.bitCount(board.pieces[BLACK][BISHOP]) * pieceValues[BISHOP - 1])
                        + (Long.bitCount(board.pieces[BLACK][ROOK]) * pieceValues[ROOK - 1])
                        + (Long.bitCount(board.pieces[BLACK][QUEEN]) * pieceValues[QUEEN - 1]));
       
       //check for bishop pair black
       if (Long.bitCount(board.pieces[BLACK][BISHOP]) == 2) {
           materialCount -= 50;
           //if white has no minor pieces, add 25 extra
           if(Long.bitCount(board.pieces[WHITE][BISHOP]) == 0 && Long.bitCount(board.pieces[WHITE][KNIGHT]) == 0) {
               materialCount -= 25;
           }
       }
       
       //check for white bishop pair
       if (Long.bitCount(board.pieces[WHITE][BISHOP]) == 2) {
           materialCount += 50;
           //if black has no minor pieces, add 25 extra
           if(Long.bitCount(board.pieces[BLACK][BISHOP]) == 0 && Long.bitCount(board.pieces[BLACK][KNIGHT]) == 0) {
               materialCount += 25;
           }
       }

       return materialCount;
    }
    
    //TODO: finish black mobility calculations
    //then integrat into overal mobility calculation
    //EFFECTS: return mobility factor for position
    private static int Mobility(final ChessBoard board) {
        int mobilityScore = 0;
        final long pinnedWhite;
        final long nonPinnedWhite;
        final long pinnedBlack;
        final long nonPinnedBlack;
        
        //set pinned peices
        if(board.colorToMove == WHITE) {
            pinnedWhite = board.pinnedPieces;
            nonPinnedWhite = ~pinnedWhite;
            board.changeSideToMove();
            board.setPinnedAndDiscoPieces();
            pinnedBlack = board.pinnedPieces;
            nonPinnedBlack = ~pinnedBlack;
            board.changeSideToMove();
            board.setPinnedAndDiscoPieces();
        } else {
            pinnedBlack = board.pinnedPieces;
            nonPinnedBlack = ~pinnedBlack;
            board.changeSideToMove();
            board.setPinnedAndDiscoPieces();
            pinnedWhite = board.pinnedPieces;
            nonPinnedWhite = ~pinnedWhite;
            board.changeSideToMove();
            board.setPinnedAndDiscoPieces();
        }
        
        //calculate mobility of each piece type
        
        //white pawn moves
         long piece = board.pieces[WHITE][PAWN] & (board.emptySpaces  >>> 8) & Bitboard.RANK_23456;
         while (piece != 0) {
            mobilityScore++;
            piece &= piece - 1;
        }

        piece = board.pieces[WHITE][PAWN] & (board.emptySpaces >>> 16) & Bitboard.RANK_2;
        while (piece != 0) {
            if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                mobilityScore++;
            }
            piece &= piece - 1;
        }
        
        //black pawn moves
         piece = board.pieces[BLACK][PAWN] & (board.emptySpaces  >>> 8) & Bitboard.RANK_23456;
         while (piece != 0) {
            mobilityScore--;
            piece &= piece - 1;
        }

        piece = board.pieces[BLACK][PAWN] & (board.emptySpaces >>> 16) & Bitboard.RANK_2;
        while (piece != 0) {
            if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                mobilityScore--;
            }
            piece &= piece - 1;
        }
        
        //bishop moves
        
        //then mobility of black pieces
        return 0;
    }
    

   
}
