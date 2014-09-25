package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.util.List;

/**
 * Door.
 * Is a way to exit a place should always have two exits.
 * @author Shane Brewer
 *
 */
public class Door extends Exit{

	private final String name;
	private final String imageName;
	private final Point3D position;
	private final Rectangle3D boundingBox;
	public Door(List<Place> places, String name, Point3D position, Rectangle3D size) {
		super(places);
		this.name = name;
		this.imageName = "Door";
		this.position = position;
		this.boundingBox = size;
	}

	@Override
	public Point3D getPosition() {
		return position;
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


}
