/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.ROOK;
import Engine.SearchTree;

/**
 *
 * 
 */
public class RookMoveGen {
    
     public static void addRookCaptures(final SearchTree tree, long rooks, final ChessBoard board, final long emptySpaces) {
        while (rooks != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(rooks);
            long moves = MagicUtil.getRookMoves(fromIndex, board.allPieces) & emptySpaces;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addMove(MoveUtil.createCaptureMove(fromIndex, toIndex, ROOK, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
            rooks &= rooks - 1;
        }
    }
     
    public static void addRookMoves(final SearchTree tree, long rooks, final long allPieces, final long emptySpaces) {
        while (rooks != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(rooks);
            long moves = MagicUtil.getRookMoves(fromIndex, allPieces) & emptySpaces;
            while (moves != 0) {
                tree.addMove(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), ROOK));
                moves &= moves - 1;
            }
            rooks &= rooks - 1;
        }
    }
    
}
