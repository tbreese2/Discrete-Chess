/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.S12;

import Engine.MoveGen.*;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.KING;
import static Engine.EngineValues.WHITE;
import static Engine.EngineValues.BLACK;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import org.apache.commons.math3.analysis.function.Sigmoid;

public class InputsUtil {
    //inputs array, that will be operated in place on for the 
    //duration of the program
    private static INDArray inputs = Nd4j.zeros(2,NNUEConstants.FT);
    
    //sigmoid function
    private static Sigmoid sig = new Sigmoid();

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
                    inputs.get(NDArrayIndex.point(perspective), NDArrayIndex.point(getIndex(perspective, kingSquare, pieceSquare, type, c))).addi(1.0);
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
                    inputs.get(NDArrayIndex.point(perspective), NDArrayIndex.point(getIndex(perspective, kingSquare, pieceSquare, type, c))).addi(1.0);
                    pieces &= pieces - 1;
                }
            }
        }
    }
    
    public static INDArray getFeatures() {
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
        final int fromIndex = MoveUtil.getFromIndex(move);
        int toIndex = MoveUtil.getToIndex(move);
        long toMask = 1L << toIndex;
        final long fromToMask = (1L << fromIndex) ^ toMask;
        final int sourcePieceIndex = MoveUtil.getSourcePieceIndex(move);
        final int attackedPieceIndex = MoveUtil.getAttackedPieceIndex(move);
        
        //handled in 3 cats: normal, king move, promotion
        if(!MoveUtil.isPromotion(move) && !MoveUtil.isCastlingMove(move) && sourcePieceIndex != KING) {
            //get rid of originial piece from white perspective
            int kingSquare = Long.numberOfTrailingZeros(board.pieces[WHITE][KING]);
            inputs.get(NDArrayIndex.point(WHITE), NDArrayIndex.point(getIndex(WHITE, kingSquare, fromIndex, sourcePieceIndex, board.colorToMove))).subi(1.0);
            
            //get rid of original piece from blacks perspective
            kingSquare = 63 - Long.numberOfTrailingZeros(board.pieces[BLACK][KING]);
            inputs.get(NDArrayIndex.point(BLACK), NDArrayIndex.point(getIndex(BLACK, kingSquare, 63 - fromIndex, sourcePieceIndex, board.colorToMove))).subi(1.0);
        } else if(MoveUtil.isPromotion(move)) {
            
        } else {
            
        }
    }

    public static void undoMove(int move, ChessBoard board) {

    }
    
    public static double cpToWdl(float cp) {
        return sig.value(cp / NNUEConstants.SCALING_FACTOR);
    }
}
