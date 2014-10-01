package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Key implements Item {

	private static final Rectangle3D boundingBox = new Rectangle3D(0.5f, 0.5f, 0.5f);
	private final String name;
	private final String imageName;
	private Point3D position;

	//==================Constructor=================//
	public Key(String name,
			Point3D position) {
		super();
		this.name = name;
		this.imageName = "Key";
		this.position = position;
	}
	//====================End=======================//
	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public boolean canPickUp() {
		return true;
	}

	@Override
	public boolean canDrop() {
		return true;
	}

	@Override
	public void setPosition(Point3D point) {
		position = point;
	}
	@Override
	public Point3D getPosition(Place place) {
		return getPosition();
	}

}
