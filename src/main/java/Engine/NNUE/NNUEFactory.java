/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;


import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.NeuralNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.TrainingEvent;
import deepnetts.net.train.TrainingListener;
import deepnetts.net.FeedForwardNetwork.*;
import deepnetts.net.FeedForwardNetwork.Builder;
import deepnetts.util.FileIO;

public class NNUEFactory {
    //L_0: Linear 41024->256*2
    //C_0: Clipped ReLu of size 256*2
    //L_1: Linear 256*2->32
    //C_1: Clipped ReLu of size 32
    //L_2: Linear 32->32
    //C_2: Clipped ReLu of size 32
    //L_3: Linear 32->1
    //C_3: Clipped ReLu of size 32
    //L_4: Linear 1
    
    private NeuralNetwork<?> network;  
    
    public static NeuralNetwork<?> getNewNetwork() {
        NeuralNetwork<?> network;
        
        network = FeedForwardNetwork.builder()
	                .addInputLayer(NNUEConstants.SIZE)
	                .addOutputLayer(1, ActivationType.RELU)
	                .hiddenActivationFunction(ActivationType.RELU)
	                .lossFunction(LossType.MEAN_SQUARED_ERROR)
	                .randomSeed(3244)
	                .build();
                
        return network;
    }
}
