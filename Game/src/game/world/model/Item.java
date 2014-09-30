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
public interface Item extends Drawable, Serializable{

	/**
	 * All items have names and you need to be able to get them.
	 *
	 * @return - returns the name of the item.
	 */
	public String getName();

	/**
	 * Gets the position of the item in a 3D space. if the items is in a
	 * container it has no position in the game currently the return value will
	 * be null.
	 *
	 * @return Point3D - returns the items position is 3D space if it is in an
	 *         container or inventory null will be returned..
	 */
	public Point3D getPosition();

	/**
	 * Tells you if the item can be picked up.
	 *
	 * @return - returns true if the items can be picked up.
	 */
	public boolean canPickUp();

	/**
	 * Tells if the item can be droped.
	 *
	 * @return - returns true if the item can be droped.
	 */
	public boolean canDrop();

	/**
	 * Used to se the new point off the item.
	 * @param point3d  the point that is to be set
	 */
	public void setPosition(Point3D point);

}
