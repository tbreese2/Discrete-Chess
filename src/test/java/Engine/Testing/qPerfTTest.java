/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine.Testing;

import Engine.MoveGen.ChessBoard;
import static Engine.Testing.qPerfT.qPerfT;
import org.junit.*;
import Engine.MoveGen.*;

/**
 *
 * @author Tyler
 */
public class qPerfTTest {
    
    public qPerfTTest() {
    }
    

    /**
     * Test of main method, of class qPerfT.
     */
    //@Test
    public void testMain() {
         System.out.println("generateAllMoves");
        ChessBoard board = ChessBoardUtil.getNewCB("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");
        final long startTime = System.currentTimeMillis();
        final long count = qPerfT(5, board);
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime) / (double) 1000 + " seconds");
        System.out.println("Total moves generated: " + count);
        double nodesPerSecond = count / ((endTime - startTime) / (double) 1000);
        System.out.println("Total nodes per second: " + String.format("%.0f", nodesPerSecond));
    }

}
