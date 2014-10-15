package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

/**
 * End game protal hard to open
 * @author Shane Brewer 300289850
 */
public class FinishPortal extends  LockedPortal{

	public FinishPortal(String name, Place placeOne,
			Point3D positionInPlaceOne, Place placeTwo,
			Point3D positionInPlaceTwo) {
		super(name, placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo,
				30);
	}

	@Override
	public String getImageName(){
		return "FinishPortal";
	}

	@Override
	public Rectangle3D getBoundingBox(){
		return new Rectangle3D(100,200,100);
	}
}
