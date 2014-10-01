package game.world.model;

import game.world.dimensions.Point3D;
import game.world.util.Drawable;

import java.util.*;

/**
 * Exit
 * Defines what it is to be an exit in the game.
 * @author Shane Brewer
 *
 */
public abstract class Exit implements Drawable{

	private final List<PlaceAndPosition> contectedPlaces;

	/**
	 * There should always be to places connected to a Exit.
	 * @param placeOne - where one place leads to.
	 * @param positionInPlaceOne - the place it is in in the world.
	 * @param placeTwo - the second place that the exit is connected to.
	 * @param positionInPlaceTwo - where in the second place the exit is.
	 */
	public Exit(Place placeOne, Point3D positionInPlaceOne, Place placeTwo, Point3D positionInPlaceTwo){
		contectedPlaces = new ArrayList<PlaceAndPosition>();
		PlaceAndPosition temp = new PlaceAndPosition(placeOne, positionInPlaceOne);
		contectedPlaces.add(temp);
		temp = new PlaceAndPosition(placeTwo, positionInPlaceTwo);
		contectedPlaces.add(temp);
	}


	public abstract String getName();

	public abstract String getImageName();

	public abstract boolean isLocked();

	public abstract boolean unlock(Inventory inventory);
	
	public Point3D getPosition(){
		return contectedPlaces.get(0).position;
	}
	
	public Point3D getPosition(Place place){
		for(PlaceAndPosition other: contectedPlaces){
			if (other.place.getName().equals(place.getName())){
				return other.position;
			}
		}
		return contectedPlaces.get(0).position;
	}

	/**
	 * Should return the places counter part.
	 * @param place - place that needs to be found.
	 * @return - return the other place - null if the place is is not found..... imposable.
	 */
	public Place getOtherPlace(Place place) {
		for (PlaceAndPosition otherPlace: contectedPlaces){
			if (!place.equals(otherPlace.place)){
				return otherPlace.place;
			}
		}
		return null;
	}
	
	private class PlaceAndPosition{
		public final Place place;
		public final Point3D position;
		public PlaceAndPosition(Place place, Point3D position){
			this.place = place;
			this.position = position;
		}
	}
}
