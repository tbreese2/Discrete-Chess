package Engine;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
import static Engine.EngineValues.BISHOP;
import static Engine.EngineValues.BLACK;
import static Engine.EngineValues.KING;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.WHITE;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.MoveGen.Bitboard;
import Engine.MoveGen.CastlingUtil;
import Engine.MoveGen.MoveUtil;
import Engine.Search.Negamax;
import Engine.MoveGen.MoveGen;
import static Engine.MoveGen.ChessConstants.KNIGHT;
import Engine.MoveGen.StaticMoves;

//main engine class
//able to read in moves in UCI formated String
//return best moves in UCI formated string
public class EngineMain {
    
    //strings to convert UCI board positions to indexes
    private final String fileArray = "hgfedcba";
    private final String rankArray = "12345678";
    private final String peiceArray = "  NBRQK";
    private final String peiceArrayLowerCase = "  nbrqk";
    
    //class to represent chess board and 
    //all bitboard information
    private ChessBoard board;
    
    //modes: infinite, movetime, more to come
    private String mode;
    private double moveTime;
    
    //MODIFIES: this
    //EFFECTS: creates new chessboard
    //by defualt set with FIDE board
    //position
    public EngineMain() {
        board = ChessBoardUtil.getNewCB();
    }
    
    //MODIFIES: this
    //EFFECTS: creates board with costume fen
    public void setBoardFen(String fen) {
        board = ChessBoardUtil.getNewCB(fen);
    }
    
    //MODIFIES: this
    //EFFECTS: resets board to default
    //FIDE board state
    public void resetBoard() {
        board = ChessBoardUtil.getNewCB();
    }
    
    //MODIFIES: EngineMain
    //EFFECTS: Plays move
    //most be UCI formated string
    public void doUCIMove(String move) {
        board.doMove(stringToMove(move));
    }
    
    //EFFECTS: helper function
    //to convert UCI formated string to move
    private int stringToMove(String move) {
        char[] moveArray = move.toCharArray();
        long originalLocation = getBitBoard(moveArray[0], moveArray[1]);
        long toLocaion = getBitBoard(moveArray[2], moveArray[3]);
        
        //check if it is a promotion move
        if(move.length() == 5 && getPieceIndex(toLocaion, board.colorToMoveInverse) == -1) {
            String promotion = String.valueOf(move.charAt(4));
            MoveUtil.createPromotionMove(peiceArrayLowerCase.indexOf(promotion), Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion));
        }
        
        //check if promotion attack
        if(move.length() == 5 && getPieceIndex(toLocaion, board.colorToMoveInverse) != -1) {
            String promotion = String.valueOf(move.charAt(4));
            MoveUtil.createPromotionAttack(peiceArrayLowerCase.indexOf(promotion), Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(toLocaion, board.colorToMoveInverse));
        }
        
        //check if en passant
        int enPassantMove = 0;
        SearchTree checkEnPassantCastle = new SearchTree();
        checkEnPassantCastle.newLayer();
        if (board.epIndex == 0) {
        } else {
            long piece = board.pieces[board.colorToMove][PAWN] & StaticMoves.PAWN_ATTACKS[board.colorToMoveInverse][board.epIndex];
            while (piece != 0) {
                if (board.isLegalEPMove(Long.numberOfTrailingZeros(piece))) {
                    checkEnPassantCastle.addNode(MoveUtil.createEPMove(Long.numberOfTrailingZeros(piece), board.epIndex));
                }
                piece &= piece - 1;
            }
        }
        if(checkEnPassantCastle.isLayerNotEmpty()) {
           int tempMove = checkEnPassantCastle.next();
           if(MoveUtil.getFromIndex(tempMove) == Long.numberOfTrailingZeros(originalLocation) 
           && MoveUtil.getToIndex(tempMove) == Long.numberOfTrailingZeros(toLocaion)) { 
               //then it is an enPassantMove, return move
               enPassantMove = tempMove;
               return enPassantMove;
           }
        }
        checkEnPassantCastle.endLayer();
        
        //next, check if it is a castling move
        int castleMove = 0;
        checkEnPassantCastle = new SearchTree();
        checkEnPassantCastle.newLayer();
        final int fromIndex = board.kingIndex[board.colorToMove];
        long moves = StaticMoves.KING_MOVES[fromIndex] & board.emptySpaces;
        while (moves != 0) {
            moves &= moves - 1;
        }
        if (board.checkingPieces == 0) {
            long castlingIndexes = CastlingUtil.getCastlingIndexes(board);
            while (castlingIndexes != 0) {
                final int castlingIndex = Long.numberOfTrailingZeros(castlingIndexes);
                if (CastlingUtil.isValidCMove(board, fromIndex, castlingIndex)) {
                    checkEnPassantCastle.addNode(MoveUtil.createCastlingMove(fromIndex, castlingIndex));
                }
                castlingIndexes &= castlingIndexes - 1;
            }
        }
        if(checkEnPassantCastle.isLayerNotEmpty()) {
           int tempMove = checkEnPassantCastle.next();
           if(MoveUtil.getFromIndex(tempMove) == Long.numberOfTrailingZeros(originalLocation) 
           && MoveUtil.getToIndex(tempMove) == Long.numberOfTrailingZeros(toLocaion)) { 
               //then it is an enPassantMove, return move
               castleMove = tempMove;
               return castleMove;
           }
        }
        checkEnPassantCastle.endLayer();
        
        //check if it is an capture move
        if(getPieceIndex(toLocaion, board.colorToMoveInverse) != -1) {
            //it is an capture move, create an attack move
            return MoveUtil.createCaptureMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation, board.colorToMove), getPieceIndex(toLocaion, board.colorToMoveInverse));
        }
        
        return MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation, board.colorToMove));
    }
    
    //EFFECTS: helper function 
    //given location and piece color
    //tries to find piece and return type
    //if piece can't be found, returns -1
    private int getPieceIndex(final long location, int color) {
        
        //this hurts to look at, but it works
        if((board.pieces[color][PAWN] & location) != 0) {
            return PAWN;
        } else if((board.pieces[color][KNIGHT] & location) != 0) {
            return KNIGHT;
        } else if((board.pieces[color][BISHOP] & location) != 0) {
            return BISHOP;
        } else if((board.pieces[color][ROOK] & location) != 0) {
            return ROOK;
        } else if((board.pieces[color][QUEEN] & location) != 0) {
            return QUEEN;
        } else if((board.pieces[color][KING] & location) != 0) {
            return KING;
        }
        return -1;
    }
    
    //EFFECTS: helper function
    //returns bitboard with piece
    //at rank, file
    private long getBitBoard(char file, char rank) {
       return Bitboard.FILES[fileArray.indexOf(file)] & Bitboard.RANKS[rankArray.indexOf(rank)];
    }
    
    //MODIFIES: this
    //EFFECTS: sets engine mode
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    //MODIFIES: this
    //EFFECTS: sets engine move time
    public void setMoveTime(double time) {
        moveTime = time;
    }
    
    //MODIFIES: this
    //Effects: generates move for current board,
    //applys move to current board and then returns move in UCI formated string
    public String generateMove() {
        //defualt depth
        byte depth = 4;
        
        //create new search tree
        SearchTree tree = new SearchTree();
        
        //run negamax search
        int result = Negamax.calcBestMoveNegamax(board, depth, tree, -EngineValues.MAX_MATE_SCORE, EngineValues.MAX_MATE_SCORE);
        
        //do move and return
        board.doMove(result);
        String move = moveToString(result);
        return move;
    }
    
    //EFFECTS: helper function
    //given a move formated based on moveUtil returns UCI formated string of move
    private String moveToString(int move) {
        //get all essential information from the move
        int toIndex = MoveUtil.getToIndex(move);
        int fromIndex = MoveUtil.getFromIndex(move);
        int type = MoveUtil.getMoveType(move);
        int toIndexRank = toIndex/8;
        int toIndexFile = toIndex - 8 * toIndexRank;
        int fromIndexRank = fromIndex/8;
        int fromIndexFile = fromIndex - 8 * fromIndexRank;
        int piece = MoveUtil.getSourcePieceIndex(move);
        
        //convert to string
        String temp = "";
        temp = String.valueOf(peiceArray.charAt(piece)) +
               String.valueOf(fileArray.charAt(fromIndexFile)) +
               String.valueOf(rankArray.charAt(fromIndexRank)) +
               String.valueOf(fileArray.charAt(toIndexFile)) +
               String.valueOf(rankArray.charAt(toIndexRank));
        
        //add promotion information
        if(type == MoveUtil.TYPE_PROMOTION_N) {
            temp += "n";
        }
        
        if(type == MoveUtil.TYPE_PROMOTION_B) {
            temp += "b";
        }
        
        if(type == MoveUtil.TYPE_PROMOTION_R) {
            temp += "r";
        }
        
        if(type == MoveUtil.TYPE_PROMOTION_Q) {
            temp += "q";
        }
        
        //return final move
        return temp;
    }
}
