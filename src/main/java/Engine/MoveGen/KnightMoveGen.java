/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.MoveGen.ChessConstants.NIGHT;
import Engine.SearchTree;

/**
 *
 * 
 */
public class KnightMoveGen {
    public static void addNightCaptures(final SearchTree tree, long knights, final int[] indexes, final long emptySpaces) {
        while (knights != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(knights);
            long moves = StaticMoves.KNIGHT_MOVES[fromIndex] & emptySpaces;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addMove(MoveUtil.createAttackMove(fromIndex, toIndex, NIGHT, indexes[toIndex]));
                moves &= moves - 1;
            }
            knights &= knights - 1;
        }
    }
    
    public static void addNightMoves(final SearchTree tree, long piece, final long emptySpaces) {
        while (piece != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(piece);
            long moves = StaticMoves.KNIGHT_MOVES[fromIndex] & emptySpaces;
            while (moves != 0) {
                tree.addMove(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), NIGHT));
                moves &= moves - 1;
            }
            piece &= piece - 1;
        }
    }
}
