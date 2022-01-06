
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
public class Bishop implements Piece {
     private Cordinate location;
    private String color;
    private boolean captured;
    
    //REQUIRES: that xIn and Yin are valid cordinates on board
    //and color is either "white" or "black"
    public Bishop(int xStart, int yStart, String colorIn) {
        location.move(xStart, yStart);
        color = colorIn;
        captured = false;
    }
    //EFFECTS: returns location of piece, 
    public Cordinate location() {
        return location;
    }
    
    //EFFECTS: returns a list of valid moves for the piece given its locaiton
    //does not account for king saftey, so if moving the piece out of the way
    //puts this king in check, it will still allow the move
    //also will list any potential unpessant as a move, must check
    //if you can actualy do it
    public ArrayList<Cordinate> possibleMoves() {
        ArrayList<Cordinate> moves = new ArrayList<>();
        
        //walk in all 4 diagonal directions and add moves
        for(int i = x; i <= 8; x++) {
            
        }
        
        
        return moves;
    }
    
    //REQUIRES: that xIn and Yin are valid cordinates on board
    //bottum left is (1,1) and that xIn and Yin are a valid
    //move for the piece
    //MODIFIES: Piece
    //EFFECTS: moves piece to 
    public void move(int xIn, int yIn) {
        location.move(xIn, yIn);
    }
    
    //MODIFIES: Piece
    //EFFECTS: the piece will be captured after this function
    //is called
    public void capturePiece() {
        captured = true;
    }
    
    //EFFECTS: returns true if a piece is captured
    //returns false otherwise
    public boolean isCaptured() {
        return captured;
    }
    
    //EFFECTS: returns "white" if white and "black" if black
    public String getColor() {
        return color;
    }
}
