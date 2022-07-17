package Engine;

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
import Engine.MoveGen.ChessBoardUtil;
import Engine.*;

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
        
        //start main uci loop
        uciLoop();
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
            
            while(args.length > i && !args[i].equals("moves")) {
                fen += args[i] + " ";
                i++;
            }
            fen = fen.substring(0, fen.length() - 1);
            System.out.println(fen);
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
    
    public void ParseGo(String line) {
        String[] args = line.split("\\s+");
        if(args[1].equals("infinite"));
        
        if(Arrays.asList(args).indexOf("binc") != -1 && EngineMain.board.colorToMove == EngineValues.BLACK) {
            EngineMain.tMan.setIncTime(Integer.parseInt(args[Arrays.asList(args).indexOf("binc") + 1]));
        }
        
        if(Arrays.asList(args).indexOf("winc") != -1 && EngineMain.board.colorToMove == EngineValues.WHITE) {
            EngineMain.tMan.setIncTime(Integer.parseInt(args[Arrays.asList(args).indexOf("winc") + 1]));
        }
        
        if(Arrays.asList(args).indexOf("wtime") != -1 && EngineMain.board.colorToMove == EngineValues.WHITE) {
            EngineMain.tMan.setTimeOnClock(Integer.parseInt(args[Arrays.asList(args).indexOf("wtime") + 1]));
        }
        
        if(Arrays.asList(args).indexOf("btime") != -1 && EngineMain.board.colorToMove == EngineValues.BLACK) {
            EngineMain.tMan.setTimeOnClock(Integer.parseInt(args[Arrays.asList(args).indexOf("btime") + 1]));
        }
        
	if(Arrays.asList(args).indexOf("movestogo") != -1) {
            EngineMain.tMan.setMovesToGo(Integer.parseInt(args[Arrays.asList(args).indexOf("movestogo") + 1]));
        }
	
        if(Arrays.asList(args).indexOf("movetime") != -1) {
            EngineMain.tMan.setMoveTime(Integer.parseInt(args[Arrays.asList(args).indexOf("movetime") + 1]));
        }
        
        if(Arrays.asList(args).indexOf("depth") != -1) {
            System.out.println(Arrays.asList(args).indexOf("depth") + 1);
            EngineMain.tMan.setDepth((byte)Integer.parseInt(args[Arrays.asList(args).indexOf("depth") + 1]));
        }
        
        System.out.println("bestmove " + engine.generateMove());
    }
    
    //MODIFIES: this
    //EFFECTS: handles all none time related uci inputs
    //and also serves as main loop for engine
    public void uciLoop() {
        while(true) {
            String nextLine = inputReader.nextLine();
            if(nextLine.substring(0, 1).equals('\n')) continue;
            
            //don't run until board compatible is ready
            else if(nextLine.substring(0,7).equals("isready")){
               System.out.println("readyok"); 
            }
            
            else if(nextLine.substring(0,8).equals("position")) {
                ParsePosition(nextLine);
                System.out.println("Set board to: " + ChessBoardUtil.toString(EngineMain.board, false));
            }
            
            else if(nextLine.substring(0,10).equals("ucinewgame")) {
                engine.resetBoard();
                System.out.println("Board and Transposition tables reset");
            }
            
            else if (nextLine.substring(0,2).equals("go")) {
                System.out.println("Seen Go..");
                ParseGo(nextLine);
            }
            
            else if (nextLine.substring(0,4).equals("quit")) {
                System.out.println("quiting");
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
                System.out.println(UCIOptions.hashSize);
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
