/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.Search;
import java.lang.instrument.Instrumentation;
import java.io.*;

public class SizeOf{
    /*
     * Java method to return size of primitive data type based on hard coded values
     * valid but provided by developer
     */
    public static int serialize(Class dataType) {
        if (dataType == null) {
            throw new NullPointerException();
        }
        if (dataType == byte.class || dataType == Byte.class) {
            return 1;
        }
        if (dataType == short.class || dataType == Short.class) {
            return 2;
        }
        if (dataType == char.class || dataType == Character.class) {
            return 2;
        }
        if (dataType == int.class || dataType == Integer.class) {
            return 4;
        }
        if (dataType == long.class || dataType == Long.class) {
            return 8;
        }
        if (dataType == float.class || dataType == Float.class) {
            return 4;
        }
        if (dataType == double.class || dataType == Double.class) {
            return 8;
        }
        return 4; // default for 32-bit memory pointer
    }
}