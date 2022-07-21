/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;


import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.util.FileIO;

import java.io.*;
import java.io.File;
import java.util.Random;

public class NNUEFactory {
    public static String networkExportTag = getSaltString() + "_";
    
    public static NNUE getNewNetwork() {
        NNUE net = new NNUE();
        
        //build feature networks
        net.ft = FeedForwardNetwork.builder()
	                .addInputLayer(NNUEConstants.ft)
	                .addFullyConnectedLayer(NNUEConstants.L_0,ActivationType.RELU)
	                .randomSeed(3244)
	                .build();
        
        net.main = FeedForwardNetwork.builder()
	                .addInputLayer(NNUEConstants.L_0*2)
                        .addHiddenFullyConnectedLayers(NNUEConstants.L_1, NNUEConstants.L_2)
                        .addOutputLayer(NNUEConstants.L_3,ActivationType.RELU)
                        .hiddenActivationFunction(ActivationType.RELU)
	                .randomSeed(3244)
	                .build();
                
        return net;
    }
    
    public static void exportNet(NNUE network) {
        try {
            FileIO.writeToFile(network.ft, networkExportTag + NNUEConstants.ftNetFile);
            FileIO.writeToFile(network.main, networkExportTag + NNUEConstants.ftNetFile);
        } catch(IOException e) {
            System.out.println("Couldn't export net");
        }
    }
    
    public static NNUE importNet() {
        NNUE nn = new NNUE(); 
        try {
            try {
                nn.ft = FileIO.createFromFile(new File(NNUEConstants.ftNetFile));
                nn.main = FileIO.createFromFile(new File(NNUEConstants.ftNetFile));
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found for fetching network");
            }
            
        } catch(IOException e) {
            System.out.println("Couldn't export net");
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
