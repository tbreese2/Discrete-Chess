/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tylerbreese
 */
public class Square {
    private String occupier;
    
    public Square() {
        this.occupier = "";
    }
    
    //REQUIRES: occupier takes first 2 letters of piece name, then first letter
    //of color, for example, knw would be the white knight
    //put an empty string for an unocupied square
    public Square(String occupier) {
        this.occupier = occupier;
    }
    
    //EFFECTS: returns true if the square is ocupied
    public boolean isOccupied() {
        return !occupier.equals("");
    }
    
    //REQUIRES: the square is occupied
    //EFFECTS: occupier of square in
    //occupier returns first 2 letters of piece name, then first letter
    //of color, for example, knw would be the white knight
    //put an empty string for an unocupied square
    public String getOccupier(){
        return occupier;
    }
    
    //REQUIRES: occupier takes first 2 letters of piece name, then first letter
    //of color, for example, knw would be the white knight
    //put an empty string for an unocupied square
    public void setOccupier(String occupier){
        this.occupier = occupier;
    }
}
