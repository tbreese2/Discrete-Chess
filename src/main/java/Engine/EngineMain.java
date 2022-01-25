package Engine;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
import Engine.MoveGen.ChessBoard;
import Engine.MoveGen.ChessBoardUtil;

public class EngineMain {
    ChessBoard board;
    
    public EngineMain() {
        board = ChessBoardUtil.getNewCB();
    }
    
    public void setFen(String fen) {
        board = ChessBoardUtil.getNewCB(fen);
    }
}
