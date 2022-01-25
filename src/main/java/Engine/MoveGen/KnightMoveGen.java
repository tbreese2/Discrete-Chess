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
    public static void addKnightCaptures(final SearchTree tree, long knights, final int[] indexes, final long emptySpaces) {
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
    
    public static void addKnightMoves(final SearchTree tree, long Knights, final long emptySpaces) {
        while (Knights != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(Knights);
            long moves = StaticMoves.KNIGHT_MOVES[fromIndex] & emptySpaces;
            while (moves != 0) {
                tree.addMove(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), NIGHT));
                moves &= moves - 1;
            }
            Knights &= Knights - 1;
        }
    }
}
