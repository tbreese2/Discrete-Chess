/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Engine.Eval;

import Engine.UCI;
import Engine.Search.Negamax;
import Engine.EngineValues;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.SearchTree;
import Engine.UCI;
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
//        System.out.println("bestMove");
//        //new EngineBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
//        //rnbqkbnr/ppppp1p1/5p1p/4P3/3P4/3B1N2/PPP2PPP/RNBQK2R b KQq - 0 5
//        ChessBoard instance = ChessBoardUtil.getNewCB("rnbqkbnr/p1pppppp/8/1p6/6P1/8/PPPPPP1P/RNBQKBNR w KQkq b6 0 2");
//        System.out.println(ChessBoardUtil.toString(instance, false));
//        byte depth = 6;
//        SearchTree tree = new SearchTree();
//        int result = Negamax.calcBestMoveNegamax(instance, depth, tree, EngineValues.SHORT_MIN, EngineValues.SHORT_MAX);
//        System.out.println(ChessBoardUtil.toString(instance, false));
//        instance.doMove(result);
//        System.out.println(ChessBoardUtil.toString(instance, false));
//        // TODO review the generated test code and remove the default call to fail.
        UCI instance = new UCI();
        
        instance.uciProc("ucinewgame");
        instance.uciProc("isready");

        instance.uciProc("position startpos moves f2f4");
    
    }


}
