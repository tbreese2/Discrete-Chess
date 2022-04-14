/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

import static Engine.EngineValues.BISHOP;
import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.KING;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.WHITE;

import java.util.Arrays;
import java.util.Random;

import Engine.SearchTree;
import static Engine.MoveGen.ChessConstants.KNIGHT;

public class MoveGen {

    public static final boolean GENERATE_BR_PROMOTIONS = true;

    public static void generateMoves(final SearchTree moves, final ChessBoard board) {
        if (board.checkingPieces == 0) {
            generateNotInCheckMoves(moves, board);
        } else if (Long.bitCount(board.checkingPieces) == 1) {
            generateOutOfCheckMoves(moves, board);
        } else {
            // double check, only the king can move
            KingMoveGen.addKingMoves(moves, board);
        }
    }

    public static void generateCaptures(final SearchTree tree, final ChessBoard board) {
        if (board.checkingPieces == 0) {
            generateNotInCheckAttacks(tree, board);
        } else if (Long.bitCount(board.checkingPieces) == 1) {
            generateOutOfCheckAttacks(tree, board);
        } else {
            // double check, only the king can attack
            KingMoveGen.addKingCaptures(tree, board);
        }
    }

    private static void generateNotInCheckMoves(final SearchTree tree, final ChessBoard board) {

        // non pinned pieces
        final long nonPinned = ~board.pinnedPieces;
        final long[] pieces = board.pieces[board.colorToMove];
        KnightMoveGen.addKnightMoves(tree, pieces[KNIGHT] & nonPinned, board.emptySpaces);
        BishopMoveGen.addBishopMoves(tree, pieces[BISHOP] & nonPinned, board.allPieces, board.emptySpaces);
        RookMoveGen.addRookMoves(tree, pieces[ROOK] & nonPinned, board.allPieces, board.emptySpaces);
        QueenMoveGen.addQueenMoves(tree, pieces[QUEEN] & nonPinned, board.allPieces, board.emptySpaces);
        PawnMoveGen.addPawnMoves(tree, pieces[PAWN] & nonPinned, board, board.emptySpaces);
        KingMoveGen.addKingMoves(tree, board);

        // pinned pieces
        long piece = board.friendlyPieces[board.colorToMove] & board.pinnedPieces;
        while (piece != 0) {
            switch (board.pieceIndexes[Long.numberOfTrailingZeros(piece)]) {
                case PAWN:
                    PawnMoveGen.addPawnMoves(tree, Long.lowestOneBit(piece), board,
                            board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
                    break;
                case BISHOP:
                    BishopMoveGen.addBishopMoves(tree, Long.lowestOneBit(piece), board.allPieces,
                            board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
                    break;
                case ROOK:
                    RookMoveGen.addRookMoves(tree, Long.lowestOneBit(piece), board.allPieces,
                            board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
                    break;
                case QUEEN:
                    QueenMoveGen.addQueenMoves(tree, Long.lowestOneBit(piece), board.allPieces,
                            board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
            }
            piece &= piece - 1;
        }

    }

    private static void generateOutOfCheckMoves(final SearchTree tree, final ChessBoard board) {
        final long inBetween = ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)];
        if (inBetween != 0) {
            final long nonPinned = ~board.pinnedPieces;
            final long[] pieces = board.pieces[board.colorToMove];
            PawnMoveGen.addPawnMoves(tree, pieces[PAWN] & nonPinned, board, inBetween);
            KnightMoveGen.addKnightMoves(tree, pieces[KNIGHT] & nonPinned, inBetween);
            BishopMoveGen.addBishopMoves(tree, pieces[BISHOP] & nonPinned, board.allPieces, inBetween);
            RookMoveGen.addRookMoves(tree, pieces[ROOK] & nonPinned, board.allPieces, inBetween);
            QueenMoveGen.addQueenMoves(tree, pieces[QUEEN] & nonPinned, board.allPieces, inBetween);
        }

        KingMoveGen.addKingMoves(tree, board);
    }

    private static void generateNotInCheckAttacks(final SearchTree tree, final ChessBoard board) {

        // non pinned pieces
        addEpAttacks(tree, board);
        final long nonPinned = ~board.pinnedPieces;
        final long enemies = board.friendlyPieces[board.colorToMoveInverse];
        final long[] pieces = board.pieces[board.colorToMove];
        PawnMoveGen.addPawnCapturesAndPromotions(tree, pieces[PAWN] & nonPinned, board, enemies, board.emptySpaces);
        KnightMoveGen.addKnightCaptures(tree, pieces[KNIGHT] & nonPinned, board.pieceIndexes, enemies);
        BishopMoveGen.addBishopCaptures(tree, pieces[BISHOP] & nonPinned, board, enemies);
        RookMoveGen.addRookCaptures(tree, pieces[ROOK] & nonPinned, board, enemies);
        QueenMoveGen.addQueenCaptures(tree, pieces[QUEEN] & nonPinned, board, enemies);
        KingMoveGen.addKingCaptures(tree, board);

        // pinned pieces
        long piece = board.friendlyPieces[board.colorToMove] & board.pinnedPieces;
        while (piece != 0) {
            switch (board.pieceIndexes[Long.numberOfTrailingZeros(piece)]) {
                case PAWN:
                    PawnMoveGen.addPawnCapturesAndPromotions(tree, Long.lowestOneBit(piece), board,
                            enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]], 0);
                    break;
                case BISHOP:
                    BishopMoveGen.addBishopCaptures(tree, Long.lowestOneBit(piece), board,
                            enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
                    break;
                case ROOK:
                    RookMoveGen.addRookCaptures(tree, Long.lowestOneBit(piece), board,
                            enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
                    break;
                case QUEEN:
                    QueenMoveGen.addQueenCaptures(tree, Long.lowestOneBit(piece), board,
                            enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]]);
            }
            piece &= piece - 1;
        }

    }

    private static void generateOutOfCheckAttacks(final SearchTree tree, final ChessBoard board) {
        // attack attacker
        final long nonPinned = ~board.pinnedPieces;
        final long[] pieces = board.pieces[board.colorToMove];
        addEpAttacks(tree, board);
        PawnMoveGen.addPawnCapturesAndPromotions(tree, pieces[PAWN] & nonPinned, board, board.checkingPieces,
                ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)]);
        KnightMoveGen.addKnightCaptures(tree, pieces[KNIGHT] & nonPinned, board.pieceIndexes, board.checkingPieces);
        BishopMoveGen.addBishopCaptures(tree, pieces[BISHOP] & nonPinned, board, board.checkingPieces);
        RookMoveGen.addRookCaptures(tree, pieces[ROOK] & nonPinned, board, board.checkingPieces);
        QueenMoveGen.addQueenCaptures(tree, pieces[QUEEN] & nonPinned, board, board.checkingPieces);
        KingMoveGen.addKingCaptures(tree, board);
    }

    public static void addEpAttacks(final SearchTree tree, final ChessBoard board) {
        if (board.epIndex == 0) {
            return;
        }
        long piece = board.pieces[board.colorToMove][PAWN] & StaticMoves.PAWN_ATTACKS[board.colorToMoveInverse][board.epIndex];
        while (piece != 0) {
            if (board.isLegalEPMove(Long.numberOfTrailingZeros(piece))) {
                tree.addNode(MoveUtil.createEPMove(Long.numberOfTrailingZeros(piece), board.epIndex));
            }
            piece &= piece - 1;
        }
    }
}
