/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.KING;
import Engine.SearchTree;

/**
 *
 * @author tylerbreese
 */
public class KingMoveGen {
    
    public static void addKingMoves(final SearchTree tree, final ChessBoard board) {
        final int fromIndex = board.kingIndex[board.colorToMove];
        long moves = StaticMoves.KING_MOVES[fromIndex] & board.emptySpaces;
        while (moves != 0) {
            tree.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), KING));
            moves &= moves - 1;
        }

        // castling
        if (board.checkingPieces == 0) {
            long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
            while (castlingIndexes != 0) {
                final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                // no piece in between?
                if (CastlingUtil.isValidCMove(board, fromIndex, castlingIndex)) {
                    tree.addNode(MoveUtil.createCastlingMove(fromIndex, castlingIndex));
                }
                castlingIndexes &= castlingIndexes - 1;
            }
        }
    }
    
    public static void addKingCastlingMoves(final SearchTree tree, final ChessBoard board) {
        final int fromIndex = board.kingIndex[board.colorToMove];
        long moves = StaticMoves.KING_MOVES[fromIndex] & board.emptySpaces;
        while (moves != 0) {
            moves &= moves - 1;
        }
        
         // castling
        if (board.checkingPieces == 0) {
            long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
            while (castlingIndexes != 0) {
                final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                // no piece in between?
                if (CastlingUtil.isValidCMove(board, fromIndex, castlingIndex)) {
                    tree.addNode(MoveUtil.createCastlingMove(fromIndex, castlingIndex));
                }
                castlingIndexes &= castlingIndexes - 1;
            }
        }
    }

    public static void addKingCaptures(final SearchTree tree, final ChessBoard board) {
        final int fromIndex = board.kingIndex[board.colorToMove];
        long moves = StaticMoves.KING_MOVES[fromIndex] & board.friendlyPieces[board.colorToMoveInverse] & ~board.discoveredPieces;
        while (moves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(moves);
            tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, KING, board.pieceIndexes[toIndex]));
            moves &= moves - 1;
        }
    }
    
}
