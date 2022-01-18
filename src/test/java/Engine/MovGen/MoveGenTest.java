/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine.MovGen;

import Engine.EngineBoard;
import Engine.MoveList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 *
 * @author tylerbreese
 */
public class MoveGenTest {
    
    public MoveGenTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    

    /**
     * Test of genAllMoves method, of class MoveGen.
     */
    @Test
    public void testGenAllMoves() {
        System.out.println("genAllMoves");
        MoveList moves = new MoveList();
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        boolean whiteToPlay = true;
        EngineBoard instance = new EngineBoard();
        instance.setBoard(FEN, EngineBoard.WHITE);
        MoveGen.genAllMoves(moves, instance);
        assertTrue(moves.reserved_getCurrentSize() == 4);
        ArrayList<Integer> toMoves = new ArrayList<>();
        int move = moves.next();
        while(move != 0) {
            toMoves.add(MoveUtil.getToIndex(move));
            move = moves.next();
        }
        
        assertTrue(toMoves.contains(40));
        assertTrue(toMoves.contains(42));
        assertTrue(toMoves.contains(45));
        assertTrue(toMoves.contains(47));
    }

    /**
     * Test of generateNoCheckMoves method, of class MoveGen.
     */
    @Test
    public void testGenerateNoCheckMoves() {
        System.out.println("generateNoCheckMoves");
        MoveList moves = null;
        EngineBoard board = null;
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of generateOneCheckMoves method, of class MoveGen.
     */
    @Test
    public void testGenerateOneCheckMoves() {
        System.out.println("generateOneCheckMoves");
        MoveList moves = null;
        EngineBoard board = null;
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
