package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;


/**
 * @author Shane Brewer 300289850
 */
public class Chest extends Container{
	
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
		return getContents().isEmpty() ? "OpenChest" : "Chest";
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
