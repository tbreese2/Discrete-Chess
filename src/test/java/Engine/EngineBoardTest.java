/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author tylerbreese
 */
public class EngineBoardTest {
    
    public EngineBoardTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of setBoard method, of class EngineBoard.
     */
    @Test
    public void testSetBoard() {
        System.out.println("setBoard");
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        boolean whiteToPlay = true;
        EngineBoard instance = new EngineBoard();
        instance.setBoard(FEN, EngineBoard.WHITE);
        assertTrue(instance.toNonMatrixString().equals("1111111111111111000000000000000000000000000000001111111111111111"));
    }
    
    /**
     * Test of setBoard method, of class EngineBoard, edge case.
     */
    @Test
    public void testSetBoardEdge() {
        System.out.println("setBoard");
        String FEN = "8/8/8/4p1K1/2k1P3/8/8/8";
        boolean whiteToPlay = true;
        EngineBoard instance = new EngineBoard();
        instance.setBoard(FEN, EngineBoard.WHITE);
        System.out.println(instance.toString());
        System.out.println(instance.toNonMatrixString());
        assertTrue(instance.toNonMatrixString().equals("0000000000000000000000000010100000001010000000000000000000000000"));
    }
}
