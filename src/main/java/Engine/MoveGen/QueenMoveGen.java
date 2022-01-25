/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.QUEEN;
import Engine.SearchTree;

/**
 *
 * @author tylerbreese
 */
public class QueenMoveGen {
    
    public static void addQueenCaptures(final SearchTree tree, long queens, final ChessBoard board, final long emptySpaces) {
        while (queens != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(queens);
            long moves = MagicUtil.getQueenMoves(fromIndex, board.allPieces) & emptySpaces;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addMove(MoveUtil.createAttackMove(fromIndex, toIndex, QUEEN, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
            queens &= queens - 1;
        }
    }
    
    public static void addQueenMoves(final SearchTree tree, long queens, final long allPieces, final long emptySpaces) {
        while (queens != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(queens);
            long moves = MagicUtil.getQueenMoves(fromIndex, allPieces) & emptySpaces;
            while (moves != 0) {
                tree.addMove(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), QUEEN));
                moves &= moves - 1;
            }

            queens &= queens - 1;
        }
    }
     
}
