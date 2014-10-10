package game.world.model;

import game.world.dimensions.Point3D;
import game.world.util.Drawable;

import java.io.Serializable;

/**
 * Item.
 * All items need to be some where eather in a place or a container or a
 * inventory
 *
 * @author Shane Brewer
 *
 */
public abstract class Item implements Drawable, Serializable{

	private final String name;
	private Point3D position;
	private boolean isSlelected;

	public Item (String name, Point3D position){
		this.name = name;
		this.position = position;
	}

	/**
	 * All items have names and you need to be able to get them.
	 *
	 * @return - returns the name of the item.
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the position of the item in a 3D space. if the items is in a
	 * container it has no position in the game currently the return value will
	 * be null.
	 *
	 * @return Point3D - returns the items position is 3D space if it is in an
	 *         container or inventory null will be returned..
	 */
	public Point3D getPosition(){
		return position;
	}

	/**
	 * Tells you if the item can be picked up.
	 *
	 * @return - returns true if the items can be picked up.
	 */
	public abstract boolean canPickUp();

	/**
	 * Tells if the item can be droped.
	 *
	 * @return - returns true if the item can be droped.
	 */
	public abstract boolean canDrop();

	/**
	 * Used to se the new point off the item.
	 * @param point3d  the point that is to be set
	 */
	public void setPosition(Point3D point){
		position = point;
	}

	/**
	 * Check if an item is selected.
	 *
	 * @return - returns true if the item is selected.
	 */
	public boolean isSlelected(){
		return isSlelected;
	}

	/**
	 * Changes the value in the selected field.
	 *
	 * @param change - the value to change the field to.
	 */
	public void setSelected(boolean change){
		isSlelected = change;
	}

	/**
	 *Uses an item.
	 * @return - returns true if the item was used.
	 */
	public boolean canUse(){
		return false;
	}

	@Override
	public Point3D getPosition(Place place){
		return position;
	}

}
