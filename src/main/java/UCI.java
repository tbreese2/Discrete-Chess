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

//Main UCI interface
//Set as main class for UCI compatible compiled engine
public class UCI {
    //engine and input reading classes
    EngineMain engine;
    Scanner inputReader;
    
    //MODIFIES: this
    //EFFECTS: creates a new UCI interface unit
    public UCI() {
        //initilize engine and scannenr
        inputReader = new Scanner(System.in);
        engine = new EngineMain();
    }
    
    //EFFECTS: reads in uci command
    private void uciInitialization() {
        String nextLine = inputReader.nextLine();
        
        //check for uci command
        while(!nextLine.equals("uci")) {
            nextLine = inputReader.nextLine();
        }
        
        //engine and author infor
        System.out.println("id name Discrete Chess");
        System.out.println("id author Tyler B");
        System.out.println("option name Hash type spin default 64 min 4 max " + UCIOptions.MAX_HASH);
        System.out.println("uciok");
    }
    
    //MODIFIES: this
    //EFFECTS: reads moves until quit command is specified
    private void run() {
        String nextLine = inputReader.nextLine();
        
        //check if quitcommand is given
        while(!nextLine.equals("quit")) {
            
            //don't run until board compatible is ready
            if(nextLine.equals("isready")){
               System.out.println("readyok"); 
            } 
            
            //resents board to FIDE default
            else if(nextLine.equals("ucinewgame")) {
                engine.resetBoard();
            } 
            
            //new inputed position check
            else if(nextLine.split("\\s+")[0].equals("position")) {
                String[] args = nextLine.split("\\s+");
                if(args[1].equals("startpos")) {
                    engine.resetBoard();
                } else {
                    engine.setBoardFen(args[1]);
                }
                if(args.length > 2) {
                    if(args[2].equals("moves")) {
                        for(int i = 3; i < args.length; i++) {
                            engine.doUCIMove(args[i]);
                        }
                    }
                }
            } 
            
            //check for move request
            else if(nextLine.split("\\s+")[0].equals("go")) {
                String[] args = nextLine.split("\\s+");
                if(args[1].equals("infinite")) {
                    engine.setMode("infinite");
                } else if (args[1].equals("movetime")) {
                    engine.setMode("movetime");
                    engine.setMoveTime(Double.valueOf(args[2]));
                }
                System.out.println("bestmove " + engine.generateMove());
            }
            nextLine = inputReader.nextLine();
        }
        
    }
    
    //MODIFIES: board stored in engine main
    //EFFECTS: applys moves in line to board stored by engine
    private void ParsePosition(String line) {
        
    }
    
    //MODIFIES: this
    //EFFECTS: handles all none time related uci inputs
    //and also serves as main loop for engine
    private void uciLoop() {
        while(true) {
            String nextLine = inputReader.nextLine();
        
            if(nextLine.substring(0, 1).equals('\n')) continue;
            
            //don't run until board compatible is ready
            else if(nextLine.equals("isready")){
               System.out.println("readyok"); 
            }
            
            else if(nextLine.split("\\s+")[0].equals("position")) {
                ParsePosition(nextLine);
            }
            
            else if(nextLine.equals("ucinewgame")) {
                
            }
            
        }
    }
    
    
    private void sendOptions() {
        //non game loop
    }
    
    //EFFECTS: creates engine opject,
    //reads options, then runs
    public static void main(String[] args) {
       UCI EngineCommunicaiton = new UCI();
       
       //initilizes UCI communication
       EngineCommunicaiton.uciInitialization();
    }
}
