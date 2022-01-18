/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

import Engine.*;
import Engine.MoveList;
import Engine.MovGen.KnightMoveGen;
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
    public static void generateNoCheckMoves(MoveList moves, EngineBoard board) {
        //do non-pinned pieces
        // non pinned pieces

	final long nonPinned = ~board.pinnedPieces;
	final long[] pieces = board.pieces[board.colorsTurn];
        
	KnightMoveGen.addKnightMoves(moves, pieces[board.KNIGHT] & nonPinned, board.emptySpaces);
        //then pinned pieces
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves assuming the king is in check
    public static void generateOneCheckMoves(MoveList moves, EngineBoard board) {
        
    }
}
