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
    public byte depth;
    public byte type;
    public byte age;
    public int eval;
    public int score;
 
    public void edit(final int z, final int move, final byte depth, final byte type, final byte age, final int eval, final int score) {
        this.zobrist = z;
        this.move = move;
        this.depth = depth;
        this.type = type;
        this.age = age;
        this.eval = eval;
        this.score = score;
    }
}
