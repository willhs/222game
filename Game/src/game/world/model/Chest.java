package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Chest extends Container{
	
	
	private static final long serialVersionUID = -2847746894980423704L;
	
	/**
	 * Constrcts the Chest.
	 * @param name - name that is to be unique to all other things in the world.
	 * @param contents - the contents of the chest.
	 * @param position - the position in the room that the chest is in.
	 */
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
