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

public class EngineMain {
    private final String fileArray = "hgfedcba";
    private final String rankArray = "12345678";
    private final String peiceArray = " PNBRQK";
    
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
        
        //check if en passant
        int enPassantMove = 0;
        SearchTree checkEnPassant = new SearchTree(1);
        checkEnPassant.startPly();
        MoveGen.addEpAttacks(checkEnPassant, board);
        if(checkEnPassant.hasNext()) {
           int tempMove = checkEnPassant.next();
           if(MoveUtil.getFromIndex(tempMove) == Long.numberOfTrailingZeros(originalLocation) 
           && MoveUtil.getToIndex(tempMove) == Long.numberOfTrailingZeros(toLocaion)) {
               
               //then it is an enPassantMove, return move
               return enPassantMove;
           }
        }
        checkEnPassant.endPly();
        
        //next, check if it is a castling move
        
        return MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation));
    }
    
    public int getPieceIndex(long location) {
        if((board.pieces[board.colorToMove][PAWN] & location) != 0) {
            return PAWN;
        } else if((board.pieces[board.colorToMove][KNIGHT] & location) != 0) {
            return KNIGHT;
        } else if((board.pieces[board.colorToMove][BISHOP] & location) != 0) {
            return BISHOP;
        } else if((board.pieces[board.colorToMove][ROOK] & location) != 0) {
            return ROOK;
        } else if((board.pieces[board.colorToMove][QUEEN] & location) != 0) {
            return QUEEN;
        } else if((board.pieces[board.colorToMove][KING] & location) != 0) {
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
        if(type == MoveUtil.TYPE_NORMAL) {
            temp = String.valueOf(fileArray.charAt(fromIndexFile)) +
                   String.valueOf(rankArray.charAt(fromIndexRank)) +
                   String.valueOf(fileArray.charAt(toIndexFile)) +
                   String.valueOf(rankArray.charAt(toIndexRank));
        }
        return temp;
    }
}
