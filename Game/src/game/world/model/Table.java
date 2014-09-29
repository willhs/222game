package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Table implements Item{

	private final String name;
	private Point3D position;
	private final Rectangle3D boundingBox;

	public Table(String name, Point3D posistion, Rectangle3D size){
		this.name = name;
		this.position = posistion;
		this.boundingBox = size;
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
		return false;
	}

	@Override
	public boolean canDrop() {
		return false;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getImageName() {
		return "Table";
	}

	@Override
	public void setPosition(Point3D point) {
		position = point;
	}


}
