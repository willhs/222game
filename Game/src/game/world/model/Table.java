package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Table implements Item{

	private final String name;
	private Point3D position;
	private final Rectangle3D boundingBox;
	private boolean selected;

	public Table(String name, Point3D posistion, Rectangle3D size){
		this.name = name;
		this.position = posistion;
		this.boundingBox = size;
	}
	
	public Table(String name, Point3D position){
		this.name = name;
		this.position = position;
		this.boundingBox = new Rectangle3D(50, 50, 50);
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
