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
import static Engine.EngineValues.PV_NODE;
import static Engine.EngineValues.CUT_NODE;
import static Engine.EngineValues.FORCED_ALL_NODE;
import static Engine.EngineValues.ALL_NODE;
import static Engine.EngineValues.MAX_MATE_SCORE;
import static Engine.EngineValues.ONE_PLY;
import static Engine.EngineValues.MAX_PLY;
import static Engine.EngineValues.MAX_PVSEARCH_PLY;
import static Engine.EngineValues.MAX_INTERNAL_PLY;
import static Engine.EngineValues.TB_WIN_SCORE;
import static Engine.EngineValues.MIN_MATE_SCORE;
import static Engine.EngineValues.RAZOR_MARGIN;
import Engine.MoveGen.ChessBoardUtil;

//negamax search class
public class Negamax {

    

    
    //transposition table for dynamic programming
    //by default creates a 32 mb table
    public static final TTTable table = new TTTable(100);

    //MODIFIES: tree
    //EFFECTS: searches for the best move to given depth
    //returns move as an int
    public static int calcBestMoveNegamax(ChessBoard board, byte depth, SearchTree tree, int alpha, int beta) {
        tree.newLayer();
        int bestScore = -MAX_MATE_SCORE;
        int score  = -MAX_MATE_SCORE;
        final int oAlpha = alpha;
        int staticEval = 0;
        int hashMove = 0;
        boolean pv = (beta - alpha) != 1;
        
        
        //begin q search
        if (depth == 0) {
            int ret = Quiescence(board, tree, alpha, beta);
            tree.endLayer();
            return ret;
        } 
        
        //check if entry has already been evaluated before
        table.nextAge();
        Position get = table.transpositionTableLookup(board.zobristKey);
        
        //check if position is a hit, currently only using cut node
        //or all node
        if (get.zobrist == board.zobristKey >> 32) {
            hashMove = get.move;

            staticEval = get.eval;
            int condition = (board.getPreviousMove() == 0 && get.score >= beta) ? 1 : 0;
            
            if (!pv && get.depth + condition * 100 >= depth) {
                if (get.type == PV_NODE) {
                    tree.endLayer();
                    return get.score;
                } else if (get.type == CUT_NODE) {
                    if (get.score >= beta) {
                        tree.endLayer();
                        return get.score;
                    }
                } else if ((get.type & ALL_NODE) != 0) {
                    if (get.score <= alpha) {
                        tree.endLayer();
                        return get.score;
                    }
                }
            }
        } else {
            if (board.checkingPieces != 0)
                staticEval = -MAX_MATE_SCORE + tree.ply;
            else {
                staticEval = Eval.boardEval(board) * board.getColor();
            }
        }
        
        tree.setHistoricEval(staticEval, board.colorToMove, tree.ply);
        
        // razoring
        if (depth <= 3 && staticEval + RAZOR_MARGIN * depth < beta) {
            score = Quiescence(board, tree, alpha, beta);
            if (score < beta) {
                tree.endLayer();
                return score;
            } else if (depth == 1) {
                tree.endLayer();
                return beta;
            }
        }
        
        //create new layer and populate
        MoveGen.generateMoves(tree, board);
        MoveGen.generateCaptures(tree, board);
            
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
            score = -calcBestMoveNegamax(board, (byte)(depth - 1), tree, -beta, -alpha);
                    
            //update max move values for particular layer
            if(score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
                    
            board.undoMove(move);

            //alpha beta pruning
             if (score > alpha) {
                // increase alpha
                alpha = score;
            }
            
            if (score >= beta) {
                table.transpositionTableStore(board.zobristKey, score, move, CUT_NODE, (byte)(depth - 3), tree.getHistoricEval(board.colorToMove, tree.ply));
                if (!tree.isTop()) {
                    tree.endLayer();
                    return bestScore;
                } else {
                    tree.endLayer();
                    return bestMove;
                }
            }
        }
        
        if (alpha > oAlpha) {
            table.transpositionTableStore(board.zobristKey, bestScore, bestMove, PV_NODE, depth, tree.getHistoricEval(board.colorToMove, tree.ply));
        } else {
            if (hashMove != 0 && get.type == CUT_NODE) {
                bestMove = get.move;
            } else if (bestScore == alpha){ //TODO: implement same move function && !sameMove(hashMove, bestMove)) {
                bestMove = 0;
            }

            if (depth > 7 && bestMove != 0){ //TODO: implement thread node count functions && (td->nodes - prevNodeCount) / 2 < bestNodeCount) {
                table.transpositionTableStore(board.zobristKey, bestScore, bestMove, FORCED_ALL_NODE, depth, tree.getHistoricEval(board.colorToMove, tree.ply));
            } else {
                table.transpositionTableStore(board.zobristKey, bestScore, bestMove, ALL_NODE, depth, tree.getHistoricEval(board.colorToMove, tree.ply));
            }
        }
                
        if (!tree.isTop()) {
            tree.endLayer();
            return bestScore;
        } else {
            tree.endLayer();
            return bestMove;
        }
    }

    //EFFECTS: helper function
    //quiescence is a way to verify search accuracy
    //goes to a static position before evaluating board
    private static int Quiescence(ChessBoard board, SearchTree tree, int alpha, int beta) {
        //check if entry has already been evaluated before
        //stand pat
       tree.newLayer();
        int bestMove = 0;
        
        // extract information like search data (history tables), zobrist etc
        long key = board.zobristKey;
        Position en = table.transpositionTableLookup(board.zobristKey);
        byte ttNodeType = ALL_NODE;

        int stand_pat;
        int bestScore = -MAX_MATE_SCORE;

        // transposition table probing:
        if (en.zobrist == key >> 32) {
            if (en.type == PV_NODE) {
                tree.endLayer();
                return en.score;
            } else if (en.type == CUT_NODE) {
                if (en.score >= beta) {
                    tree.endLayer();
                    return en.score;
                }
            } else if ((en.type & ALL_NODE) != 0) {
                if (en.score <= alpha) {
                    tree.endLayer();
                    return en.score;
                }
            }
            stand_pat = bestScore = en.eval;
        } else {
            stand_pat = bestScore = board.checkingPieces != 0 ? -MAX_MATE_SCORE + tree.ply : Eval.boardEval(board) * board.getColor();
        }

        // we can also use the tt entry to adjust the evaluation.
        if (en.zobrist == key >> 32) {
            // adjusting eval
            if (   (en.type == PV_NODE)
                || (en.type == CUT_NODE && stand_pat < en.score)
                || ((en.type & ALL_NODE) != 0 && stand_pat > en.score)) {
                // save as best score
                bestScore = en.score;
            }
        }

        if (bestScore >= beta || tree.ply >= 100) {
            tree.endLayer();
            return bestScore;
        }
            
        if (alpha < bestScore)
            alpha = bestScore;

    
        MoveGen.generateCaptures(tree, board);

        //go through disruptive moves
        while (tree.isLayerNotEmpty()) {
            final int move = tree.next();
            
            //check if legal
            if (!board.isLegal(move)) {
                continue;
            }
        
            // static exchange evaluation pruning (see pruning):
            //Score see =
                //(!inCheck && (isCapture(m) || isPromotion(m))) ? mGen->lastSee : 0;
           // if (see < 0)
                //continue;
            //if (see + stand_pat > beta + 200)
                //return beta;
        

            board.doMove(move);
            
            //recursive call
            final int score = -1 * Quiescence(board, tree, -beta, -alpha);
            
            board.undoMove(move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                if (score >= beta) {
                    ttNodeType = CUT_NODE;
                    // store the move with higher depth in tt incase the same capture would improve on
                    // beta in ordinary pvSearch too.
                    byte condition = (byte)(board.checkingPieces == 0 ? 1 : 0);
                    table.transpositionTableStore(key, bestScore, move, ttNodeType, condition, stand_pat);
                    tree.endLayer();
                    return score;
                }
                if (score > alpha) {
                    ttNodeType = PV_NODE;
                    alpha      = score;
                }
            }
        }

        // store the current position inside the transposition table
        if (bestMove != 0) {
            table.transpositionTableStore(key, bestScore, bestMove, ttNodeType, (byte)0, stand_pat);
        }
        
        tree.endLayer();
        return bestScore;
    }

}
