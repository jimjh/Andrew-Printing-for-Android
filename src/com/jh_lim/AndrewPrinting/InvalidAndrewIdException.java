package com.jh_lim.AndrewPrinting;

/**
 * Throw this when the Andrew ID is invalid.
 * @author Jiunn Haur Lim
 */
public class InvalidAndrewIdException extends Exception {

	private static final long serialVersionUID = 2794070563339398135L;
	
	/** Rouge Andrew Id */
	private final String mId;
	
	/**
	 * @param msg
	 * 			Exception message
	 * @param id
	 * 			Andrew id that failed validation
	 */
	public InvalidAndrewIdException (String msg, String id){
		super (msg);
		mId = id;
	}
	
	public String getId (){
		return mId;
	}

}
