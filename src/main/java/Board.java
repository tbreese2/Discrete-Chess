/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
import java.util.ArrayList;

public class Board {
    Square[][] board;
    
    //EFFECTS: creates a new board without pieces
    public Board() {
        board = new Square[8][8];
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                board[r][c] = new Square();
            }
        }
        setBoard();
    }
    
    //MODIFES: Board
    //EFFECTS: clears board and sets new peices
    public void setBoard() {
        setBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }
    
    //REQUIRES: iinput string must be in FEN format,
    //see https://www.chess.com/terms/fen-chess
    //for more details
    //MODIFES: Board
    //EFFECTS: sets board according to inputed string
    public void setBoard(String FEN) {
       // using simple for-loop
       int r = 7;
       int c = 0;
        for (int i = 0; i < FEN.length(); i++) {
            Boolean flag = Character.isDigit(FEN.charAt(i));
            if(flag) {
                for(int b = 0; b < Character.getNumericValue(FEN.charAt(i)); b++){
                    board[r][c].setOccupier("");
                    c++;
                }
            } else if(FEN.charAt(i) == '/') {
                r--;
                c = 0;
            } else {
                board[r][c].setOccupier(String.valueOf(FEN.charAt(i)));
                c++;
            }
            
        }
    }
    
    //EFFECTS: returns string representing board
    public String toString() {
        String out = "";
        for(int r = 7; r >= 0; r--) {
            for(int c = 0; c < 8; c++) {
                if(board[r][c].isOccupied()) {
                    out += board[r][c].getOccupier();
                } else {
                    out += "*";
                } 
            }
            out += "\n";
        }
        return out;
    }
    
    //REQUIRES: xIn and yIn are valid board cordinates,
    //yIn (1-8) corisponds to 1-8 and xIn (1-8) corsiponds to a-h
    //EFFECTS: returns string representing board
    //if there is no peice, returns empty string
    public String getPieceAt(int xIn, int yIn) {
        if(board[xIn - 1][yIn - 1].isOccupied()) {
            return board[xIn - 1][yIn - 1].getOccupier();
        } else {
            return "";
        } 
    }
   
    
}
