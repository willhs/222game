package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Sword extends Item{
	
	private final Rectangle3D boundingBox = new Rectangle3D(5,5,10);
	
	public Sword (String name, Point3D position){
		super(name, position);
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
	public boolean canPickUp() {
		return true;
	}

	@Override
	public boolean canDrop() {
		return true;
	}

	@Override
	public boolean canUse(){
		return false;
	}
	
}
