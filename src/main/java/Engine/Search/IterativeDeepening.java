
package Engine.Search;

import Engine.Eval.Eval;
import Engine.MoveGen.MoveGen;
import Engine.MoveGen.ChessBoard;
import Engine.SearchTree;
import Engine.Time;
import Engine.EngineValues;

public class IterativeDeepening {
    
    private static long stopTime;
    private static byte maxDepth;
    
    //EFFECTS: returns true if time is still left
    public static boolean timeLeft() {
        if(System.currentTimeMillis() > stopTime)
            return false;
        return true;
    }
    
    public static int searchMain(ChessBoard board, SearchTree tree, Time tMan) {
        tMan.processMoveInformation();
        
        stopTime = tMan.getStopTime();
        maxDepth = tMan.getDepth();
        
        int bestMove = 0;
        
        for (byte depth = 1; depth <= maxDepth; depth++) {
            Negamax.table.voidTable();
            int value = Negamax.calcBestMoveNegamax(board, depth, tree, EngineValues.SHORT_MIN, EngineValues.SHORT_MAX);
            
        if(Negamax.isRunning == true) {
                bestMove = value;
            } else {
                break;
            }
        }
        
        return bestMove;
    }
}
