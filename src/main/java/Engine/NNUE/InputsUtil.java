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

    public static void setInputsArr(ChessBoard board) {
        //will use same perspective for differen't weights
        //white king perspective features
        int perspective = WHITE;
        int kingSquare = Long.numberOfTrailingZeros(board.pieces[perspective][KING]);
        for(int c = WHITE; c <= BLACK; c++) {
            for (int type = PAWN; type <= QUEEN; type++) {
                long pieces = board.pieces[c][type];
                while (pieces != 0) {
                    int pieceSquare = Long.numberOfTrailingZeros(pieces);
                    inputs[perspective][getIndex(kingSquare, pieceSquare, type, c)] = 1;
                    pieces &= pieces - 1;
                }
            }
        }
        
        //black king perspective features
        perspective = BLACK;
        kingSquare = 63 - Long.numberOfTrailingZeros(board.pieces[perspective][KING]);
        for(int c = WHITE; c <= BLACK; c++) {
            for (int type = PAWN; type <= QUEEN; type++) {
                long pieces = board.pieces[c][type];
                while (pieces != 0) {
                    int pieceSquare = 63 - Long.numberOfTrailingZeros(pieces);
                    inputs[perspective][getIndex(kingSquare, pieceSquare, type, c)] = 1;
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

    public static int getIndex(int kingSquare, int pieceSQ, int pieceType, int pieceColor) {
        //adjust input as piece type starts at 1
        pieceType--;
        int index = pieceType * 2 + pieceColor;
        return ((kingSquare * 10) * 64) + (index * 64) + pieceSQ;
    }

    public static void doMove(int move, ChessBoard board) {

    }

    public static void undoMove(int move, ChessBoard board) {

    }
}
