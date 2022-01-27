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
        
        while(!nextLine.equals("uci")) {
            nextLine = inputReader.nextLine();
        }
        System.out.println("id name Discrete Chess");
        System.out.println("id author Tyler B");
        sendOptions();
        mainGameLoop();
    }
    
    public void mainGameLoop() {
        String nextLine = inputReader.nextLine();
        
        while(!nextLine.equals("quite")) {
            if(nextLine.equals("isready")){
               System.out.println("readyok"); 
            } else if(nextLine.equals("ucinewgame")) {
                engine.resetBoard();
            } else if(nextLine.split("\\s+")[0].equals("position")) {
                String[] args = nextLine.split("\\s+");
                if(args[1].equals("startpos")) {
                    engine.resetBoard();
                } else {
                    engine.setBoardFen(args[1]);
                }
                if(args[2].equals("moves")) {
                    for(int i = 3; i < args.length; i++) {
                        engine.doUCIMove(args[i]);
                    }
                }
                
            } else if(nextLine.split("\\s+")[0].equals("go")) {
                String[] args = nextLine.split("\\s+");
                if(args[1].equals("infinite")) {
                    engine.setMode("infinite");
                } else if (args[1].equals("movetime")) {
                    engine.setMode("movetime");
                    engine.setMoveTime(Double.valueOf(args[2]));
                }
               // String bestmove = engine.generateMove();
            }
            nextLine = inputReader.nextLine();
        }
        
    }
    
    public void sendOptions() {
        //non game loop
    }
    public static void main(String[] args) {
       UCI EngineCommunicaiton = new UCI();
    }
}
