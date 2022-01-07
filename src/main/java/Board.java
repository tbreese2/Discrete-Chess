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
        setBoard();
    }
    
    //MODIFES: Board
    //EFFECTS: clears board and sets new peices
    public void setBoard() {
        //clear board
        board = new Square[8][8];
        
         for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                board[r][c] = new Square();
            }
        }
        
        
        //set white peices
        board[0][0].setOccupier("row");
        board[0][1].setOccupier("knw");
        board[0][2].setOccupier("biw");
        board[0][3].setOccupier("quw");
        board[0][4].setOccupier("kiw");
        board[0][5].setOccupier("biw");
        board[0][6].setOccupier("knw");
        board[0][7].setOccupier("row");
        board[1][0].setOccupier("paw");
        board[1][1].setOccupier("paw");
        board[1][2].setOccupier("paw");
        board[1][3].setOccupier("paw");
        board[1][4].setOccupier("paw");
        board[1][5].setOccupier("paw");
        board[1][6].setOccupier("paw");
        board[1][7].setOccupier("paw");
        
        //set black peices
        board[6][0].setOccupier("pab");
        board[6][1].setOccupier("pab");
        board[6][2].setOccupier("pab");
        board[6][3].setOccupier("pab");
        board[6][4].setOccupier("pab");
        board[6][5].setOccupier("pab");
        board[6][6].setOccupier("pab");
        board[6][7].setOccupier("pab");
        board[7][0].setOccupier("rob");
        board[7][1].setOccupier("knb");
        board[7][2].setOccupier("bib");
        board[7][3].setOccupier("qub");
        board[7][4].setOccupier("kib");
        board[7][5].setOccupier("bib");
        board[7][6].setOccupier("knb");
        board[7][7].setOccupier("rob");
    }
    
    //REQUIRES: iinput string must be in FEN format,
    //see https://www.chess.com/terms/fen-chess
    //for more details
    //MODIFES: Board
    //EFFECTS: sets board according to inputed string
    public void setBoard(String board) {
       
    }
    
    //EFFECTS: returns string representing board
    public String toString() {
        String out = "";
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                if(board[r][c].isOccupied()) {
                    out += board[r][c].getOccupier().charAt(0);
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
