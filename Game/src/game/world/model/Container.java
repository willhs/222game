package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;


/**
 * Container
 * defines what it is to be a container in this game.
 * @author Shane Brewer
 *
 */
public abstract class Container extends Item{
	
	private final Inventory contents;
	private final Rectangle3D boundingBox;
	protected boolean isLocked;
	
	public Container (String name, Inventory contents, Rectangle3D boundingBox, Point3D position){
		super(name, position);
		this.contents = contents;
		this.boundingBox = boundingBox;
	}
	/**
	 * Used for getting the contens of the chest.
	 * @return - returns the contents of the chest.
	 */
	public Inventory getContents(){
		return contents;
	}
	
	/**
	 * Used for checking if the chest is locked.
	 * @return - return true if the chest is locked.
	 */
	public abstract boolean isLocaked();
	
	/**
	 * Used for unlocking the container 
	 * @param playersInventory - checks the players invenory for the right number of keys needed.
	 * @return - true if the container is unlocked or was unlocked.
	 */
	public abstract boolean unlock(Inventory playersInventory);
	
	@Override
	public Rectangle3D getBoundingBox(){
		return boundingBox;
	}
}
