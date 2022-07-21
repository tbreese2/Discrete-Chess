/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MoveGen;

public class PieceToHistory {

    int[][] array = new int[16][64];

    PieceToHistory() {

        for (int i = 0; i < 16; i++) {

            for (int j = 0; j < 64; j++) {

                array[i][j] = 1;
            }
        }
    }
}
