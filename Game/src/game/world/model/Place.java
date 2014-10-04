package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Point;
import java.util.Iterator;

/**
 * Place
 * Defines what it is to be a place in the world
 * @author Shane Brewer
 *
 */
public interface Place {

	/**
	 * Should return the iterator of exits that the place has.
	 * Exits can't be remove from a room.
	 * @return - Iterator of exits.
	 */
	public Iterator<Exit> getExits();

	/**
	 * Should return the list of Items, items can be remove
	 * form the place to something that is in that place for
	 * example a container or player.
	 * @return - List of items currently in the place.
	 */
	public Iterator<Item> getItems();

	/**
	 * Should return a list of players that are in a room
	 * player can move from room to room so player can be
	 * removed and added to the list.
	 * @return - list of players.
	 */
	public Iterator<Player> getPlayers();

	/**
	 * Gets all the drawable items in the room.
	 * @return - returns a iterator over all the drawable things in the room.
	 */
	public Iterator<Drawable> getDrawable();

	/**
	 * Checks weather a point is in a place or not.
	 * @param point - point to be checked
	 * @return - returns true if the point is within the place.
	 */
	public boolean contains(Point point);

	/**
	 * Checks weather a point in the 3D space is in the place
	 * @param point - 3D point is space.
	 * @return - true if it is in the place.
	 */
	public boolean contains(Point3D point);

	/**
	 * Returns the floor of the room a floor is a arraylist of 3D points
	 * @return - the floor.
	 */
	public Floor getFloor();

	/**
	 * Checks that a rectangle is withing the space of the room.
	 * @param point - 3dPoint that represents the middle point of the "Thing" in 3d space.
	 * @param rectangle3D - the bounding box just width and height and length.
	 * @return - returns true only if the place contains the point.
	 */
	public boolean contains(Point3D point, Rectangle3D rectangle3D);

	/**
	 * Sets the player a point.
	 * Should only be used if the player is not in any place.
	 * @param player - to be placed.
	 * @return - returns true if placed.
	 */
	public boolean setStartPoint(Player player);

	/**
	 * Removes the item from the room.
	 * @param item - item to be removed.
	 */
	public void removeItem(Item item);

	/**
	 * Adds a item to the room
	 * @param item - item to be added.
	 */
	public void addItem(Item item);

	/**
	 * Removes a player from the room.
	 * @param player - player to be removed.
	 */
	public void removePlayer(Player player);

	/**
	 * Adds a player to the place.
	 * @param player - player to be added.
	 */
	public void addPlayer(Player player);

	/**
	 * Gets the name of the place.
	 * @return - returns the name of the place this should be unique to this place.
	 */
	public String getName();

	/**
	 * Adds the exit to the place.
	 * @param exit - the exit to be added
	 */
	public void addExit(Exit exit);

	/**
	 * Gets the enviroment for the place.
	 * @return - the enviroment iterator.
	 */
	public Iterator<Enviroment> getEnviroment();
}
