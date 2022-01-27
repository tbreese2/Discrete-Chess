/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.BISHOP;
import Engine.SearchTree;

/**
 *
 * @author tylerbreese
 */
public class BishopMoveGen {
    
    public static void addBishopCaptures(final SearchTree tree, long bishops, final ChessBoard board, final long emptySpaces) {
        while (bishops != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(bishops);
            long moves = MagicUtil.getBishopMoves(fromIndex, board.allPieces) & emptySpaces;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addMove(MoveUtil.createCaptureMove(fromIndex, toIndex, BISHOP, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
            bishops &= bishops - 1;
        }
    }
    
    public static void addBishopMoves(final SearchTree tree, long bishops, final long allPieces, final long emptySpaces) {
        while (bishops != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(bishops);
            long moves = MagicUtil.getBishopMoves(fromIndex, allPieces) & emptySpaces;
            while (moves != 0) {
                tree.addMove(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), BISHOP));
                moves &= moves - 1;
            }

            bishops &= bishops - 1;
        }
    }
}
