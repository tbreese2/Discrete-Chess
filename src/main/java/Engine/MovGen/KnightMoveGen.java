/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;

/**
 *
 * @author tylerbreese
 */
import Engine.*;


public class KnightMoveGen {
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves, returns movelist filled with all valid knight moves
    public static void genAllMoves(MoveList moves, EngineBoard board){
        if (board.checkingPieces == 0) {
            generateNotInCheckMoves(moves, board);
	} else if (Long.bitCount(board.checkingPieces) == 1) {
            generateInCheckMoves(moves, board);
	} else {
            //only the king can move
            KingMoveGen.genAllMoves(moves, board);
	}
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves and assumes the king is in check
    public static void generateNotInCheckMoves(MoveList moves, EngineBoard board) {
        
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves assuming the king is in check
    public static void generateInCheckMoves(MoveList moves, EngineBoard board) {
        
    }
}
