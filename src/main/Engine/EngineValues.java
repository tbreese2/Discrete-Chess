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
    
    //flags for move types
    public static final byte PV_NODE = 0;
    public static final byte CUT_NODE = 1;
    public static final byte FORCED_ALL_NODE = 2;
    public static final byte ALL_NODE = 3;
    
    //standadardized max score
    //public because other functions may
    //find it useful
    public static final int MAX_MATE_SCORE = 16383;
    public static final int ONE_PLY = 1;
    public static final int MAX_PLY = 128;
    public static final int MAX_PVSEARCH_PLY = 235;
    public static final int MAX_INTERNAL_PLY = 255;
    public static final int TB_CURSED_SCORE = 1;
    public static final int TB_WIN_SCORE = 7937;
    public static final int MIN_MATE_SCORE  = 16383 - 255;
    
    //margin for razoring
    public static final int RAZOR_MARGIN = 190;
    
    //min and max short values
    public static final int SHORT_MIN = -32767;
    public static final int SHORT_MAX = 32767;
}
