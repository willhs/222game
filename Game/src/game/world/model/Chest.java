package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Chest extends Container{
	
	
	public Chest(String name, Inventory contents, Point3D position) {
		super(name, contents, new Rectangle3D(30, 30, 30) ,position);
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
	public String getImageName() {
		return "Chest";
	}

	@Override
	public Point3D getPosition(Place place) {
		return getPosition();
	}

	@Override
	public boolean isLocaked() {
		return isLocked;
	}

	@Override
	public boolean unlock(Inventory playersInventory) {
		return true;
	}

}
