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

public class Negamax {

    public static int bestMove(ChessBoard board, int depth) {
        SearchTree tree = new SearchTree(1);
        int bestMove = 0;
        double bestMoveValue = -2147483648;
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
            tempScore = -calcBestMoveNegamax(board, depth - 1, -color, tree);

            if (tempScore > bestMoveValue) {
                bestMove = move;
                bestMoveValue = tempScore;
            }
            board.undoMove(move);
        }

        tree.endPly();
        return bestMove;
    }

    public static int calcBestMoveNegamax(ChessBoard board, int depth, int color, SearchTree tree) {
        int bestMoveValue = 0;
        int tempScore;

        if (depth == 0) {
            return color * Eval.boardEval(board);
        } else {
            tree.startPly();
            MoveGen.generateMoves(tree, board);
            MoveGen.generateCaptures(tree, board);

            bestMoveValue = -2147483648;

            while (tree.hasNext()) {
                final int move = tree.next();
                if (!board.isLegal(move)) {
                    continue;
                }
                board.doMove(move);
                tempScore = -calcBestMoveNegamax(board, depth - 1, -color, tree);
                if (tempScore > bestMoveValue) {
                    bestMoveValue = tempScore;
                }
                board.undoMove(move);
            }
            tree.endPly();
            return bestMoveValue;
        }
    }

}
