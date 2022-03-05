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
    private long tableMask;
    private byte age;
    
    public TTTable (final long mb) {
        final long maxNum = (mb * 1024 * 1024) / ObjectSizeFetcher.getObjectSize(new Position());
        tableSize = 1;
        while (tableSize <= maxNum) {
            tableSize *= 2;
        }

        tableSize /= 2;
        tableMask = tableSize - 1;
        age = 0;
        hashtable = new Position[tableSize];
        voidTable();
    }
    
    public void voidTable() {
        for(int i = 0; i < hashtable.length; i++) {
            hashtable[i] = new Position();
        }
    }
    
    public Position transpositionTableLookup(final long key) {
        final long iKey = key & tableMask;
        return hashtable[(int)iKey];
    }
    
    public void transpositionTableStore(final long key, final int move, final short value, final byte depth, final byte type){
        final long iKey = key & tableMask;
        Position ent = hashtable[(int)iKey];
        final int lKey = (int)(key >> 32);
        if (ent.zobrist == 0) {
            
        }
    }
}
