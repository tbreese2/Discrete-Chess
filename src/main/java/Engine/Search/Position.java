/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Search;

/**
 *
 * @author Tyler
 */
public class Position {
    public int zobrist;
    public int move;
    public int value;
    public byte depth;
    public byte type;
    public short age;
 
    public void edit(final int z, final int move, final int value, final byte depth, final byte type, final short age) {
        this.zobrist = z;
        this.move = move;
        this.value = value;
        this.depth = depth;
        this.type = type;
        this.age = age;
    }
    
    public Position() {
        this.zobrist = 0;
        this.move = 0;
        this.value = 0;
        this.depth = 0;
        this.type = 0;
    }
}
