/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.S12;

import Engine.NNUE.CReLu;
import Engine.NNUE.S12.NNUEConstants;
import Engine.NNUE.S12.NNUE;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.*;
import java.io.File;
import java.util.Random;

public class NNUEFactory {
    public static String networkExportTag = getSaltString() + "_";
    
    //activation function
    public static final CReLu crelu = new CReLu();
    
    public static NNUE getNewNetwork() {
        NNUE net = new NNUE();

        final long seed = 6;

        MultiLayerConfiguration ft_c = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .activation(crelu)
            .weightInit(WeightInit.XAVIER)
            .list()
            .layer(new DenseLayer.Builder().nIn(NNUEConstants.FT).nOut(NNUEConstants.L_0).activation(crelu)
                .build())
            .build();
        
        MultiLayerConfiguration main_c = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .activation(crelu)
            .weightInit(WeightInit.XAVIER)
            .list()
            .layer(new DenseLayer.Builder().nIn(NNUEConstants.L_0*2).nOut(NNUEConstants.L_1).activation(crelu)
                .build())
            .layer(new DenseLayer.Builder().nIn(NNUEConstants.L_1).nOut(NNUEConstants.L_2).activation(crelu)
                .build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(NNUEConstants.L_2).nOut(NNUEConstants.L_3).activation(crelu)
                .build())
            .build();
        //run the model
        
        //build feature networks
        net.ft = new MultiLayerNetwork(ft_c);
        net.ft.init();
        
        net.main = new MultiLayerNetwork(main_c);
        net.main.init();
                
        return net;
    }
    
    public static void exportNet(NNUE network) {
        try {
            network.ft.save(new File(networkExportTag + NNUEConstants.FT_NET_FILE));
            network.main.save(new File(networkExportTag + NNUEConstants.MAIN_NET_FILE));
	} catch (IOException e) {
            e.printStackTrace();
	}

    }
    
    public static NNUE importNet() {
        NNUE nn = new NNUE(); 
        
        try {
           nn.ft = MultiLayerNetwork.load(new File(NNUEConstants.FT_NET_FILE), true);
           nn.main = MultiLayerNetwork.load(new File(NNUEConstants.MAIN_NET_FILE), true); 
        } catch (IOException e) {
            System.out.println("Couldn't import net");
        }

        return nn;
    }
    
    protected static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
