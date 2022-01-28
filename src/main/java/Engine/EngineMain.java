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
import Engine.MoveGen.MoveUtil;
import Engine.Search.MinMax;
import Engine.MoveGen.MoveGen;
import static Engine.MoveGen.ChessConstants.KNIGHT;
import Engine.MoveGen.KingMoveGen;

public class EngineMain {
    private final String fileArray = "hgfedcba";
    private final String rankArray = "12345678";
    private final String peiceArray = "  NBRQK";
    private final String peiceArrayLowerCase = "  nbrqk";
    
    private ChessBoard board;
    private int depth;
    //modes: infinite, movetime
    private String mode;
    private double moveTime;
    
    public EngineMain() {
        board = ChessBoardUtil.getNewCB();
    }
    
    public void setBoardFen(String fen) {
        board = ChessBoardUtil.getNewCB(fen);
    }
    
    public void resetBoard() {
        board = ChessBoardUtil.getNewCB();
    }
    
    public void doUCIMove(String move) {
        board.doMove(stringToMove(move));
    }
    
    public int stringToMove(String move) {
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
        SearchTree checkEnPassantCastle = new SearchTree(1);
        checkEnPassantCastle.startPly();
        MoveGen.addEpAttacks(checkEnPassantCastle, board);
        if(checkEnPassantCastle.hasNext()) {
           int tempMove = checkEnPassantCastle.next();
           if(MoveUtil.getFromIndex(tempMove) == Long.numberOfTrailingZeros(originalLocation) 
           && MoveUtil.getToIndex(tempMove) == Long.numberOfTrailingZeros(toLocaion)) { 
               //then it is an enPassantMove, return move
               enPassantMove = tempMove;
               return enPassantMove;
           }
        }
        checkEnPassantCastle.endPly();
        
        //next, check if it is a castling move
        int castleMove = 0;
        checkEnPassantCastle = new SearchTree(1);
        checkEnPassantCastle.startPly();
        KingMoveGen.addKingCastlingMoves(checkEnPassantCastle, board);
        if(checkEnPassantCastle.hasNext()) {
           int tempMove = checkEnPassantCastle.next();
           if(MoveUtil.getFromIndex(tempMove) == Long.numberOfTrailingZeros(originalLocation) 
           && MoveUtil.getToIndex(tempMove) == Long.numberOfTrailingZeros(toLocaion)) { 
               //then it is an enPassantMove, return move
               castleMove = tempMove;
               return castleMove;
           }
        }
        checkEnPassantCastle.endPly();
        
        //check if it is an capture move
        if(getPieceIndex(toLocaion, board.colorToMoveInverse) != -1) {
            //it is an capture move, create an attack move
            return MoveUtil.createCaptureMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation, board.colorToMove), getPieceIndex(toLocaion, board.colorToMoveInverse));
        }
        
        return MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation, board.colorToMove));
    }
    
    public int getPieceIndex(final long location, int color) {
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
    
    private long getBitBoard(char file, char rank) {
       return Bitboard.FILES[fileArray.indexOf(file)] & Bitboard.RANKS[rankArray.indexOf(rank)];
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public void setMoveTime(double time) {
        moveTime = time;
    }
    
    public String generateMove() {
        int depth = 5;
        boolean isWhite = board.colorToMove == WHITE;
        int result = MinMax.bestMove(board, depth, isWhite);
        board.doMove(result);
        System.out.println(ChessBoardUtil.toString(board, isWhite));
        String move = moveToString(result);
        return move;
    }
    
    public String moveToString(int move) {
        int toIndex = MoveUtil.getToIndex(move);
        int fromIndex = MoveUtil.getFromIndex(move);
        int type = MoveUtil.getMoveType(move);
        int toIndexRank = toIndex/8;
        int toIndexFile = toIndex - 8 * toIndexRank;
        int fromIndexRank = fromIndex/8;
        int fromIndexFile = fromIndex - 8 * fromIndexRank;
        int piece = MoveUtil.getSourcePieceIndex(move);
        
        String temp = "";
        temp = String.valueOf(peiceArray.charAt(piece)) +
               String.valueOf(fileArray.charAt(fromIndexFile)) +
               String.valueOf(rankArray.charAt(fromIndexRank)) +
               String.valueOf(fileArray.charAt(toIndexFile)) +
               String.valueOf(rankArray.charAt(toIndexRank));
        
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
        
        return temp;
    }
}
