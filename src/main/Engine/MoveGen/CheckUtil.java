/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;


import static  Engine.EngineValues.BISHOP;
import static  Engine.EngineValues.BLACK;
import static  Engine.EngineValues.KING;
import static  Engine.EngineValues.KNIGHT;
import static  Engine.EngineValues.PAWN;
import static  Engine.EngineValues.QUEEN;
import static  Engine.EngineValues.ROOK;
import static  Engine.EngineValues.WHITE;

public final class CheckUtil {

    public static boolean isInCheck(final ChessBoard cb, int color) {
        final int kingIndex = cb.kingIndex[color];
        int colorInverse = 1 - color;
        return (cb.pieces[colorInverse][KNIGHT] & StaticMoves.KNIGHT_MOVES[kingIndex]
                | (cb.pieces[colorInverse][ROOK] | cb.pieces[colorInverse][QUEEN]) & MagicUtil.getRookMoves(kingIndex, cb.allPieces)
                | (cb.pieces[colorInverse][BISHOP] | cb.pieces[colorInverse][QUEEN]) & MagicUtil.getBishopMoves(kingIndex, cb.allPieces)
                | cb.pieces[colorInverse][PAWN] & StaticMoves.PAWN_ATTACKS[color][kingIndex]) != 0L;
    }

    public static long getCheckingPieces(final ChessBoard cb) {
        final int kingIndex = cb.kingIndex[cb.colorToMove];

        return (cb.pieces[cb.colorToMoveInverse][KNIGHT] & StaticMoves.KNIGHT_MOVES[kingIndex]
                | (cb.pieces[cb.colorToMoveInverse][ROOK] | cb.pieces[cb.colorToMoveInverse][QUEEN]) & MagicUtil.getRookMoves(kingIndex, cb.allPieces)
                | (cb.pieces[cb.colorToMoveInverse][BISHOP] | cb.pieces[cb.colorToMoveInverse][QUEEN]) & MagicUtil.getBishopMoves(kingIndex, cb.allPieces)
                | cb.pieces[cb.colorToMoveInverse][PAWN] & StaticMoves.PAWN_ATTACKS[cb.colorToMove][kingIndex]);
    }

    public static long getCheckingPieces(final ChessBoard cb, final int sourcePieceIndex) {
        switch (sourcePieceIndex) {
            case PAWN:
                return cb.pieces[cb.colorToMoveInverse][PAWN] & StaticMoves.PAWN_ATTACKS[cb.colorToMove][cb.kingIndex[cb.colorToMove]];
            case KNIGHT:
                return cb.pieces[cb.colorToMoveInverse][KNIGHT] & StaticMoves.KNIGHT_MOVES[cb.kingIndex[cb.colorToMove]];
            case BISHOP:
                return cb.pieces[cb.colorToMoveInverse][BISHOP] & MagicUtil.getBishopMoves(cb.kingIndex[cb.colorToMove], cb.allPieces);
            case ROOK:
                return cb.pieces[cb.colorToMoveInverse][ROOK] & MagicUtil.getRookMoves(cb.kingIndex[cb.colorToMove], cb.allPieces);
            case QUEEN:
                return cb.pieces[cb.colorToMoveInverse][QUEEN] & MagicUtil.getQueenMoves(cb.kingIndex[cb.colorToMove], cb.allPieces);
            default:
                return 0;
        }
    }

    public static boolean isInCheck(final int kingIndex, final int colorToMove, final long[] enemyPieces, final long allPieces) {

        return (enemyPieces[KNIGHT] & StaticMoves.KNIGHT_MOVES[kingIndex]
                | (enemyPieces[ROOK] | enemyPieces[QUEEN]) & MagicUtil.getRookMoves(kingIndex, allPieces)
                | (enemyPieces[BISHOP] | enemyPieces[QUEEN]) & MagicUtil.getBishopMoves(kingIndex, allPieces)
                | enemyPieces[PAWN] & StaticMoves.PAWN_ATTACKS[colorToMove][kingIndex]) != 0;
    }

    public static boolean isInCheckIncludingKing(final int kingIndex, final int colorToMove, final long[] enemyPieces, final long allPieces, final int enemyMajorPieces) {

        //TODO
        if (enemyMajorPieces == 0) {
            return (enemyPieces[PAWN] & StaticMoves.PAWN_ATTACKS[colorToMove][kingIndex]
                    | enemyPieces[KING] & StaticMoves.KING_MOVES[kingIndex]) != 0;
        }

        return (enemyPieces[KNIGHT] & StaticMoves.KNIGHT_MOVES[kingIndex]
                | (enemyPieces[ROOK] | enemyPieces[QUEEN]) & MagicUtil.getRookMoves(kingIndex, allPieces)
                | (enemyPieces[BISHOP] | enemyPieces[QUEEN]) & MagicUtil.getBishopMoves(kingIndex, allPieces)
                | enemyPieces[PAWN] & StaticMoves.PAWN_ATTACKS[colorToMove][kingIndex]
                | enemyPieces[KING] & StaticMoves.KING_MOVES[kingIndex]) != 0;
    }

    public static boolean isInCheckIncludingKing(final int kingIndex, final int colorToMove, final long[] enemyPieces, final long allPieces) {

        return (enemyPieces[KNIGHT] & StaticMoves.KNIGHT_MOVES[kingIndex]
                | (enemyPieces[ROOK] | enemyPieces[QUEEN]) & MagicUtil.getRookMoves(kingIndex, allPieces)
                | (enemyPieces[BISHOP] | enemyPieces[QUEEN]) & MagicUtil.getBishopMoves(kingIndex, allPieces)
                | enemyPieces[PAWN] & StaticMoves.PAWN_ATTACKS[colorToMove][kingIndex]
                | enemyPieces[KING] & StaticMoves.KING_MOVES[kingIndex]) != 0;
    }
}
