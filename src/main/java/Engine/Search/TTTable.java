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

public class TTTable {
    private final int TABLESIZE = 8388608;
    
    public static class entry {
        public long key;
        public int move;
        public short value;
        public char depth;
        public char type;
        
        public entry(long key, int move, short value, char depth, char type) {
            this.key = key;
            this.move = move;
            this.value = value;
            this.depth = depth;
            this.type = type;
        }
        public entry() {
            this.key = 0;
            this.move = 0;
            this.value = 0;
            this.depth = '0';
            this.type = '0';
        }
    }
    
    private entry hashtable[];
    
    public TTTable () {
        hashtable = new entry[TABLESIZE];
    }
    
    public entry transpositionTableLookup(final long key) {
        return new entry();
    }
    
    public void transpositionTableStore(long key, int move, short value, char depth, char type){
        
    }
}
