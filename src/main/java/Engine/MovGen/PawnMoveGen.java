/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

import Engine.MoveList;
import Engine.*;

/**
 *
 * @author tylerbreese
 */
public class PawnMoveGen {

    
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
}
