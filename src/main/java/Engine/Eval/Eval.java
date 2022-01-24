/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Eval;

import static Engine.EngineBoard.BISHOP;
import static Engine.EngineBoard.BLACK;
import static Engine.EngineBoard.EMPTY;
import static Engine.EngineBoard.KING;
import static Engine.EngineBoard.KNIGHT;
import static Engine.EngineBoard.PAWN;
import static Engine.EngineBoard.QUEEN;
import static Engine.EngineBoard.ROOK;
import static Engine.EngineBoard.WHITE;

import Engine.MoveGen.ChessBoard;

public class Eval {
    //eval constants
    //pawn, knight, bishop rook, queen, king
    public final static int[] pieceValues = {100, 325, 325, 500, 1050, 40000};
    
    //using basic eval right now
    public static int boardEval(ChessBoard board) {
        int materialCount = 0;
        
        materialCount = (Long.bitCount(board.pieces[WHITE][PAWN]) * pieceValues[PAWN - 1])
                        + (Long.bitCount(board.pieces[WHITE][KNIGHT]) * pieceValues[KNIGHT - 1])
                        + (Long.bitCount(board.pieces[WHITE][BISHOP]) * pieceValues[BISHOP - 1])
                        + (Long.bitCount(board.pieces[WHITE][ROOK]) * pieceValues[ROOK - 1])
                        + (Long.bitCount(board.pieces[WHITE][QUEEN]) * pieceValues[QUEEN - 1])
                        + (Long.bitCount(board.pieces[WHITE][KING]) * pieceValues[KING - 1]);
        
        materialCount -= (Long.bitCount(board.pieces[BLACK][PAWN]) * pieceValues[PAWN - 1])
                        + (Long.bitCount(board.pieces[BLACK][KNIGHT]) * pieceValues[KNIGHT - 1])
                        + (Long.bitCount(board.pieces[BLACK][BISHOP]) * pieceValues[BISHOP - 1])
                        + (Long.bitCount(board.pieces[BLACK][ROOK]) * pieceValues[ROOK - 1])
                        + (Long.bitCount(board.pieces[BLACK][QUEEN]) * pieceValues[QUEEN - 1])
                        + (Long.bitCount(board.pieces[BLACK][KING]) * pieceValues[KING - 1]);
        
        return materialCount;        
    }
}
