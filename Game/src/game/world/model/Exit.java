package game.world.model;

import game.world.util.Drawable;

import java.util.List;

/**
 * Exit
 * Defines what it is to be an exit in the game.
 * @author Shane Brewer
 *
 */
public abstract class Exit implements Drawable{

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


	public abstract String getName();

	public abstract String getImageName();

	public abstract boolean isLocked();

	public abstract boolean unlock(Inventory inventory);

	/**
	 * Should return the places counter part.
	 * @param place - place that needs to be found.
	 * @return - return the other place - null if the place is is not found..... imposable.
	 */
	public Place getOtherPlace(Place place) {
		for (Place otherPlace: contectedPlaces){
			if (!place.equals(otherPlace)){
				return otherPlace;
			}
		}
		return null;
	}
}
