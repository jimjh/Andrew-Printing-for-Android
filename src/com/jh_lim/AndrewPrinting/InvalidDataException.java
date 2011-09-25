package com.jh_lim.AndrewPrinting;

/**
 * Throw this when the image data (or data to print) is invalid.
 * @author Jiunn Haur Lim
 */
public class InvalidDataException extends Exception {

	private static final long serialVersionUID = -7555842369208928458L;

	public InvalidDataException (String msg){
		super (msg);
	}
	
}
