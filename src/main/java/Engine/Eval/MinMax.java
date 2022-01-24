/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Eval;

import Engine.MoveGen.MoveGen;
import Engine.MoveGen.ChessBoard;
import Engine.SearchTree;

public class MinMax {

    public static int bestMove(ChessBoard board, int depth, boolean isWhite) {
        SearchTree tree = new SearchTree(1);
        int bestMove = 0;
        double bestMoveValue = 0;
        double tempScore;

        if (isWhite) {
            bestMoveValue = -2147483648;
        } else {
            bestMoveValue = 2147483647;
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
            if(depth == 1) {
                tempScore = Eval.boardEval(board);
            } else {
                tempScore = calcBestMoveMinMax(board, depth - 1, !isWhite, tree);
            }
            
            if (isWhite && tempScore > bestMoveValue) {
                bestMove = move;
                bestMoveValue = tempScore;
            } else if (!isWhite && tempScore < bestMoveValue) {
                bestMove = move;
                bestMoveValue = tempScore;
            }
            board.undoMove(move);
        }

        tree.endPly();
        return bestMove;
    }

    public static double calcBestMoveMinMax(ChessBoard board, int depth, boolean isMaximizingPlayer, SearchTree tree) {
        double bestMoveValue = 0;
        double tempScore;

        if (isMaximizingPlayer) {
            bestMoveValue = -2147483648;
        } else {
            bestMoveValue = 2147483647;
        }

        tree.startPly();
        MoveGen.generateMoves(tree, board);
        MoveGen.generateCaptures(tree, board);

        if (depth == 1) {
            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                tempScore = Eval.boardEval(board);
                if (isMaximizingPlayer && tempScore > bestMoveValue) {
                    bestMoveValue = tempScore;
                } else if (!isMaximizingPlayer && tempScore < bestMoveValue) {
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
            }

            tree.endPly();

            return bestMoveValue;
        } else {
            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                tempScore = calcBestMoveMinMax(board, depth - 1, !isMaximizingPlayer, tree);
                if (isMaximizingPlayer && tempScore > bestMoveValue) {
                    bestMoveValue = tempScore;
                } else if (!isMaximizingPlayer && tempScore < bestMoveValue) {
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
            }
            tree.endPly();
        }
        return bestMoveValue;
    }

}
