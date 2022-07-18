/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;
import Engine.MoveGen.*;

public class InputsUtil {
    public static final int SIZE = 768;
    public static final int KING = 0;
    public static final int PAWNS = 64;
    public static final int KNIGHTS = 128;
    public static final int BISHOP = 192;
    public static final int ROOK = 256;
    public static final int QUEEN = 320;
    
    static float[] inputs = new float[SIZE];
    
    public static void setInputs(ChessBoard board) {
        
    }
    
    public static float[] getInputs() {
        return inputs;
    }
    
    public static void doMove(int move, ChessBoard board) {
        
    }
    
    public static void undoMove(int move, ChessBoard board) {
        
    }
}
