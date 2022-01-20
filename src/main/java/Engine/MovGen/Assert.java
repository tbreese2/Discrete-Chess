/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Engine.MovGen;


public class Assert {

	public static void isTrue(boolean condition) {
		if (!condition) {
			throw new AssertionError();
		}
	}

	public static void isTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}

}
