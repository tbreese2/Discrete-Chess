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
import static Engine.EngineValues.ALL_NODE;
import static Engine.EngineValues.PV_NODE;
import static Engine.EngineValues.FORCED_ALL_NODE;
import static Engine.EngineValues.CUT_NODE;

//negamax search class
public class Negamax {

    //standadardized max score
    //public because other functions may
    //find it useful
    public static final int maxScore = 1147483647;
    
    //transposition table for dynamic programming
    //by default creates a 32 mb table
    public static final TTTable table = new TTTable(100);

    //MODIFIES: tree
    //EFFECTS: searches for the best move to given depth
    //returns move as an int
    public static int calcBestMoveNegamax(ChessBoard board, byte depth, SearchTree tree, int alpha, int beta) {
        int bestMoveValue = -maxScore;
        final int oAlpha = alpha;
        
        //check if entry has already been evaluated before
        table.nextAge();
        Position get = table.transpositionTableLookup(board.zobristKey);
        
        //check if position is a hit
        if(get.zobrist == board.zobristKey >> 32) {
            if (get.type == PV_NODE) {
                return get.value;
            } else if (get.type == CUT_NODE) {
                if (get.value >= beta) {
                    return get.value;
                }
            } else if ((get.type & ALL_NODE) != 0) {
                if (get.value <= alpha) {
                    return get.value;
                }
            }
        }
        
        //end search
        if (depth == 0) {
            return Quiescence(board, tree, alpha, beta);
        } 
        //conduct branch and bound search
        else {
            //create new layer and populate
            tree.newLayer();
            MoveGen.generateMoves(tree, board);
            MoveGen.generateCaptures(tree, board);

            //best move for layer will be highest, so set
            //to negative max score
            bestMoveValue = -maxScore;
            
            //check if is not top layer, then must
            //return move value instead of move
            if (!tree.isTop()) {
                int bestMove = tree.getFirstMove();
                //loop through every move
                while (tree.isLayerNotEmpty()) {
                    final int move = tree.next();
                    
                    //check if move is illegal
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    
                    board.doMove(move);
                    
                    final int value = -calcBestMoveNegamax(board, (byte)(depth - 1), tree, -beta, -alpha);
                    
                    //update max move values for particular layer
                    if(value > bestMoveValue) {
                        bestMoveValue = value;
                        bestMove = move;
                    }

                    board.undoMove(move);

                    //set new alpha
                    if (bestMoveValue > alpha) {
                        alpha = bestMoveValue;
                    }
                    
                    //alpha beta pruning condition
                    if (alpha >= beta) {
                        table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)CUT_NODE);
                        break;
                    }
                }
                tree.endLayer();
                
                //write node to ttable
                if (alpha > oAlpha) {
                    table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)PV_NODE);
                } else {
                    table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)ALL_NODE);
                }
                
                return bestMoveValue;
            } 
            
            else {
                //if its not the top layer, then we return the best move
                //instead of its score
                int bestMove = tree.getFirstMove();
                
                //loop through every move
                while (tree.isLayerNotEmpty()) {
                    final int move = tree.next();
                    if (!board.isLegal(move)) {
                        continue;
                    }
                    board.doMove(move);
                    
                    //recursive call
                    final int value = -calcBestMoveNegamax(board, (byte)(depth - 1), tree, -beta, -alpha);
                    
                    //update max move values for particular layer
                    if(value > bestMoveValue) {
                        bestMoveValue = value;
                        bestMove = move;
                    }
                    
                    board.undoMove(move);

                    //alpha beta pruning
                    if (bestMoveValue > alpha) {
                        alpha = bestMoveValue;
                    }
                    if (alpha >= beta) {
                        table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)CUT_NODE);
                        break;
                    }
                }
                tree.endLayer();
                
                //write node to ttable
                if (alpha > oAlpha) {
                    table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)PV_NODE);
                } else {
                    table.transpositionTableStore(board.zobristKey, bestMove, bestMoveValue, depth, (byte)ALL_NODE);
                }
                return bestMove;
            }

        }
    }

    //EFFECTS: helper function
    //quiescence is a way to verify search accuracy
    //goes to a static position before evaluating board
    private static int Quiescence(ChessBoard board, SearchTree tree, int alpha, int beta) {
        
        //stand pat
        int bestValue = Eval.boardEval(board) * board.getColor();

        //alpha beta pruning
        if (bestValue >= beta) {
            return beta;
        }
        if (alpha < bestValue) {
            alpha = bestValue;
        }

        tree.newLayer();
        MoveGen.generateCaptures(tree, board);

        //go through disruptive moves
        while (tree.isLayerNotEmpty()) {
            final int move = tree.next();
            
            //check if legal
            if (!board.isLegal(move)) {
                continue;
            }
            board.doMove(move);
            
            //recursive call
            final int value = -1 * Quiescence(board, tree, -beta, -alpha);
            
            board.undoMove(move);

            //stand pat/alpha beta
            if (value >= beta) {
                tree.endLayer();
                return beta;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        
        tree.endLayer();
        
        //stand pat
        return alpha;
    }

}
