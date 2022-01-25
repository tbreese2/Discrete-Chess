/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
import java.util.*;
import Engine.EngineMain;

public class UCI {
    EngineMain engine;
    Scanner inputReader;
    
    public UCI() {
        inputReader = new Scanner(System.in);
        engine = new EngineMain();
        uciInitialization();
    }
 
    public void uciInitialization() {
        String nextLine = inputReader.nextLine();
        
        if(nextLine.equals("uci")) {
            System.out.println("id name Discrete Chess");
            System.out.println("id author Tyler B");
        }
    }
    
    public static void main(String[] args) {
       UCI EngineCommunicaiton = new UCI();
    }
}
