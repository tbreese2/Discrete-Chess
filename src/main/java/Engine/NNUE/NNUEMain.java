/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import org.apache.commons.lang3.*;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import Engine.MoveGen.*;
import Engine.*;

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
    
//    public static INDArray evalPos(ChessBoard pos, NNUE net) {
//        //float arrs for player to move and other
//        int playerToMove;
//        int playerOther;
//        
//        //TODO: add incremental updating so this call
//        //does not have to be made
//        InputsUtil.setInputsArr(pos);
//        INDArray features = InputsUtil.getFeatures();
//        
//        //get color to move
//        int color = pos.colorToMove;
//        
//        if(color == EngineValues.WHITE) {
//            playerToMove = 0;
//            playerOther = 1;
//        } else {
//            playerToMove = 1;
//            playerOther = 0;
//        }
//        
//        //concatanate arrays and return network prediction
//        return net.main.predict(
//            new Tensor(
//                ArrayUtils.addAll(
//                    net.ft.predict(new Tensor(features[playerToMove])).getValues(), 
//                    net.ft.predict(new Tensor(features[playerOther])).getValues()
//                )
//            )
//        );
//    }
    
    public static void trainOnData(String dataPath) {
        
    }
  
}
