/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Testing;

import Engine.EngineValues;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.MoveGen.MoveGen;
import Engine.SearchTree;

/**
 *
 * @author tylerbreese
 */
class qPerfT {
    public static ChessBoard board = ChessBoardUtil.getNewCB();
    public static SearchTree tree = new SearchTree(2);
    
    public static void main(String[] args) {
       
    }
    
     //constants for accessing
    public static final int ALL = 0;
    public static final int EMPTY = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    public static final int WHITE = 0;
    public static final int BLACK = 1;   

    public static long qPerfT(final int depth, final ChessBoard boardTest) {
        tree.startPly();
        MoveGen.generateMoves(tree, boardTest);
        MoveGen.generateCaptures(tree, boardTest);

        long counter = 0;
        if (depth == 1) {
            while (tree.hasNext()) {
                if (boardTest.isLegal(tree.next())) {
                    counter++;
                }
            }
            tree.endPly();
            return counter;
        }

        while (tree.hasNext()) {
            final int move = tree.next();
            if (!boardTest.isLegal(move)) {
                continue;
            }
            boardTest.doMove(move);
            counter += qPerfT(depth - 1, boardTest);
            boardTest.undoMove(move);
        }

        tree.endPly();
        return counter;

    }

    public ChessBoard getEngineChessBoard() {
        return board;
    }
};