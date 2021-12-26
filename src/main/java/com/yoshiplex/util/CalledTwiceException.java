package com.yoshiplex.util;

/**
 * should be thrown when some method has been called twice or called at the wrong or inappropriate time
 * @author retro
 * 
 */
public class CalledTwiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String reason;
	
	public CalledTwiceException(String reason) {
        this.reason = reason;
    }

	@Override
	public String getLocalizedMessage() {
		return this.reason;
	}
}
