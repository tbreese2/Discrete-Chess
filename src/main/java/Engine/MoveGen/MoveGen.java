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
    public static final boolean BR = true;

    //MODIFIES: moves
    //EFFECTS: calculates all non capture moves
    //for a given position
    public static void generateMoves(final SearchTree moves, final ChessBoard board) {
        if (board.checkingPieces == 0) {
            // non pinned pieces
            final long nonPinned = ~board.pinnedPieces;
            final long[] pieces = board.pieces[board.colorToMove];
            long Knights = pieces[KNIGHT] & nonPinned;
            while (Knights != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(Knights);
                long moves1 = StaticMoves.KNIGHT_MOVES[fromIndex] & board.emptySpaces;
                while (moves1 != 0) {
                    moves.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves1), KNIGHT));
                    moves1 &= moves1 - 1;
                }
                Knights &= Knights - 1;
            }
            //nonpinned bishop moves
            long pieceList = pieces[BISHOP] & nonPinned;
            while (pieceList != 0) {
                final int fromIndex1 = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
                long moves2 = MagicUtil.getBishopMoves(fromIndex1, board.allPieces) & board.emptySpaces;
                while (moves2 != 0) {
                    moves.addNode(MoveUtil.createMove(fromIndex1, Long.numberOfTrailingZeros(moves2), BISHOP));
                    moves2 &= moves2 - 1;
                }
                pieceList &= pieceList - 1;
            }
            long rooks = pieces[ROOK] & nonPinned;
            while (rooks != 0) {
                final int fromIndex2 = Long.numberOfTrailingZeros(rooks);
                long moves3 = MagicUtil.getRookMoves(fromIndex2, board.allPieces) & board.emptySpaces;
                while (moves3 != 0) {
                    moves.addNode(MoveUtil.createMove(fromIndex2, Long.numberOfTrailingZeros(moves3), ROOK));
                    moves3 &= moves3 - 1;
                }
                rooks &= rooks - 1;
            }
            long queens = pieces[QUEEN] & nonPinned;
            while (queens != 0) {
                final int fromIndex3 = Long.numberOfTrailingZeros(queens);
                long moves4 = MagicUtil.getQueenMoves(fromIndex3, board.allPieces) & board.emptySpaces;
                while (moves4 != 0) {
                    moves.addNode(MoveUtil.createMove(fromIndex3, Long.numberOfTrailingZeros(moves4), QUEEN));
                    moves4 &= moves4 - 1;
                }
                queens &= queens - 1;
            }
            long pawns = pieces[PAWN] & nonPinned;
            if (pawns == 0) {
            } else {
                if (board.colorToMove == WHITE) {
                    long piece = pawns & (board.emptySpaces >>> 8) & Bitboard.RANK_23456;
                    while (piece != 0) {
                        moves.addNode(MoveUtil.createWhitePawnMove(Long.numberOfTrailingZeros(piece)));
                        piece &= piece - 1;
                    }
                    piece = pawns & (board.emptySpaces >>> 16) & Bitboard.RANK_2;
                    while (piece != 0) {
                        if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                            moves.addNode(MoveUtil.createWhitePawn2Move(Long.numberOfTrailingZeros(piece)));
                        }
                        piece &= piece - 1;
                    }
                } else {
                    long piece1 = pawns & (board.emptySpaces << 8) & Bitboard.RANK_34567;
                    while (piece1 != 0) {
                        moves.addNode(MoveUtil.createBlackPawnMove(Long.numberOfTrailingZeros(piece1)));
                        piece1 &= piece1 - 1;
                    }
                    piece1 = pawns & (board.emptySpaces << 16) & Bitboard.RANK_7;
                    while (piece1 != 0) {
                        if ((board.emptySpaces & (Long.lowestOneBit(piece1) >>> 8)) != 0) {
                            moves.addNode(MoveUtil.createBlackPawn2Move(Long.numberOfTrailingZeros(piece1)));
                        }
                        piece1 &= piece1 - 1;
                    }
                }
            }
            final int fromIndex4 = board.kingIndex[board.colorToMove];
            long moves5 = StaticMoves.KING_MOVES[fromIndex4] & board.emptySpaces;
            while (moves5 != 0) {
                moves.addNode(MoveUtil.createMove(fromIndex4, Long.numberOfTrailingZeros(moves5), KING));
                moves5 &= moves5 - 1;
            }
            if (board.checkingPieces == 0) {
                long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
                while (castlingIndexes != 0) {
                    final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                    if (CastlingUtil.isValidCMove(board, fromIndex4, castlingIndex)) {
                        moves.addNode(MoveUtil.createCastlingMove(fromIndex4, castlingIndex));
                    }
                    castlingIndexes &= castlingIndexes - 1;
                }
            }
            // pinned pieces
            long piece2 = board.friendlyPieces[board.colorToMove] & board.pinnedPieces;
            while (piece2 != 0) {
                long queens1 = Long.lowestOneBit(piece2);
                long rooks1 = Long.lowestOneBit(piece2);
                long pawns1 = Long.lowestOneBit(piece2);
                switch (board.pieceIndexes[Long.numberOfTrailingZeros(piece2)]) {
                    case PAWN:
                        if (pawns1 == 0) {
                        } else {
                            if (board.colorToMove == WHITE) {
                                long piece21 = pawns1 & (board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]] >>> 8) & Bitboard.RANK_23456;
                                while (piece21 != 0) {
                                    moves.addNode(MoveUtil.createWhitePawnMove(Long.numberOfTrailingZeros(piece21)));
                                    piece21 &= piece21 - 1;
                                }
                                piece21 = pawns1 & (board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]] >>> 16) & Bitboard.RANK_2;
                                while (piece21 != 0) {
                                    if ((board.emptySpaces & (Long.lowestOneBit(piece21) << 8)) != 0) {
                                        moves.addNode(MoveUtil.createWhitePawn2Move(Long.numberOfTrailingZeros(piece21)));
                                    }
                                    piece21 &= piece21 - 1;
                                }
                            } else {
                                long piece3 = pawns1 & (board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]] << 8) & Bitboard.RANK_34567;
                                while (piece3 != 0) {
                                    moves.addNode(MoveUtil.createBlackPawnMove(Long.numberOfTrailingZeros(piece3)));
                                    piece3 &= piece3 - 1;
                                }
                                piece3 = pawns1 & (board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]] << 16) & Bitboard.RANK_7;
                                while (piece3 != 0) {
                                    if ((board.emptySpaces & (Long.lowestOneBit(piece3) >>> 8)) != 0) {
                                        moves.addNode(MoveUtil.createBlackPawn2Move(Long.numberOfTrailingZeros(piece3)));
                                    }
                                    piece3 &= piece3 - 1;
                                }
                            }
                        }
                        break;
                    case BISHOP:
                        pieceList = Long.lowestOneBit(piece2);
                        while (pieceList != 0) {
                            final int fromIndex11 = Long.numberOfTrailingZeros(Long.lowestOneBit(piece2));
                            long moves11 = MagicUtil.getBishopMoves(fromIndex11, board.allPieces) & board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves11 != 0) {
                                moves.addNode(MoveUtil.createMove(fromIndex11, Long.numberOfTrailingZeros(moves11), BISHOP));
                                moves11 &= moves11 - 1;
                            }
                            pieceList &= pieceList - 1;
                        }
                        break;
                    case ROOK:
                        while (rooks1 != 0) {
                            final int fromIndex21 = Long.numberOfTrailingZeros(rooks1);
                            long moves21 = MagicUtil.getRookMoves(fromIndex21, board.allPieces) & board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves21 != 0) {
                                moves.addNode(MoveUtil.createMove(fromIndex21, Long.numberOfTrailingZeros(moves21), ROOK));
                                moves21 &= moves21 - 1;
                            }
                            rooks1 &= rooks1 - 1;
                        }
                        break;
                    case QUEEN:
                        while (queens1 != 0) {
                            final int fromIndex22 = Long.numberOfTrailingZeros(queens1);
                            long moves22 = MagicUtil.getQueenMoves(fromIndex22, board.allPieces) & board.emptySpaces & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves22 != 0) {
                                moves.addNode(MoveUtil.createMove(fromIndex22, Long.numberOfTrailingZeros(moves22), QUEEN));
                                moves22 &= moves22 - 1;
                            }
                            queens1 &= queens1 - 1;
                        }
                }
                piece2 &= piece2 - 1;
            }
        } else if (Long.bitCount(board.checkingPieces) == 1) {
            final long inBetween = ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)];
            if (inBetween != 0) {
                final long nonPinned = ~board.pinnedPieces;
                final long[] pieces = board.pieces[board.colorToMove];
                long pawns = pieces[PAWN] & nonPinned;
                if (pawns == 0) {
                } else {
                    if (board.colorToMove == WHITE) {
                        long piece = pawns & (inBetween >>> 8) & Bitboard.RANK_23456;
                        while (piece != 0) {
                            moves.addNode(MoveUtil.createWhitePawnMove(Long.numberOfTrailingZeros(piece)));
                            piece &= piece - 1;
                        }
                        piece = pawns & (inBetween >>> 16) & Bitboard.RANK_2;
                        while (piece != 0) {
                            if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                                moves.addNode(MoveUtil.createWhitePawn2Move(Long.numberOfTrailingZeros(piece)));
                            }
                            piece &= piece - 1;
                        }
                    } else {
                        long piece1 = pawns & (inBetween << 8) & Bitboard.RANK_34567;
                        while (piece1 != 0) {
                            moves.addNode(MoveUtil.createBlackPawnMove(Long.numberOfTrailingZeros(piece1)));
                            piece1 &= piece1 - 1;
                        }
                        piece1 = pawns & (inBetween << 16) & Bitboard.RANK_7;
                        while (piece1 != 0) {
                            if ((board.emptySpaces & (Long.lowestOneBit(piece1) >>> 8)) != 0) {
                                moves.addNode(MoveUtil.createBlackPawn2Move(Long.numberOfTrailingZeros(piece1)));
                            }
                            piece1 &= piece1 - 1;
                        }
                    }
                }
                long Knights = pieces[KNIGHT] & nonPinned;
                while (Knights != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(Knights);
                    long moves1 = StaticMoves.KNIGHT_MOVES[fromIndex] & inBetween;
                    while (moves1 != 0) {
                        moves.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves1), KNIGHT));
                        moves1 &= moves1 - 1;
                    }
                    Knights &= Knights - 1;
                }
                //bishop moves
                long pieceList = pieces[BISHOP] & nonPinned;
                while (pieceList != 0) {
                    final int fromIndex1 = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
                    long moves2 = MagicUtil.getBishopMoves(fromIndex1, board.allPieces) & inBetween;
                    while (moves2 != 0) {
                        moves.addNode(MoveUtil.createMove(fromIndex1, Long.numberOfTrailingZeros(moves2), BISHOP));
                        moves2 &= moves2 - 1;
                    }
                    pieceList &= pieceList - 1;
                }
                long rooks = pieces[ROOK] & nonPinned;
                while (rooks != 0) {
                    final int fromIndex2 = Long.numberOfTrailingZeros(rooks);
                    long moves3 = MagicUtil.getRookMoves(fromIndex2, board.allPieces) & inBetween;
                    while (moves3 != 0) {
                        moves.addNode(MoveUtil.createMove(fromIndex2, Long.numberOfTrailingZeros(moves3), ROOK));
                        moves3 &= moves3 - 1;
                    }
                    rooks &= rooks - 1;
                }
                long queens = pieces[QUEEN] & nonPinned;
                while (queens != 0) {
                    final int fromIndex3 = Long.numberOfTrailingZeros(queens);
                    long moves4 = MagicUtil.getQueenMoves(fromIndex3, board.allPieces) & inBetween;
                    while (moves4 != 0) {
                        moves.addNode(MoveUtil.createMove(fromIndex3, Long.numberOfTrailingZeros(moves4), QUEEN));
                        moves4 &= moves4 - 1;
                    }
                    queens &= queens - 1;
                }
            }
            final int fromIndex4 = board.kingIndex[board.colorToMove];
            long moves5 = StaticMoves.KING_MOVES[fromIndex4] & board.emptySpaces;
            while (moves5 != 0) {
                moves.addNode(MoveUtil.createMove(fromIndex4, Long.numberOfTrailingZeros(moves5), KING));
                moves5 &= moves5 - 1;
            }
            if (board.checkingPieces == 0) {
                long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
                while (castlingIndexes != 0) {
                    final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                    if (CastlingUtil.isValidCMove(board, fromIndex4, castlingIndex)) {
                        moves.addNode(MoveUtil.createCastlingMove(fromIndex4, castlingIndex));
                    }
                    castlingIndexes &= castlingIndexes - 1;
                }
            }
        } else {
            final int fromIndex = board.kingIndex[board.colorToMove];
            long moves1 = StaticMoves.KING_MOVES[fromIndex] & board.emptySpaces;
            while (moves1 != 0) {
                moves.addNode(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves1), KING));
                moves1 &= moves1 - 1;
            }
            // double check, only the king can move
            if (board.checkingPieces == 0) {
                long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
                while (castlingIndexes != 0) {
                    final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                    if (CastlingUtil.isValidCMove(board, fromIndex, castlingIndex)) {
                        moves.addNode(MoveUtil.createCastlingMove(fromIndex, castlingIndex));
                    }
                    castlingIndexes &= castlingIndexes - 1;
                }
            }
        }
    }

    //MODIFIES: moves
    //EFFECTS: calculates all capture moves
    //for a given position
    public static void generateCaptures(final SearchTree tree, final ChessBoard board) {
        if (board.checkingPieces == 0) {
            // non pinned pieces
            if (board.epIndex == 0) {
            } else {
                long piece = board.pieces[board.colorToMove][PAWN] & StaticMoves.PAWN_ATTACKS[board.colorToMoveInverse][board.epIndex];
                while (piece != 0) {
                    if (board.isLegalEPMove(Long.numberOfTrailingZeros(piece))) {
                        tree.addNode(MoveUtil.createEPMove(Long.numberOfTrailingZeros(piece), board.epIndex));
                    }
                    piece &= piece - 1;
                }
            }
            final long nonPinned = ~board.pinnedPieces;
            final long enemies = board.friendlyPieces[board.colorToMoveInverse];
            final long[] pieces = board.pieces[board.colorToMove];
            long pawns = pieces[PAWN] & nonPinned;
            if (pawns == 0) {
            } else {
                if (board.colorToMove == WHITE) {
                    long piece = pawns & Bitboard.RANK_NON_PROMOTION[WHITE] & Bitboard.getBlackPawnAttacks(enemies);
                    while (piece != 0) {
                        final int fromIndex = Long.numberOfTrailingZeros(piece);
                        long moves = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex] & enemies;
                        while (moves != 0) {
                            final int toIndex = Long.numberOfTrailingZeros(moves);
                            tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, PAWN, board.pieceIndexes[toIndex]));
                            moves &= moves - 1;
                        }
                        piece &= piece - 1;
                    }
                    piece = pawns & Bitboard.RANK_7;
                    while (piece != 0) {
                        final int fromIndex1 = Long.numberOfTrailingZeros(piece);
                        if ((Long.lowestOneBit(piece) << 8 & board.emptySpaces) != 0) {
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex1, fromIndex1 + 8));
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex1, fromIndex1 + 8));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex1, fromIndex1 + 8));
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex1, fromIndex1 + 8));
                            }
                        }
                        long moves1 = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex1] & enemies;
                        while (moves1 != 0) {
                            final int toIndex1 = Long.numberOfTrailingZeros(moves1);
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            }
                            moves1 &= moves1 - 1;
                        }
                        piece &= piece - 1;
                    }
                } else {
                    long piece1 = pawns & Bitboard.RANK_NON_PROMOTION[BLACK] & Bitboard.getWhitePawnAttacks(enemies);
                    while (piece1 != 0) {
                        final int fromIndex2 = Long.numberOfTrailingZeros(piece1);
                        long moves2 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex2] & enemies;
                        while (moves2 != 0) {
                            final int toIndex2 = Long.numberOfTrailingZeros(moves2);
                            tree.addNode(MoveUtil.createCaptureMove(fromIndex2, toIndex2, PAWN, board.pieceIndexes[toIndex2]));
                            moves2 &= moves2 - 1;
                        }
                        piece1 &= piece1 - 1;
                    }
                    piece1 = pawns & Bitboard.RANK_2;
                    while (piece1 != 0) {
                        final int fromIndex3 = Long.numberOfTrailingZeros(piece1);
                        if ((Long.lowestOneBit(piece1) >>> 8 & board.emptySpaces) != 0) {
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex3, fromIndex3 - 8));
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex3, fromIndex3 - 8));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex3, fromIndex3 - 8));
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex3, fromIndex3 - 8));
                            }
                        }
                        long moves3 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex3] & enemies;
                        while (moves3 != 0) {
                            final int toIndex3 = Long.numberOfTrailingZeros(moves3);
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            }
                            moves3 &= moves3 - 1;
                        }
                        piece1 &= piece1 - 1;
                    }
                }
            }
            long knights = pieces[KNIGHT] & nonPinned;
            while (knights != 0) {
                final int fromIndex4 = Long.numberOfTrailingZeros(knights);
                long moves4 = StaticMoves.KNIGHT_MOVES[fromIndex4] & enemies;
                while (moves4 != 0) {
                    final int toIndex4 = Long.numberOfTrailingZeros(moves4);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex4, toIndex4, KNIGHT, board.pieceIndexes[toIndex4]));
                    moves4 &= moves4 - 1;
                }
                knights &= knights - 1;
            }
            long pieceList = pieces[BISHOP] & nonPinned;
            while (pieceList != 0) {
                final int fromIndex5 = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
                long moves5 = MagicUtil.getBishopMoves(fromIndex5, board.allPieces) & enemies;
                while (moves5 != 0) {
                    final int toIndex5 = Long.numberOfTrailingZeros(moves5);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex5, toIndex5, BISHOP, board.pieceIndexes[toIndex5]));
                    moves5 &= moves5 - 1;
                }
                pieceList &= pieceList - 1;
            }
            long rooks = pieces[ROOK] & nonPinned;
            while (rooks != 0) {
                final int fromIndex6 = Long.numberOfTrailingZeros(rooks);
                long moves6 = MagicUtil.getRookMoves(fromIndex6, board.allPieces) & enemies;
                while (moves6 != 0) {
                    final int toIndex6 = Long.numberOfTrailingZeros(moves6);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex6, toIndex6, ROOK, board.pieceIndexes[toIndex6]));
                    moves6 &= moves6 - 1;
                }
                rooks &= rooks - 1;
            }
            long queens = pieces[QUEEN] & nonPinned;
            while (queens != 0) {
                final int fromIndex7 = Long.numberOfTrailingZeros(queens);
                long moves7 = MagicUtil.getQueenMoves(fromIndex7, board.allPieces) & enemies;
                while (moves7 != 0) {
                    final int toIndex7 = Long.numberOfTrailingZeros(moves7);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex7, toIndex7, QUEEN, board.pieceIndexes[toIndex7]));
                    moves7 &= moves7 - 1;
                }
                queens &= queens - 1;
            }
            final int fromIndex8 = board.kingIndex[board.colorToMove];
            long moves8 = StaticMoves.KING_MOVES[fromIndex8] & board.friendlyPieces[board.colorToMoveInverse] & ~board.discoveredPieces;
            while (moves8 != 0) {
                final int toIndex8 = Long.numberOfTrailingZeros(moves8);
                tree.addNode(MoveUtil.createCaptureMove(fromIndex8, toIndex8, KING, board.pieceIndexes[toIndex8]));
                moves8 &= moves8 - 1;
            }
            // pinned pieces
            long piece2 = board.friendlyPieces[board.colorToMove] & board.pinnedPieces;
            while (piece2 != 0) {
                long rooks1 = Long.lowestOneBit(piece2);
                long queens1 = Long.lowestOneBit(piece2);
                long pawns1 = Long.lowestOneBit(piece2);
                switch (board.pieceIndexes[Long.numberOfTrailingZeros(piece2)]) {
                    case PAWN:
                        if (pawns1 == 0) {
                        } else {
                            if (board.colorToMove == WHITE) {
                                long piece21 = pawns1 & Bitboard.RANK_NON_PROMOTION[WHITE] & Bitboard.getBlackPawnAttacks(enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]]);
                                while (piece21 != 0) {
                                    final int fromIndex41 = Long.numberOfTrailingZeros(piece21);
                                    long moves41 = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex41] & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                                    while (moves41 != 0) {
                                        final int toIndex41 = Long.numberOfTrailingZeros(moves41);
                                        tree.addNode(MoveUtil.createCaptureMove(fromIndex41, toIndex41, PAWN, board.pieceIndexes[toIndex41]));
                                        moves41 &= moves41 - 1;
                                    }
                                    piece21 &= piece21 - 1;
                                }
                                piece21 = pawns1 & Bitboard.RANK_7;
                                while (piece21 != 0) {
                                    final int fromIndex51 = Long.numberOfTrailingZeros(piece21);
                                    if ((Long.lowestOneBit(piece21) << 8 & 0) != 0) {
                                        tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex51, fromIndex51 + 8));
                                        tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex51, fromIndex51 + 8));
                                        if (BR) {
                                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex51, fromIndex51 + 8));
                                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex51, fromIndex51 + 8));
                                        }
                                    }
                                    long moves51 = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex51] & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                                    while (moves51 != 0) {
                                        final int toIndex51 = Long.numberOfTrailingZeros(moves51);
                                        tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex51, toIndex51, board.pieceIndexes[toIndex51]));
                                        tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex51, toIndex51, board.pieceIndexes[toIndex51]));
                                        if (BR) {
                                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex51, toIndex51, board.pieceIndexes[toIndex51]));
                                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex51, toIndex51, board.pieceIndexes[toIndex51]));
                                        }
                                        moves51 &= moves51 - 1;
                                    }
                                    piece21 &= piece21 - 1;
                                }
                            } else {
                                long piece3 = pawns1 & Bitboard.RANK_NON_PROMOTION[BLACK] & Bitboard.getWhitePawnAttacks(enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]]);
                                while (piece3 != 0) {
                                    final int fromIndex61 = Long.numberOfTrailingZeros(piece3);
                                    long moves61 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex61] & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                                    while (moves61 != 0) {
                                        final int toIndex61 = Long.numberOfTrailingZeros(moves61);
                                        tree.addNode(MoveUtil.createCaptureMove(fromIndex61, toIndex61, PAWN, board.pieceIndexes[toIndex61]));
                                        moves61 &= moves61 - 1;
                                    }
                                    piece3 &= piece3 - 1;
                                }
                                piece3 = pawns1 & Bitboard.RANK_2;
                                while (piece3 != 0) {
                                    final int fromIndex71 = Long.numberOfTrailingZeros(piece3);
                                    if ((Long.lowestOneBit(piece3) >>> 8 & 0) != 0) {
                                        tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex71, fromIndex71 - 8));
                                        tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex71, fromIndex71 - 8));
                                        if (BR) {
                                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex71, fromIndex71 - 8));
                                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex71, fromIndex71 - 8));
                                        }
                                    }
                                    long moves71 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex71] & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                                    while (moves71 != 0) {
                                        final int toIndex71 = Long.numberOfTrailingZeros(moves71);
                                        tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex71, toIndex71, board.pieceIndexes[toIndex71]));
                                        tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex71, toIndex71, board.pieceIndexes[toIndex71]));
                                        if (BR) {
                                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex71, toIndex71, board.pieceIndexes[toIndex71]));
                                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex71, toIndex71, board.pieceIndexes[toIndex71]));
                                        }
                                        moves71 &= moves71 - 1;
                                    }
                                    piece3 &= piece3 - 1;
                                }
                            }
                        }
                        break;
                    case BISHOP:
                        pieceList = Long.lowestOneBit(piece2);
                        while (pieceList != 0) {
                            final int fromIndex11 = Long.numberOfTrailingZeros(Long.lowestOneBit(piece2));
                            long moves11 = MagicUtil.getBishopMoves(fromIndex11, board.allPieces) & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves11 != 0) {
                                final int toIndex11 = Long.numberOfTrailingZeros(moves11);
                                tree.addNode(MoveUtil.createCaptureMove(fromIndex11, toIndex11, BISHOP, board.pieceIndexes[toIndex11]));
                                moves11 &= moves11 - 1;
                            }
                            pieceList &= pieceList - 1;
                        }
                        break;
                    case ROOK:
                        while (rooks1 != 0) {
                            final int fromIndex21 = Long.numberOfTrailingZeros(rooks1);
                            long moves21 = MagicUtil.getRookMoves(fromIndex21, board.allPieces) & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves21 != 0) {
                                final int toIndex12 = Long.numberOfTrailingZeros(moves21);
                                tree.addNode(MoveUtil.createCaptureMove(fromIndex21, toIndex12, ROOK, board.pieceIndexes[toIndex12]));
                                moves21 &= moves21 - 1;
                            }
                            rooks1 &= rooks1 - 1;
                        }
                        break;
                    case QUEEN:
                        while (queens1 != 0) {
                            final int fromIndex22 = Long.numberOfTrailingZeros(queens1);
                            long moves22 = MagicUtil.getQueenMoves(fromIndex22, board.allPieces) & enemies & ChessConstants.PINNED_MOVEMENT[Long.numberOfTrailingZeros(piece2)][board.kingIndex[board.colorToMove]];
                            while (moves22 != 0) {
                                final int toIndex13 = Long.numberOfTrailingZeros(moves22);
                                tree.addNode(MoveUtil.createCaptureMove(fromIndex22, toIndex13, QUEEN, board.pieceIndexes[toIndex13]));
                                moves22 &= moves22 - 1;
                            }
                            queens1 &= queens1 - 1;
                        }
                }
                piece2 &= piece2 - 1;
            }
        } else if (Long.bitCount(board.checkingPieces) == 1) {
            // attack attacker
            long pieceList;
            final long nonPinned = ~board.pinnedPieces;
            final long[] pieces = board.pieces[board.colorToMove];
            if (board.epIndex == 0) {
            } else {
                long piece = board.pieces[board.colorToMove][PAWN] & StaticMoves.PAWN_ATTACKS[board.colorToMoveInverse][board.epIndex];
                while (piece != 0) {
                    if (board.isLegalEPMove(Long.numberOfTrailingZeros(piece))) {
                        tree.addNode(MoveUtil.createEPMove(Long.numberOfTrailingZeros(piece), board.epIndex));
                    }
                    piece &= piece - 1;
                }
            }
            long pawns = pieces[PAWN] & nonPinned;
            if (pawns == 0) {
            } else {
                if (board.colorToMove == WHITE) {
                    long piece1 = pawns & Bitboard.RANK_NON_PROMOTION[WHITE] & Bitboard.getBlackPawnAttacks(board.checkingPieces);
                    while (piece1 != 0) {
                        final int fromIndex = Long.numberOfTrailingZeros(piece1);
                        long moves = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex] & board.checkingPieces;
                        while (moves != 0) {
                            final int toIndex = Long.numberOfTrailingZeros(moves);
                            tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, PAWN, board.pieceIndexes[toIndex]));
                            moves &= moves - 1;
                        }
                        piece1 &= piece1 - 1;
                    }
                    piece1 = pawns & Bitboard.RANK_7;
                    while (piece1 != 0) {
                        final int fromIndex1 = Long.numberOfTrailingZeros(piece1);
                        if ((Long.lowestOneBit(piece1) << 8 & ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)]) != 0) {
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex1, fromIndex1 + 8));
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex1, fromIndex1 + 8));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex1, fromIndex1 + 8));
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex1, fromIndex1 + 8));
                            }
                        }
                        long moves1 = StaticMoves.PAWN_ATTACKS[WHITE][fromIndex1] & board.checkingPieces;
                        while (moves1 != 0) {
                            final int toIndex1 = Long.numberOfTrailingZeros(moves1);
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex1, toIndex1, board.pieceIndexes[toIndex1]));
                            }
                            moves1 &= moves1 - 1;
                        }
                        piece1 &= piece1 - 1;
                    }
                } else {
                    long piece11 = pawns & Bitboard.RANK_NON_PROMOTION[BLACK] & Bitboard.getWhitePawnAttacks(board.checkingPieces);
                    while (piece11 != 0) {
                        final int fromIndex2 = Long.numberOfTrailingZeros(piece11);
                        long moves2 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex2] & board.checkingPieces;
                        while (moves2 != 0) {
                            final int toIndex2 = Long.numberOfTrailingZeros(moves2);
                            tree.addNode(MoveUtil.createCaptureMove(fromIndex2, toIndex2, PAWN, board.pieceIndexes[toIndex2]));
                            moves2 &= moves2 - 1;
                        }
                        piece11 &= piece11 - 1;
                    }
                    piece11 = pawns & Bitboard.RANK_2;
                    while (piece11 != 0) {
                        final int fromIndex3 = Long.numberOfTrailingZeros(piece11);
                        if ((Long.lowestOneBit(piece11) >>> 8 & ChessConstants.IN_BETWEEN[board.kingIndex[board.colorToMove]][Long.numberOfTrailingZeros(board.checkingPieces)]) != 0) {
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_Q, fromIndex3, fromIndex3 - 8));
                            tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_N, fromIndex3, fromIndex3 - 8));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_B, fromIndex3, fromIndex3 - 8));
                                tree.addNode(MoveUtil.createPromotionMove(MoveUtil.TYPE_PROMOTION_R, fromIndex3, fromIndex3 - 8));
                            }
                        }
                        long moves3 = StaticMoves.PAWN_ATTACKS[BLACK][fromIndex3] & board.checkingPieces;
                        while (moves3 != 0) {
                            final int toIndex3 = Long.numberOfTrailingZeros(moves3);
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_Q, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_N, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            if (BR) {
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_B, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                                tree.addNode(MoveUtil.createPromotionAttack(MoveUtil.TYPE_PROMOTION_R, fromIndex3, toIndex3, board.pieceIndexes[toIndex3]));
                            }
                            moves3 &= moves3 - 1;
                        }
                        piece11 &= piece11 - 1;
                    }
                }
            }
            long knights = pieces[KNIGHT] & nonPinned;
            while (knights != 0) {
                final int fromIndex4 = Long.numberOfTrailingZeros(knights);
                long moves4 = StaticMoves.KNIGHT_MOVES[fromIndex4] & board.checkingPieces;
                while (moves4 != 0) {
                    final int toIndex4 = Long.numberOfTrailingZeros(moves4);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex4, toIndex4, KNIGHT, board.pieceIndexes[toIndex4]));
                    moves4 &= moves4 - 1;
                }
                knights &= knights - 1;
            }
            pieceList = pieces[BISHOP] & nonPinned;
            while (pieceList != 0) {
                final int fromIndex5 = Long.numberOfTrailingZeros(pieces[BISHOP] & nonPinned);
                long moves5 = MagicUtil.getBishopMoves(fromIndex5, board.allPieces) & board.checkingPieces;
                while (moves5 != 0) {
                    final int toIndex5 = Long.numberOfTrailingZeros(moves5);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex5, toIndex5, BISHOP, board.pieceIndexes[toIndex5]));
                    moves5 &= moves5 - 1;
                }
                pieceList &= pieceList - 1;
            }
            long rooks = pieces[ROOK] & nonPinned;
            while (rooks != 0) {
                final int fromIndex6 = Long.numberOfTrailingZeros(rooks);
                long moves6 = MagicUtil.getRookMoves(fromIndex6, board.allPieces) & board.checkingPieces;
                while (moves6 != 0) {
                    final int toIndex6 = Long.numberOfTrailingZeros(moves6);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex6, toIndex6, ROOK, board.pieceIndexes[toIndex6]));
                    moves6 &= moves6 - 1;
                }
                rooks &= rooks - 1;
            }
            long queens = pieces[QUEEN] & nonPinned;
            while (queens != 0) {
                final int fromIndex7 = Long.numberOfTrailingZeros(queens);
                long moves7 = MagicUtil.getQueenMoves(fromIndex7, board.allPieces) & board.checkingPieces;
                while (moves7 != 0) {
                    final int toIndex7 = Long.numberOfTrailingZeros(moves7);
                    tree.addNode(MoveUtil.createCaptureMove(fromIndex7, toIndex7, QUEEN, board.pieceIndexes[toIndex7]));
                    moves7 &= moves7 - 1;
                }
                queens &= queens - 1;
            }
            final int fromIndex8 = board.kingIndex[board.colorToMove];
            long moves8 = StaticMoves.KING_MOVES[fromIndex8] & board.friendlyPieces[board.colorToMoveInverse] & ~board.discoveredPieces;
            while (moves8 != 0) {
                final int toIndex8 = Long.numberOfTrailingZeros(moves8);
                tree.addNode(MoveUtil.createCaptureMove(fromIndex8, toIndex8, KING, board.pieceIndexes[toIndex8]));
                moves8 &= moves8 - 1;
            }
        } else {
            final int fromIndex = board.kingIndex[board.colorToMove];
            long moves = StaticMoves.KING_MOVES[fromIndex] & board.friendlyPieces[board.colorToMoveInverse] & ~board.discoveredPieces;
            // double check, only the king can attack
            while (moves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(moves);
                tree.addNode(MoveUtil.createCaptureMove(fromIndex, toIndex, KING, board.pieceIndexes[toIndex]));
                moves &= moves - 1;
            }
        }
    }
}
