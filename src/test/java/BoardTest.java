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
        String expResult = "rkbqkbkr\npppppppp\n********\n********\n********\n********\npppppppp\nrkbqkbkr\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Board.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Board instance = new Board();
        String expResult = "rkbqkbkr\npppppppp\n********\n********\n********\n********\npppppppp\nrkbqkbkr\n";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class Board.
     */
    @Test
    public void testGetPieceAt() {
        System.out.println("toString");
        Board instance = new Board();
        assertTrue(instance.getPieceAt(1,1).equals("row"));
        assertTrue(instance.getPieceAt(3,1).equals(""));
        assertTrue(instance.getPieceAt(8,2).equals("bib"));
    }

 {
    }
    
}
