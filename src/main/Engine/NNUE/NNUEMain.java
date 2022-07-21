/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import Engine.MoveGen.*;
import deepnetts.util.Tensor;
import Engine.*;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.NeuralNetwork;
import java.util.*;
import org.apache.commons.lang3.*;

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
    
    private static float[][] concat;
    
    public static Tensor evalPos(ChessBoard pos, NNUE net) {
        //float arrs for player to move and other
        int playerToMove;
        int playerOther;
        
        //TODO: add incremental updating so this call
        //does not have to be made
        InputsUtil.setInputsArr(pos);
        float[][] features = InputsUtil.getFeatures();
        
        //get color to move
        int color = pos.colorToMove;
        
        if(color == EngineValues.WHITE) {
            playerToMove = 0;
            playerOther = 1;
        } else {
            playerToMove = 1;
            playerOther = 0;
        }
        
        //concatanate arrays and return network prediction
        return net.main.predict(
            new Tensor(
                ArrayUtils.addAll(
                    net.ft.predict(new Tensor(features[playerToMove])).getValues(), 
                    net.ft.predict(new Tensor(features[playerOther])).getValues()
                )
            )
        );
    }
    
    public static void trainOnData(String dataPath) {
        
    }
  
}
