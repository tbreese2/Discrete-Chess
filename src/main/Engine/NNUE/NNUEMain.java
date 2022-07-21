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
    
    public static void evalPos(ChessBoard pos, NNUE net) {
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
        
        //do first feature evaluation
        Tensor ft_0 = net.ft.predict(new Tensor(features[playerToMove]));
        Tensor ft_1 = net.ft.predict(new Tensor(features[playerToMove]));
        ft_0.add(ft_1);
    }
    
    public static void trainOnData(String dataPath) {
        
    }
  
}
