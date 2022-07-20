/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import Engine.MoveGen.*;
import deepnetts.util.Tensor;

/**
 *
 * @author tbreese
 */
public class NNUEMain {
    //important nnue values
    //using side to move first, then other side to move
    //using same wieghts for same perspective and will use rotate on board
    //to adjust
    //we will be using deep java library for now, but later we may switch to another lib
    //like deeplearning for java, im using deepnetts for now as it is simpiler
    String nnueBin = "bin/nnue.bin";
    
    public static void evalPos(ChessBoard pos) {
        
    }
    
    public static void trainOnData(String dataPath) {
        
    }
    
    public static void/*Tensor*/ createInputs(ChessBoard board) {
        return;
    }
}
