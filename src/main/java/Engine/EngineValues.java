/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine;

import Engine.MoveGen.CheckUtil;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.MoveGen.ChessConstants;
import Engine.MoveGen.MoveGen;
import Engine.MoveGen.MoveUtil;
import java.lang.Math;

//helper class
//basically stored move constants so they can be standardized
//across all classes
public class EngineValues {

    //constants for piece type
    public static final int ALL = 0;
    public static final int EMPTY = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    //constants for playing color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
}
