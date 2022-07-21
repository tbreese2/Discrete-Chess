/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.Testing;


import Engine.NNUE.*;
import org.junit.*;
import java.util.*;

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
    public void nnueTestFunc() {
        NNUE temp = NNUEFactory.importNet();
        
        System.out.println(temp.main.summary());
        System.out.println(temp.ft.summary());
    }
}
