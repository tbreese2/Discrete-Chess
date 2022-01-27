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
import static Engine.MoveGen.ChessConstants.NIGHT;
import static Engine.EngineValues.PAWN;
import static Engine.EngineValues.QUEEN;
import static Engine.EngineValues.ROOK;
import static Engine.EngineValues.WHITE;
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;
import Engine.MoveGen.Bitboard;
import Engine.MoveGen.MoveUtil;
import Engine.Search.MinMax;

public class EngineMain {
    private final String fileArray = "hgfedcba";
    private final String rankArray = "12345678";
    
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
        char[] moveArray = move.toCharArray();
        long originalLocation = getBitBoard(moveArray[0], moveArray[1]);
        long toLocaion = getBitBoard(moveArray[2], moveArray[3]);
        
        getMoveString(MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation)));
        board.doMove(MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation)));
    }
    
    public int getPieceIndex(long location) {
        if(Long.numberOfTrailingZeros(board.pieces[board.colorToMove][PAWN] & location) != 0) {
            return PAWN;
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
        boolean isWhite = true;
        int expResult = 0;
        int result = MinMax.bestMove(board, depth, isWhite);
        board.doMove(result);
        String move = getMoveString(result);
        return move;
    }
    
    public String getMoveString(int move) {
        int toIndex = MoveUtil.getToIndex(move);
        int fromIndex = MoveUtil.getFromIndex(move);
        int type = MoveUtil.getMoveType(move);
        int toIndexRank = toIndex/8;
        int toIndexFile = toIndex - 8 * toIndexRank;
        System.out.println(toIndexRank + " " + toIndexFile + " " + toIndex);
        if(type == MoveUtil.TYPE_NORMAL) {
            
        }
        String temp = "";
        return temp;
    }
}
