package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

/**
 * End game protal hard to open
 * @author Shane Brewer 300289850
 */
public class FinishPortal extends  LockedPortal{

	private Place place;
	private int lockNumber;

	public FinishPortal(String name, Place placeOne,
			Point3D positionInPlaceOne, int lockNumber) {
		super(name, placeOne, positionInPlaceOne, placeOne, positionInPlaceOne,
				lockNumber);
		place = placeOne;
	}

	@Override
	public String getImageName(){
		return "FinishPortal";
	}

	@Override
	public Rectangle3D getBoundingBox(){
		return new Rectangle3D(100,200,100);
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

}
