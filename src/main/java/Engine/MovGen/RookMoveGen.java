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

public class RookMoveGen {

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
}
