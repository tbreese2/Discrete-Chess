/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Search;

import Engine.Eval.Eval;
import Engine.MoveGen.MoveGen;
import Engine.MoveGen.ChessBoard;
import Engine.SearchTree;

public class MinMax {

    public static int bestMove(ChessBoard board, int depth, boolean isWhite) {
        SearchTree tree = new SearchTree(1);
        int bestMove = 0;
        double bestMoveValue = 0;
        double tempScore;

        tree.startPly();
        MoveGen.generateMoves(tree, board);
        MoveGen.generateCaptures(tree, board);

        if (isWhite) {
            bestMoveValue = -2147483648;
            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                if (depth == 1) {
                    tempScore = Eval.boardEval(board);
                } else {
                    tempScore = calcBestMoveMinMax(board, depth - 1, !isWhite, tree);
                }

                if (tempScore > bestMoveValue) {
                    bestMove = move;
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
            }
        } else {
            bestMoveValue = 2147483647;
            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                if (depth == 1) {
                    tempScore = Eval.boardEval(board);
                } else {
                    tempScore = calcBestMoveMinMax(board, depth - 1, !isWhite, tree);
                }

                if (tempScore < bestMoveValue) {
                    bestMove = move;
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
            }
        }

        tree.endPly();
        return bestMove;
    }

    public static double calcBestMoveMinMax(ChessBoard board, int depth, boolean isMaximizingPlayer, SearchTree tree) {
        double bestMoveValue = 0;
        double tempScore;

        if (depth == 0) {
            return Eval.boardEval(board);
        } else {
            tree.startPly();
            MoveGen.generateMoves(tree, board);
            MoveGen.generateCaptures(tree, board);

            if (isMaximizingPlayer) {
                bestMoveValue = -2147483648;

                while (tree.hasNext()) {
                    final int move = tree.next();
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    board.doMove(move);
                    tempScore = calcBestMoveMinMax(board, depth - 1, !isMaximizingPlayer, tree);
                    if (tempScore >= bestMoveValue) {
                        bestMoveValue = tempScore;
                    }
                    board.undoMove(move);
                }
            } else {
                bestMoveValue = 2147483647;
                
                 while (tree.hasNext()) {
                    final int move = tree.next();
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    board.doMove(move);
                    tempScore = calcBestMoveMinMax(board, depth - 1, !isMaximizingPlayer, tree);
                    if (tempScore <= bestMoveValue) {
                        bestMoveValue = tempScore;
                    }
                    board.undoMove(move);
                }
            }

        }
        tree.endPly();
        return bestMoveValue;
    }

}
