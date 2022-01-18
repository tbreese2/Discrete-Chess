/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

/**
 *
 * @author tylerbreese
 */
import Engine.*;
import Engine.MovGen.MoveTables.StaticMoves;


public class KnightMoveGen {
    
    //REQUIRES: pieces is bitboard of knights, emptySpaces i
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves, returns movelist filled with all valid knight moves
    public static void addKnightMoves(final MoveList moves, long pieces, final long emptySpaces){
        while (pieces != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(pieces);
            long pieceMoves = StaticMoves.KNIGHT_MOVES[fromIndex] & emptySpaces;
            while (pieceMoves != 0) {
                moves.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(pieceMoves), EngineBoard.KNIGHT));
                pieceMoves &= pieceMoves - 1;
            }
            pieces &= pieces - 1;
	}
    }
    
    
}
