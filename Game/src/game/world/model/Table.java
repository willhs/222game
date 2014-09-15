package game.world.model;

import game.world.dimensions.*;

public class Table implements Item{

	private final String name;
	private final Point3D position;
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
	
}
