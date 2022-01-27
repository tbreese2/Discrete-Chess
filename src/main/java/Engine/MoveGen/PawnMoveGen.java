/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.WHITE;
import static Engine.MoveGen.MoveGen.GENERATE_BR_PROMOTIONS;
import Engine.SearchTree;

/**
 *
 * @author tylerbreese
 */
public class PawnMoveGen {
    
    public static void addPawnCapturesAndPromotions(final SearchTree tree, final long pawns, final ChessBoard board, final long enemies,
            final long emptySpaces) {

        if (pawns == 0) {
            return;
        }

        if (board.colorToMove == WHITE) {

            // non-promoting
            long piece = pawns & Bitboard.RANK_NON_PROMOTION[WHITE] & Bitboard.getBlackPawnAttacks(enemies);
            while (piece != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(piece);
                long moves = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex] & enemies;
                while (moves != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(moves);
                    tree.addMove(MoveUtil.createCaptureMove(fromIndex, toIndex, PAWN, board.pieceIndexes[toIndex]));
                    moves &= moves - 1;
                }
                piece &= piece - 1;
            }

            // promoting
            piece = pawns & Bitboard.RANK_7;
            while (piece != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(piece);

                // promotion move
                if ((Long.lowestOneBit(piece) << 8 & emptySpaces) != 0) {
                    addPromotionMove(tree, fromIndex, fromIndex + 8);
                }

                // promotion attacks
                addPromotionCaptures(tree, StaticMoves.PAWN_ATTACKS[WHITE][fromIndex] & enemies, fromIndex, board.pieceIndexes);

                piece &= piece - 1;
            }
        } else {
            // non-promoting
            long piece = pawns & Bitboard.RANK_NON_PROMOTION[BLACK] & Bitboard.getWhitePawnAttacks(enemies);
            while (piece != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(piece);
                long moves = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex] & enemies;
                while (moves != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(moves);
                    tree.addMove(MoveUtil.createCaptureMove(fromIndex, toIndex, PAWN, board.pieceIndexes[toIndex]));
                    moves &= moves - 1;
                }
                piece &= piece - 1;
            }

            // promoting
            piece = pawns & Bitboard.RANK_2;
            while (piece != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(piece);

                // promotion move
                if ((Long.lowestOneBit(piece) >>> 8 & emptySpaces) != 0) {
                    addPromotionMove(tree, fromIndex, fromIndex - 8);
                }

                // promotion attacks
                addPromotionCaptures(tree, StaticMoves.PAWN_ATTACKS[BLACK][fromIndex] & enemies, fromIndex, board.pieceIndexes);

                piece &= piece - 1;
            }
        }
    }
    
    private static void addPromotionMove(final SearchTree tree, final int fromIndex, final int toIndex) {
        tree.addMove(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex, toIndex));
        tree.addMove(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex, toIndex));
        if (GENERATE_BR_PROMOTIONS) {
            tree.addMove(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex, toIndex));
            tree.addMove(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex, toIndex));
        }
    }
    
    private static void addPromotionCaptures(final SearchTree tree, long moves, final int fromIndex, final int[] indexes) {
        while (moves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(moves);
            tree.addMove(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex, toIndex, indexes[toIndex]));
            tree.addMove(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex, toIndex, indexes[toIndex]));
            if (GENERATE_BR_PROMOTIONS) {
                tree.addMove(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex, toIndex, indexes[toIndex]));
                tree.addMove(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex, toIndex, indexes[toIndex]));
            }
            moves &= moves - 1;
        }
    }
    
    public static void addPawnMoves(final SearchTree tree, final long pawns, final ChessBoard board, final long emptySpaces) {

        if (pawns == 0) {
            return;
        }

        if (board.colorToMove == WHITE) {

            long piece = pawns & (emptySpaces >>> 8) & Bitboard.RANK_23456;
            while (piece != 0) {
                tree.addMove(MoveUtil.createWhitePawnMove(Long.numberOfTrailingZeros(piece)));
                piece &= piece - 1;
            }

            piece = pawns & (emptySpaces >>> 16) & Bitboard.RANK_2;
            while (piece != 0) {
                if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                    tree.addMove(MoveUtil.createWhitePawn2Move(Long.numberOfTrailingZeros(piece)));
                }
                piece &= piece - 1;
            }
        } else {

            long piece = pawns & (emptySpaces << 8) & Bitboard.RANK_34567;
            while (piece != 0) {
                tree.addMove(MoveUtil.createBlackPawnMove(Long.numberOfTrailingZeros(piece)));
                piece &= piece - 1;
            }

            piece = pawns & (emptySpaces << 16) & Bitboard.RANK_7;
            while (piece != 0) {
                if ((board.emptySpaces & (Long.lowestOneBit(piece) >>> 8)) != 0) {
                    tree.addMove(MoveUtil.createBlackPawn2Move(Long.numberOfTrailingZeros(piece)));
                }
                piece &= piece - 1;
            }
        }
    }
}
