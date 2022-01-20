/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

import Engine.EngineBoard;
import Engine.MoveList;
import Engine.MovGen.MoveTables.StaticMoves;

/**
 *
 * @author tylerbreese
 */
public class KingMoveGen {

    public static void addKingMoves(final MoveList moveList, final EngineBoard board) {
        final int fromIndex = board.kingIndex[board.colorsTurn];
        long moves = StaticMoves.KING_MOVES[fromIndex] & board.emptySpaces;
        while (moves != 0) {
            moveList.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), board.KING));
            moves &= moves - 1;
        }

        if (board.checkingPieces == 0) {
            long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
            while (castlingIndexes != 0) {
                final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                // no piece in between?
                if (CastlingUtil.isValidCastlingMove(board, fromIndex, castlingIndex)) {
                    moveList.reserved_add(MoveUtil.createCastlingMove(fromIndex, castlingIndex));
                }
                castlingIndexes &= castlingIndexes - 1;
            }
        }
    }
}
