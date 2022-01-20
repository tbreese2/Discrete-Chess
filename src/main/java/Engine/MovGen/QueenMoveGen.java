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

public class QueenMoveGen {

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
