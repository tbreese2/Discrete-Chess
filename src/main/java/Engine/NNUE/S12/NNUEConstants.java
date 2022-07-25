/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.S12;

/**
 *
 * @author tbreese
 */
public class NNUEConstants {
    //network arch
    public static final int ft = 41024;
    public static final int L_0 = 256;
    public static final int L_1 = 32;
    public static final int L_2 = 32;
    public static final int L_3 = 1;

    //net file
    public static final String ftNetFile = "ft.bin";
    public static final String mainNetFile = "main.bin";
    
    //training constants
    public static final String trainingPath = System.getProperty("user.dir") + "/training_data";
    public static final String plainExtension = "plain";
}
