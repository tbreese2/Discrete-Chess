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
    
    //REQUIRES: pieces is bitboard of knights, emptySpaces i
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves, returns movelist filled with all valid knight moves
    public static void addKnightMoves(final MoveList moves, long pieces, long emptySpa){
       
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves and assumes the king is in check
    public static void generateNotInCheckMoves(MoveList moves, EngineBoard board) {
        //do non-pinned pieces
        
        //then pinned pieces
    }
    
    //MODIFIES: moves
    //EFFECTS: generates all possible knight moves assuming the king is in check
    public static void generateInCheckMoves(MoveList moves, EngineBoard board) {
        
    }
}
