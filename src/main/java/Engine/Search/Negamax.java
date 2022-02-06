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

    public static final int maxScore = 1147483647;

    public static int calcBestMoveNegamax(ChessBoard board, int depth, SearchTree tree, int alpha, int beta) {
        int bestMoveValue = -maxScore;

        if (depth == 0) {
            return Quiescence(board, tree, alpha, beta);
        } else {
            tree.newLayer();
            MoveGen.generateMoves(tree, board);
            MoveGen.generateCaptures(tree, board);

            bestMoveValue = -maxScore;
            
            //check if is not top layer, then must
            //return move value instead of move
            if (!tree.isTop()) {
                while (tree.isLayerNotEmpty()) {
                    final int move = tree.next();
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    board.doMove(move);
                    bestMoveValue = Math.max(bestMoveValue, -calcBestMoveNegamax(board, depth - 1, tree, -beta, -alpha));

                    board.undoMove(move);

                    if (bestMoveValue > alpha) {
                        alpha = bestMoveValue;
                    }

                    if (alpha >= beta) {
                        break;
                    }
                }
                tree.endLayer();
                return bestMoveValue;
            } 
            
            else {
                int bestMove = tree.getFirstMove();
                
                while (tree.isLayerNotEmpty()) {
                    final int move = tree.next();
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    board.doMove(move);
                    final int value = -calcBestMoveNegamax(board, depth - 1, tree, -beta, -alpha);
                    
                    if(value > bestMoveValue) {
                        bestMoveValue = value;
                        bestMove = move;
                    }
                    
                    board.undoMove(move);

                    if (bestMoveValue > alpha) {
                        alpha = bestMoveValue;
                    }

                    if (alpha >= beta) {
                        break;
                    }
                }
                tree.endLayer();
                
                return bestMove;
            }

        }
    }

    public static int Quiescence(ChessBoard board, SearchTree tree, int alpha, int beta) {
        int bestValue = Eval.boardEval(board) * board.getColor();

        if (bestValue >= beta) {
            return beta;
        }
        if (alpha < bestValue) {
            alpha = bestValue;
        }

        tree.newLayer();
        MoveGen.generateCaptures(tree, board);

        while (tree.isLayerNotEmpty()) {
            final int move = tree.next();
            if (!board.isLegal(move)) {
                continue;
            }
            board.doMove(move);
            final int value = -1 * Quiescence(board, tree, -beta, -alpha);
            board.undoMove(move);

            if (value >= beta) {
                tree.endLayer();
                return beta;
            }
            if (value > alpha) {
                alpha = value;
            }
        }

        tree.endLayer();
        return alpha;
    }

}
