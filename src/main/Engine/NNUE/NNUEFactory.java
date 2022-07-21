/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;


import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;

import java.io.*;

public class NNUEFactory {
    
    
    public static NNUE getNewNetwork() {
        NNUE net = new NNUE();
        
        //build feature networks
        net.ft = FeedForwardNetwork.builder()
	                .addInputLayer(NNUEConstants.ft)
	                .addOutputLayer(NNUEConstants.L_0,ActivationType.RELU)
	                .randomSeed(3244)
	                .build();
        
        net.main = FeedForwardNetwork.builder()
	                .addInputLayer(NNUEConstants.L_0*2)
	                .addFullyConnectedLayer(NNUEConstants.L_1,ActivationType.RELU)
                        .addFullyConnectedLayer(NNUEConstants.L_2,ActivationType.RELU)
                        .addOutputLayer(NNUEConstants.L_3,ActivationType.RELU)
	                .randomSeed(3244)
	                .build();
                
        return net;
    }
    
    public static void exportNet(NNUE network) {
        try {
            FileOutputStream fos = new FileOutputStream(NNUEConstants.netFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(network);
            oos.close();
            fos.close();
        } catch(IOException e) {
            System.out.println("Couldn't export net");
        }
    }
    
    public static NNUE fetchNet() {
        FileInputStream inFile;
        ObjectInputStream inStream;
        NNUE nn = new NNUE(); 
        try {
            inFile = new FileInputStream(NNUEConstants.netFile);
            inStream = new ObjectInputStream(inFile);     
            try {
                nn = (NNUE)inStream.readObject();  
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found in net file");
            }
        } catch(IOException e) {
            System.out.println("Couldn't export net");
        }
        return nn;
    }
}
