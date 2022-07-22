/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE;

import Engine.MoveGen.*;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.KNIGHT;
import static Engine.EngineValues.BISHOP;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.KING;

import static Engine.EngineValues.WHITE;
import static Engine.EngineValues.BLACK;

public class InputsUtil {
    static float[][] inputs = new float[2][NNUEConstants.ft];

    //EFFECTS: returns an array, where arr[WHITE] is features
    //from white point of view and arr[BLACK] is features from blacks 
    //pov
    public static void setInputsArr(ChessBoard board) {
        //side to move perspective
        int perspective = WHITE;
        int kingSquare = Long.numberOfTrailingZeros(board.pieces[perspective][KING]);
        for(int c = WHITE; c <= BLACK; c++) {
            for (int type = PAWN; type <= QUEEN; type++) {
                long pieces = board.pieces[c][type];
                while (pieces != 0) {
                    int pieceSquare = Long.numberOfTrailingZeros(pieces);
                    inputs[perspective][getIndex(perspective, kingSquare, pieceSquare, type, c)] = 1;
                    pieces &= pieces - 1;
                }
            }
        }
        
        //black king perspective features
        //black pieces are seen as its own pieces
        //so colors must be switched
        perspective = BLACK;
        kingSquare = 63 - Long.numberOfTrailingZeros(board.pieces[perspective][KING]);
        for(int c = WHITE; c <= BLACK; c++) {
            for (int type = PAWN; type <= QUEEN; type++) {
                long pieces = board.pieces[c][type];
                while (pieces != 0) {
                    int pieceSquare = 63 - Long.numberOfTrailingZeros(pieces);
                    //need to get inverse color, as white pieces are now enemy pieces              
                    inputs[perspective][getIndex(perspective, kingSquare, pieceSquare, type, c)] = 1;
                    pieces &= pieces - 1;
                }
            }
        }
    }
    
    public static float[][] getFeatures() {
        return inputs;
    }

    public static void inputBitboard(int kingSquare) {

    }

    public static int getIndex(int perspective, int kingSquare, int pieceSQ, int pieceType, int pieceColor) {
        //adjust input as piece type starts at 1
        pieceType--;
        
        //black becomes white when perspective switches
        if(perspective == BLACK) pieceColor = pieceColor == WHITE ? 1 : 0;
        
        //now calculate indecies
        int index = pieceType * 2 + pieceColor;
        return ((kingSquare * 10) * 64) + (index * 64) + pieceSQ;
    }

    public static void doMove(int move, ChessBoard board) {

    }

    public static void undoMove(int move, ChessBoard board) {

    }
}
