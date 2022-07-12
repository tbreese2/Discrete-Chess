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
    
    //MODIFIES: board stored in engine main
    //EFFECTS: applys moves in line to board stored by engine
    private void ParsePosition(String line) {
        String[] args = line.split("\\s+");
        int i = 1;
        if(args[1].equals("startpos")) {
            engine.resetBoard();
        } else {
            String fen = "";
            
            while(!args[i].equals("moves") && args.length > i) {
                fen += args[i];
                i++;
            }
            
            engine.setBoardFen(fen);
            i--;
        }
        i++;
        if(args.length > i) {
            if(args[i].equals("moves")) {
                for(int j = i + 1; j < args.length; j++) {
                    engine.doUCIMove(args[j]);
                }
            }
        }
    }
    
    private void ParseGo(String line) {
        
    }
    
    //MODIFIES: this
    //EFFECTS: handles all none time related uci inputs
    //and also serves as main loop for engine
    private void uciLoop() {
        while(true) {
            String nextLine = inputReader.nextLine();
        
            if(nextLine.substring(0, 1).equals('\n')) continue;
            
            //don't run until board compatible is ready
            else if(nextLine.substring(0,7).equals("isready")){
               System.out.println("readyok"); 
            }
            
            else if(nextLine.substring(0,8).equals("position")) {
                ParsePosition(nextLine);
            }
            
            else if(nextLine.substring(0,10).equals("ucinewgame")) {
                engine.resetBoard();
            }
            
            else if (nextLine.substring(0,2).equals("go")) {
                System.out.println("Seen Go..");
                ParseGo(nextLine);
            }
            
            else if (nextLine.substring(0,4).equals("quit")) {
                break;
            }
            
            else if (nextLine.substring(0,3).equals("uci")) {
                System.out.println("id name Discrete Chess");
                System.out.println("id author Tyler B");
                System.out.println("uciok");
            }
            
            else if (nextLine.substring(0,3).equals("debug")) {
                break;
            }
            
            else if (nextLine.substring(0,26).equals("setoption name Hash value ")) {
                UCIOptions.hashSize = Integer.parseInt(nextLine.substring(26));
                if(UCIOptions.hashSize < 4) UCIOptions.hashSize = 4;
		if(UCIOptions.hashSize > UCIOptions.MAX_HASH) UCIOptions.hashSize = UCIOptions.MAX_HASH;
		System.out.println("Set Hash to " + UCIOptions.hashSize + " MB");
            }
            
        }
    }
    
    //EFFECTS: creates engine opject,
    //reads options, then runs
    public static void main(String[] args) {
       UCI EngineCommunicaiton = new UCI();
       
       //initilizes UCI communication
       EngineCommunicaiton.uciInitialization();
    }
}
