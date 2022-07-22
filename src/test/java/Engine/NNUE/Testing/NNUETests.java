/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.Testing;


import Engine.NNUE.*;
import org.junit.*;
import java.util.*;
import Engine.MoveGen.*;

public class NNUETests {
    public NNUETests() {
    }
    
    
    public static void setUpClass() {
    }
    
    
    public static void tearDownClass() {
    }

    /**
     * Test of bestMove method, of class MinMax.
     */
    @Test
    public void nnueTestHalfKP() {
        
        //now we will test halfkp
        ChessBoard instance = ChessBoardUtil.getNewCB("r1bqk1nr/pp2p2p/2np1p2/2p5/5P2/2NP4/PPP1P1PP/R1BQK1NR w KQkq - 0 1");
        InputsUtil.setInputsArr(instance);
        float[][] temp = InputsUtil.getFeatures();
        
        int[] values = {1928,1929,1931,1933,1934,1935,1940,1946, 2021, 2026, 2028, 2032, 2035, 2038, 2039, 2049, 2069, 2169, 2157, 2181, 2301, 2304, 2311, 2424, 2431, 2436, 2556};
        
        for(int i = 0; i < values.length; i++) {
            if(temp[0][values[i]] != 1) {
                System.out.println(values[i]);
            }
        }
        
        int[] valuesB = {2568,2569,2572,2575,2579,2581,2586,2679,2678,2676,2674,2673,2672,2667,2661,2694,2706,2814,2794,2818,2938,2944,2951,3071,3064,3075,3195};
        
        for(int i = 0; i < valuesB.length; i++) {
            if(temp[1][valuesB[i]] != 1) {
                System.out.println(valuesB[i]);
            }
        }
        
        System.out.println(values.length == valuesB.length);
    }
}
