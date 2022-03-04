/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Search;

/**
 *
 * @author Tyler
 */
import java.util.*;
import java.lang.instrument.Instrumentation;

public class TTTable { 
    private Position hashtable[];
    private int tableSize;
    private int tableMask;
    
    public TTTable (final long mb) {
        final long maxNum = (mb * 1024 * 1024) / ObjectSizeFetcher.getObjectSize(new Position());
        
    }
    
    public Position transpositionTableLookup(final long key) {
        return new Position();
    }
    
    public void transpositionTableStore(long key, int move, short value, char depth, char type){
        
    }
}
