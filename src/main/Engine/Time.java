/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine;

/**
 *
 * @author tyler
 */
public class Time {
    //values needed to calculate time for search algo
    private byte depthI;
    private int movestogoI;
    private long movetimeI;
    private long timeI;
    private long incI;
    
    //values needed by search algo
    private long starttime = -1;
    private byte depth = -1;
    private long stoptime = -1;
    
    //EFFECTS: given that move information is allready set
    //will go through and set the data up for the desired search
    //also starts the time managment, so should be called directly
    //before iterative deepening is called
    public void processMoveInformation() {
        if(movetimeI != -1) {
            timeI = movetimeI;
            movestogoI = 1;
	}
        
	starttime = System.currentTimeMillis();
	depth = depthI;

	if(timeI != -1) {
            timeI /= movestogoI;
            timeI -= 50;	
            stoptime = starttime + timeI + incI;
	}

	if(depth == -1)
		depth = EngineValues.MAX_PLY / 2;
        
        if(stoptime == -1) 
            stoptime = Long.MAX_VALUE;
    }
    
    public void resetTimeMan() {
        depthI = -1;
        movestogoI = 35;
        movetimeI = -1;
        timeI = -1;
        incI = 0;
        starttime = -1;
        depth = -1;
        stoptime = -1;
    }
    
    public long getStopTime() {
        return stoptime;
    }
    
    public byte getDepth() {
        return depth;
    }
    
    public void setDepth(byte d) {
        depthI = d;
    }
    
    public void setMovesToGo(int m) {
        movestogoI = m;
    }
    
    public void setMoveTime(long t) {
        movetimeI = t;
    }
    
    public void setTimeOnClock(long t) {
        timeI = t;
    }
    
    public void setIncTime(long t) {
        incI = t;
    }
}
