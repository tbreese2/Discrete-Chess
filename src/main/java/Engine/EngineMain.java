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

public class EngineMain {
    private final String fileArray = "hgfedcba";
    private final String rankArray = "12345678";
    
    private ChessBoard board;
    private int depth;
    
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
        
        board.doMove(MoveUtil.createMove(Long.numberOfTrailingZeros(originalLocation), Long.numberOfTrailingZeros(toLocaion), getPieceIndex(originalLocation)));
        System.out.println(ChessBoardUtil.toString(board, true));
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
    
}
