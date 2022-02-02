/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Search;

import Engine.Eval.Eval;
import Engine.MoveGen.MoveGen;
import Engine.MoveGen.ChessBoard;
import Engine.SearchTree;
import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.WHITE;
import Engine.MoveGen.ChessBoardUtil;

public class Negamax {
    public static final int minInt = -2147483647;
    public static final int maxInt = 2147483647;

    public static int bestMove(ChessBoard board, int depth) {
        SearchTree tree = new SearchTree(1);
        int bestMove = 0;
        double bestMoveValue = minInt;
        int tempScore;
        int color;

        if (board.colorToMove == WHITE) {
            color = 1;
        } else {
            color = -1;
        }

        tree.startPly();
        MoveGen.generateMoves(tree, board);
        MoveGen.generateCaptures(tree, board);

        while (tree.hasNext()) {
            final int move = tree.next();
            if (!board.isLegal(move)) {
                continue;
            }
            board.doMove(move);
            tempScore = -calcBestMoveNegamax(board, depth - 1, -color, tree, minInt, maxInt);

            if (tempScore > bestMoveValue) {
                bestMove = move;
                bestMoveValue = tempScore;
            }
            board.undoMove(move);
        }

        tree.endPly();
        return bestMove;
    }

    public static int calcBestMoveNegamax(ChessBoard board, int depth, int color, SearchTree tree, int alpha, int beta) {
        int bestMoveValue = 0;
        int tempScore;

        if (depth == 0) {
            return -Quiescence(board, tree, -beta, -alpha, -color);
        } else {
            tree.startPly();
            MoveGen.generateMoves(tree, board);
            MoveGen.generateCaptures(tree, board);

            bestMoveValue = minInt;

            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                tempScore = -calcBestMoveNegamax(board, depth - 1, -color, tree, -beta, -alpha);

                if (tempScore > bestMoveValue) {
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
                
                if(bestMoveValue > alpha) {
                    alpha = bestMoveValue;
                }
                
                if(alpha >= beta) {
                    break;
                }
            }
            tree.endPly();
            return bestMoveValue;
        }
    }

    public static int Quiescence(ChessBoard board, SearchTree tree, int alpha, int beta, int color) {
        int bestValue = color * Eval.boardEval(board);

        if (bestValue >= beta) {
            return beta;
        }
        if(alpha < bestValue) {
            alpha = bestValue;
        }

        tree.startPly();
        MoveGen.generateCaptures(tree, board);


        while (tree.hasNext()) {
            final int move = tree.next();
            if (!board.isLegal(move)) {
                continue;
            }
            board.doMove(move);
            final int value = -1 * Quiescence(board, tree, -beta, -alpha, -color);
            board.undoMove(move);

            if (value >= beta) {
                tree.endPly();
                return beta;
            }
            if(value > alpha) {
                alpha = value;
            }
        }

        tree.endPly();
        return alpha;
    }

}
