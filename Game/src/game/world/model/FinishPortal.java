package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Exit.PlaceAndPosition;

/**
 * End game protal hard to open
 * @author Shane Brewer 300289850
 */
public class FinishPortal extends  LockedPortal{

	private Place place;
	private int lockNumber;
	private Point3D position;

	public FinishPortal(String name, Place placeOne,
			Point3D positionInPlaceOne, int lockNumber) {
		super(name, placeOne, positionInPlaceOne, placeOne, positionInPlaceOne,
				lockNumber);
		place = placeOne;
		this.position = positionInPlaceOne;
		this.lockNumber = lockNumber;
	}

	@Override
	public String getImageName(){
		return "FinishPortal";
	}

	@Override
	public Rectangle3D getBoundingBox(){
		return new Rectangle3D(50,50,50);
	}

	@Override
	public Place getOtherPlace(Place place){
		return this.place;
	}

	/**
	 * Returns the number of crystals required to power it.
	 * @return - number of cyrstals to power.
	 */
	public int getLockNumber(){
		return lockNumber;
	}

	@Override
	public Point3D getPosition(Place place){
		return position;
	}

	@Override
	public Point3D getPosition(){
		return position;
	}

}
