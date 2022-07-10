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
import Engine.EngineValues;

public class TTTable { 
    private Position hashtable[];
    private int tableSize;
    private long tableMask;
    public byte age = -128;
    
    public TTTable (final long mb) {
        
        try {
            int size = 4*SizeOf.serialize(int.class) + 3*SizeOf.serialize(byte.class);
            final long maxNum = (mb * 1024 * 1024) / size;
            tableSize = 1;
            while (tableSize <= maxNum) {
                tableSize *= 2;
            }
            tableSize /= 2;
            tableMask = tableSize - 1;
            age = 0;
            hashtable = new Position[tableSize];
            voidTable();
            
        } catch(Exception e) {
            
        }
    }
    
    public int tableSize() {
        return tableSize;
    }
    
    public void voidTable() {
        for(int i = 0; i < hashtable.length; i++) {
            hashtable[i] = new Position();
        }
    }
    
    public double percentFull() {
        double used = 0;
        
        for (int i = 0; i < 100; i++) {
            if (hashtable[i].zobrist != 0) {
                used++;
            }
        }

        return used / 100;
    }
    
    public Position transpositionTableLookup(final long key) {
        final long iKey = key & tableMask;
        return hashtable[(int)iKey];
    }
    
    public boolean transpositionTableStore(final long zobrist, final int score, final int move, final byte nodeType, final byte depth, final int eval){
        final long index = zobrist & tableMask;
        Position ent = hashtable[(int)index];
        final int key = (int)(zobrist >> 32);
        
        if (ent.zobrist == 0) {
            ent.edit(key, move, depth, nodeType, age, eval, score);
            hashtable[(int)index] = ent;
            return true;
        } else {
            if (ent.age != age || ent.type == Negamax.PV_NODE || (ent.type != Negamax.PV_NODE && ent.type <= depth) || (ent.zobrist == key && ent.depth <= depth * 2))  {
                ent.edit(key, move, depth, nodeType, age, eval, score);
                hashtable[(int)index] = ent;
                return true;
            }
        }
        return false;
    }
    
    public void nextAge() {
        if(age == 127) {
            age = -128;
        } else {
            age++;
        }
    }
}
