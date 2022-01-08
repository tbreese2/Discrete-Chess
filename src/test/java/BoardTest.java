/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author tylerbreese
 */
public class BoardTest {
    
    public BoardTest() {
    }
    
    
    @Test
    public void testSetBoard() {
        System.out.println("setBoard");
        Board instance = new Board();
        instance.setBoard();
        // TODO review the generated test code and remove the default call to fail.
        String expResult = "rnbqkbnr\npppppppp\n********\n********\n********\n********\nPPPPPPPP\nRNBQKBNR\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetBoardFENEdge() {
        System.out.println("setBoardFEN");
        Board instance = new Board();
        String FEN = "8/8/8/4p1K1/2k1P3/8/8/8";
        instance.setBoard(FEN, true);
        // TODO review the generated test code and remove the default call to fail.
        String expResult = "********\n********\n********\n****p*K*\n**k*P***\n********\n********\n********\n";
        String result = instance.toString();
        System.out.println(instance);
        assertEquals(expResult, result);
    }

   
    @Test
    public void testToString() {
        System.out.println("toString");
        Board instance = new Board();
        String expResult = "rnbqkbnr\npppppppp\n********\n********\n********\n********\nPPPPPPPP\nRNBQKBNR\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testToFEN() {
        System.out.println("toFEN");
        Board instance = new Board();
        String FEN = "8/8/8/4p1K1/2k1P3/8/8/8";
        instance.setBoard(FEN, true);
        String expResult = "8/8/8/4p1K1/2k1P3/8/8/8";
        String result = instance.toFEN();
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
  
    @Test
    public void testGetPieceAt() {
        System.out.println("getPieceAt");
        Board instance = new Board();
        assertTrue(instance.getPieceAt(1,1).equals("R"));
        assertTrue(instance.getPieceAt(3,1).equals(""));
        assertTrue(instance.getPieceAt(8,2).equals("n"));
    }

 {
    }
    
}
