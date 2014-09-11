package game.world.model;

import java.util.List;

/**
 * Exit
 * Defines what it is to be an exit in the game.
 * @author Shane Brewer
 *
 */
public abstract class Exit {
	
	private final List<Place> contectedPlaces;
	
	/**
	 * There should always be to places connected to a Exit.
	 * @param places
	 */
	public Exit(List<Place> places){
		if (places.size() != 2){
			throw new Error("All exits should connect to two places");
		}
		contectedPlaces = places;
	}
	
}
