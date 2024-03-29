/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import Engine.NNUE.S12.NNUE;
import Engine.NNUE.S12.InputsUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
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

    public static INDArray evalPos(ChessBoard pos, NNUE net) {
        //float arrs for player to move and other
        int playerToMove;
        int playerOther;
        
        //TODO: add incremental updating so this call
        //does not have to be made
        InputsUtil.setInputsArr(pos);
        INDArray features = InputsUtil.getFeatures();
        
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
        INDArray output = net.ft.output(features.getRow(playerToMove, true));
        INDArray output1 = net.ft.output(features.getRow(playerOther, true));

        return net.main.output(Nd4j.concat(1,output, output1));
    }
    
    public static void trainOnData(String dataPath) {
        
    }
  
}
