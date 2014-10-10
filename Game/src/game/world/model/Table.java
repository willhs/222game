package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

/**
 * @author Shane Brewer 300289850
 */
public class Table extends Item{

	private final Rectangle3D boundingBox;

	public Table(String name, Point3D posistion, Rectangle3D size){
		super(name, posistion);
		this.boundingBox = size;
	}

	public Table(String name, Point3D position){
		super(name, position);
		this.boundingBox = new Rectangle3D(50, 50, 50);
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
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getImageName() {
		return "Table";
	}

	@Override
	public boolean canUse(){
		return false;
	}


}
