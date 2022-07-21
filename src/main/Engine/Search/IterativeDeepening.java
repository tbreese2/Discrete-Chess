
package Engine.Search;

import Engine.Eval.Eval;
import Engine.MoveGen.MoveGen;
import Engine.MoveGen.ChessBoard;
import Engine.*;
import static Engine.EngineValues.SHORT_MIN;

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
        
        Negamax.startingColor = board.colorToMove;
        
        stopTime = tMan.getStopTime();
        maxDepth = tMan.getDepth();
        
        Negamax.isRunning = true;
        
        int bestMove = 0;
        for (byte depth = 1; depth <= maxDepth; depth++) {
            if(Negamax.isRunning) {
                Negamax.table.voidTable();
                
                bestMove = Negamax.calcBestMoveNegamax(board, depth, tree, EngineValues.SHORT_MIN, EngineValues.SHORT_MAX);

                if(depth > 1) 
                    tree.setPV(bestMove);
            } else {
                break;
            }
        }
        
        return bestMove;
    }
}
