/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.S12;

public class NNUEConstants {
    //network arch
    public static final int FT = 41024;
    public static final int L_0 = 256;
    public static final int L_1 = 32;
    public static final int L_2 = 32;
    public static final int L_3 = 1;

    //net file
    public static final String FT_NET_FILE = "ft.bin";
    public static final String MAIN_NET_FILE = "main.bin";
    
    //training constants
    public static final String TRAINING_PATH = System.getProperty("user.dir") + "/training_data";
    public static final String PLAIN_EXTENSION = "plain";
    
    //space conversion
    public static final int SCALING_FACTOR = 410;
}
