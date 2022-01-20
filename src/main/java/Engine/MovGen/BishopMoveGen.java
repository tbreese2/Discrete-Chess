/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

/**
 *
 * @author tylerbreese
 */
import Engine.MoveList;
import Engine.EngineBoard;

public class BishopMoveGen {

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

}
