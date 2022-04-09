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
     public long zobrist;
        public int move;
        public short value;
        public byte depth;
        public byte type;
        
        public void edit(long key, int move, short value, byte depth, byte type) {
            this.zobrist = key;
            this.move = move;
            this.value = value;
            this.depth = depth;
            this.type = type;
        }
        public Position() {
            this.zobrist = 0;
            this.move = 0;
            this.value = 0;
            this.depth = 0;
            this.type = 0;
        }
}
