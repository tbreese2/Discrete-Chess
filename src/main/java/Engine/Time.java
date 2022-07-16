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
    private byte depthI = -1;
    private int movestogoI = -1;
    private long movetimeI = -1;
    private long timeI = -1;
    private long incI = -1;
    
    //values needed by search algo
    private boolean timeset = false;
    private long starttime = -1;
    private byte depth = -1;
    private long stoptime = -1;
    
    //EFFECTS: given that move information is allready set
    //will go through and set the data up for the desired search
    //also starts the time managment, so should be called directly
    //before iterative deepening is called
    public void processMoveInformation() {
	starttime = System.currentTimeMillis();
	depth = depthI;

	if(timeI != -1) {
            timeset = true;
            timeI /= movestogoI;
            timeI -= 50;	
            stoptime = starttime + timeI + incI;
	}

	if(depth == -1)
		depth = EngineValues.MAX_PLY / 2;
    }
    
    public long getTimeLeft() {
        return stoptime - starttime;
    }
    
    public void decDepth() {
        depth--;
    }
    
    public long getDepthLeft() {
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
