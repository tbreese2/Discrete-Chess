/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine.MovGen;

import Engine.Bitboard;
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
        ArrayList<Integer> toMoves = new ArrayList<>();
        int move = moves.next();
        while(move != 0) {
            toMoves.add(MoveUtil.getToIndex(move));
            move = moves.next();
        }
        System.out.println(toMoves);
        assertTrue(moves.reserved_getCurrentSize() == 20);
        assertTrue(toMoves.contains(16));
        assertTrue(toMoves.contains(17));
        assertTrue(toMoves.contains(18));
        assertTrue(toMoves.contains(19));
        assertTrue(toMoves.contains(21));
        assertTrue(toMoves.contains(22));
        assertTrue(toMoves.contains(23));
        assertTrue(toMoves.contains(24));
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
