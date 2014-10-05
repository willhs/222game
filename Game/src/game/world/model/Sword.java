package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Sword implements Item{
	
	private final String name;
	private final Rectangle3D boundingBox = new Rectangle3D(5,5,10);
	private Point3D position;
	private boolean selected;
	
	public Sword (String name, Point3D position){
		this.name = name;
		this.position = position;
	}
	
	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getImageName() {
		return "Sword";
	}

	@Override
	public Point3D getPosition(Place place) {
		return position;
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
	public boolean isSlelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean change) {
		selected = change;
	}
	
}
