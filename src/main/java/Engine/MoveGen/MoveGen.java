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

//main move generation class
//just a note to anyone unfortunate enough to be reading this
//i know its really ugly
//im sorry, it has to be this way for maximum speed
public class MoveGen {
    
    //setting for engine move gen, might change with later engine variations
    public static final boolean GENERATE_BR_PROMOTIONS = true;

    //MODIFIES: moves
    //EFFECTS: calculates all non capture moves
    //for a given position
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
        
        //nonpinned bishop moves
        long pieceList = pieces[BISHOP] & nonPinned;
        while (pieceList != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
            long moves = MagicUtil.getBishopMoves(fromIndex, board.allPieces) & board.emptySpaces;
            while (moves != 0) {
                tree.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), BISHOP));
                moves &= moves - 1;
            }
            pieceList &= pieceList - 1;
        }
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
                    pieceList = Long.lowestOneBit(piece);
                    while (pieceList != 0) {
                        final int fromIndex1 = Long.numberOfTrailingZeros(Long.lowestOneBit(piece));
                        long moves1 = MagicUtil.getBishopMoves(fromIndex1, board.allPieces) & board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]];
                        while (moves1 != 0) {
                            tree.addNode(MoveUtil.createMove(fromIndex1, Long.numberOfTrailingZeros(moves1), BISHOP));
                            moves1 &= moves1 - 1;
                        }
                        pieceList &= pieceList - 1;
                    }
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
            
            //bishop moves
            long pieceList = pieces[BISHOP] & nonPinned;
            while (pieceList != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
                long moves = MagicUtil.getBishopMoves(fromIndex, board.allPieces) & inBetween;
                while (moves != 0) {
                    tree.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), BISHOP));
                    moves &= moves - 1;
                }
                pieceList &= pieceList - 1;
            }
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
        long pieceList = pieces[BISHOP] & nonPinned;
        while (pieceList != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
            long moves = MagicUtil.getBishopMoves(fromIndex, board.allPieces) & enemies;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, BISHOP, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
            pieceList &= pieceList - 1;
        }
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
                pieceList = Long.lowestOneBit(piece);
                while (pieceList != 0) {
                    final int fromIndex1 = Long.numberOfTrailingZeros(Long.lowestOneBit(piece));
                    long moves1 = MagicUtil.getBishopMoves(fromIndex1, board.allPieces) & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece)][board.kingIndex[board.colorToMove]];
                    while (moves1 != 0) {
                        final int toIndex1 = Long.numberOfTrailingZeros(moves1);
                        tree.addNode(MoveUtil.createCaptureMove(fromIndex1, toIndex1, BISHOP, board.pieceIndexes[toIndex1]));
                        moves1 &= moves1 - 1;
                    }
                    pieceList &= pieceList - 1;
                }
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
        long pieceList;
        final long nonPinned = ~board.pinnedPieces;
        final long[] pieces = board.pieces[board.colorToMove];
        addEpAttacks(tree, board);
        PawnMoveGen.addPawnCapturesAndPromotions(tree, pieces[PAWN] & nonPinned, board, board.checkingPieces,
                ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)]);
        KnightMoveGen.addKnightCaptures(tree, pieces[KNIGHT] & nonPinned, board.pieceIndexes, board.checkingPieces);
        
        pieceList = pieces[BISHOP] & nonPinned;
        while (pieceList != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
            long moves = MagicUtil.getBishopMoves(fromIndex, board.allPieces) & board.checkingPieces;
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, BISHOP, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
            pieceList &= pieceList - 1;
        }
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
