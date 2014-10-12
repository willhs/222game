package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.util.List;

/**
 * Door. Is a way to exit a place should always have two exits.
 *
 * @author Shane Brewer 300289850
 *
 */
public class Portal extends Exit {

	private final String name;
	private final String imageName;
	private static final Rectangle3D boundingBox = new Rectangle3D(30, 60, 30);

	public Portal(String name, Place placeOne, Point3D positionOne, Place placeTwo, Point3D positionTwo) {
		super(placeOne, positionOne, placeTwo, positionTwo);
		this.name = name;
		this.imageName = "teleporter_on";
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public boolean isLocked() {
		return false;
	}

	@Override
	public boolean unlock(Inventory inventory) {
		return false;
	}

	@Override
	public void setLocked(boolean change) {
	}

}
