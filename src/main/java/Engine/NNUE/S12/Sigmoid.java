/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.NNUE.S12;

import java.lang.*;

public class Sigmoid {
    public static float getY(final float x) {
        return 1 / (1 + (float) Math.exp(-x));
    }

    public static double getX(final float y) {
        return Math.log(y / (float) (1-y));
    }
}
