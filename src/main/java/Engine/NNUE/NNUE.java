/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import deepnetts.net.NeuralNetwork;

/**
 *
 * @author tbreese
 */
public class NNUE {
    //Feature Network:
    //L_0: Linear 41024->256
    //C_0: Clipped ReLu of size 256
    public NeuralNetwork<?> ft;
    
    
    //Main Network:
    //L_1: Linear 256*2->32
    //C_1: Clipped ReLu of size 32
    //L_2: Linear 32->32
    //C_2: Clipped ReLu of size 32
    //L_3: Linear 32->1
    //C_3: Clipped ReLu of size 32
    //L_4: Linear 1
    public NeuralNetwork<?> main;
}
