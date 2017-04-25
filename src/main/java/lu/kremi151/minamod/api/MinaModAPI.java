package lu.kremi151.minamod.api;

public interface MinaModAPI { // NO_UCD (unused code)
	
	/**
	 * Posts an overlay message to a player
	 * 
	 * @param player The player name
	 * @param text The message
	 * @param duration The duration to stay in milliseconds
	 * 
	 * @return Returns true if successful
	 */
	boolean postOverlayMessage(String player, String text, long duration);
}
