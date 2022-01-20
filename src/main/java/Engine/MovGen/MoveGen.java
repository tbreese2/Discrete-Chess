/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

import Engine.Bitboard;
import Engine.MoveList;
import Engine.EngineBoard;
/**
 *
 * @author tylerbreese
 */
public class MoveGen {
    
    public static final int genAllMoves(MoveList moves, EngineBoard board){
        int count = 0;
        if (board.checkingPieces == 0) {
            generateNoCheckMoves(moves, board);
	} else if (Long.bitCount(board.checkingPieces) == 1) {
            generateOneCheckMoves(moves, board);
	} else {
            // double check, only the king can move
            //addKingMoves(moves, board);
        }
        return count;
    }
     
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves and assumes the king is in check
    public static void generateNoCheckMoves(final MoveList moves, final EngineBoard board) {
        //do non-pinned pieces
	final long nonPinned = ~board.pinnedPieces;
	final long[] pieces = board.pieces[board.colorsTurn];
        
	addKnightMoves(moves, pieces[board.KNIGHT] & nonPinned, board.emptySpaces);
        addPawnMoves(moves, pieces[board.PAWN] & nonPinned, board, board.emptySpaces);
        addBishopMoves(moves, pieces[board.BISHOP] & nonPinned, board.allPieces, board.emptySpaces);
        addRookMoves(moves, pieces[board.ROOK] & nonPinned, board.allPieces, board.emptySpaces);
        addQueenMoves(moves, pieces[board.QUEEN] & nonPinned, board.allPieces, board.emptySpaces);
        addKingMoves(moves, board);
        
        //then pinned pieces
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves assuming the king is in check
    public static void generateOneCheckMoves(final MoveList moves, final EngineBoard board) {
        
    }
    
    //MODIFIES: movelist
    //EFFECTS: adds bishop moves to move list
    public static void addBishopMoves(final MoveList moveList, long piece, final long allPieces, final long emptySpaces) {
        while (piece != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(piece);
            long moves = MagicUtil.getBishopMoves(fromIndex, allPieces) & emptySpaces;
            while (moves != 0) {
                moveList.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), EngineBoard.BISHOP));
                moves &= moves - 1;
            }

            piece &= piece - 1;
        }
    }
    
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
      
       
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves, returns movelist filled with all valid knight moves
    public static void addKnightMoves(final MoveList moves, long pieces, final long emptySpaces){
        while (pieces != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(pieces);
            long pieceMoves = StaticMoves.KNIGHT_MOVES[fromIndex] & emptySpaces;
            while (pieceMoves != 0) {
                moves.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(pieceMoves), EngineBoard.KNIGHT));
                pieceMoves &= pieceMoves - 1;
            }
            pieces &= pieces - 1;
	}
    }
    
      //MODIFIES: movelist
    //EFFECTS: adds pawn moves to move list, no en pessant or upgrades
    //however.
    public static void addPawnMoves(final MoveList moves, long pieces, final EngineBoard board, final long possiblePositions) {

        if (pieces == 0) {
            return;
        }

        if (board.colorsTurn == board.WHITE) {

            long piece = pieces & (possiblePositions >>> 8) & Bitboard.RANK_23456;
            while (piece != 0) {
                moves.reserved_add(MoveUtil.createWhitePawnMove(Long.numberOfTrailingZeros(piece)));
                piece &= piece - 1;
            }

            piece = pieces & (possiblePositions >>> 16) & Bitboard.RANK_2;
            while (piece != 0) {
                if ((board.emptySpaces & (Long.lowestOneBit(piece) << 8)) != 0) {
                    moves.reserved_add(MoveUtil.createWhitePawn2Move(Long.numberOfTrailingZeros(piece)));
                }
                piece &= piece - 1;
            }
        } else {

            long piece = pieces & (possiblePositions << 8) & Bitboard.RANK_34567;
            while (piece != 0) {
                moves.reserved_add(MoveUtil.createBlackPawnMove(Long.numberOfTrailingZeros(piece)));
                piece &= piece - 1;
            }

            piece = pieces & (possiblePositions << 16) & Bitboard.RANK_7;
            while (piece != 0) {
                if ((board.emptySpaces & (Long.lowestOneBit(piece) >>> 8)) != 0) {
                     moves.reserved_add(MoveUtil.createBlackPawn2Move(Long.numberOfTrailingZeros(piece)));
                }
                piece &= piece - 1;
            }
        }
    }
    
    //MODIFIES: movelist
    //EFFECTS: adds rook moves to move list
    public static void addRookMoves(final MoveList moveList, long piece, final long allPieces, final long possiblePositions) {
        while (piece != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(piece);
            long moves = MagicUtil.getRookMoves(fromIndex, allPieces) & possiblePositions;
            while (moves != 0) {
                moveList.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), EngineBoard.ROOK));
                moves &= moves - 1;
            }
            piece &= piece - 1;
        }
    }
    
    //MODIFIES: movelist
    //EFFECTS: adds queen moves to the move list
    public static void addQueenMoves(final MoveList moveList, long piece, final long allPieces, final long possiblePositions) {
        while (piece != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(piece);
            long moves = MagicUtil.getQueenMoves(fromIndex, allPieces) & possiblePositions;
            while (moves != 0) {
                moveList.reserved_add(MoveUtil.createMove(fromIndex, Long.numberOfTrailingZeros(moves), EngineBoard.QUEEN));
                moves &= moves - 1;
            }

            piece &= piece - 1;
        }
    }
}
