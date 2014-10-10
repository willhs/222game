package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.*;

public class Crystal implements Item {

	private static final Rectangle3D boundingBox = new Rectangle3D(10f, 10f, 10f);
	private final String name;
	private final String imageName;
	private Point3D position;
	private boolean selected;

	//==================Constructor=================//
	public Crystal(String name,
			Point3D position) {
		super();
		this.name = name;
		this.imageName = ItemImageNames.crystalNames[(int)(ItemImageNames.crystalNames.length*Math.random())];
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
	@Override
	public boolean isSlelected() {
		return selected;
	}
	@Override
	public void setSelected(boolean change) {
		selected = change;
	}

}
