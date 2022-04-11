/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine.Eval;

import Engine.Search.Negamax;
import Engine.EngineValues;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.SearchTree;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author tylerbreese
 */
public class MinMaxTest {
    
    public MinMaxTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Test of bestMove method, of class MinMax.
     */
    @Test
    public void testBestMove() {
        System.out.println("bestMove");
        //new EngineBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        //rnbqkbnr/ppppp1p1/5p1p/4P3/3P4/3B1N2/PPP2PPP/RNBQK2R b KQq - 0 5
        ChessBoard instance = ChessBoardUtil.getNewCB("rnbqkbnr/ppppp1p1/5p1p/4P3/3P4/3B1N2/PPP2PPP/RNBQK2R b KQq - 0 5");
        System.out.println(ChessBoardUtil.toString(instance, false));
        int depth = 5;
        SearchTree tree = new SearchTree();
        int result = Negamax.calcBestMoveNegamax(instance, depth, tree, -Negamax.maxScore, Negamax.maxScore);
        System.out.println(ChessBoardUtil.toString(instance, false));
        instance.doMove(result);
        System.out.println(ChessBoardUtil.toString(instance, false));
        // TODO review the generated test code and remove the default call to fail.

    }


}
