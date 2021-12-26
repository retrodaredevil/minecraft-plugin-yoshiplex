package com.yoshiplex.util.voting;

public interface Votable {

	
	/**
	 * 
	 * @return the lowercase name with no spaces
	 */
	public String getName();
	
	/**
	 * Note used for comparing
	 * @return the name to be displayed.
	 */
	public String getDisplayName();
	
}
