package game.world.model;

import game.world.dimensions.*;
import game.world.logic.MovementHandler;
import game.world.util.*;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.*;

/**
 * Place Defines what it is to be a place in the world
 *
 * @author Shane Brewer 300289850
 *
 */
public abstract class Place implements Serializable {

	private final List<Exit> exits;
	private final List<Player> players;
	private final List<Item> items;
	private final List<Enviroment> enviroment;
	private final Polygon floor;
	private final String name;

	public Place(String name, Polygon floor, List<Enviroment> enviroment,
			List<Item> items) {
		this.exits = new ArrayList<Exit>();
		this.players = new ArrayList<Player>();
		this.items = items;
		this.floor = floor;
		this.name = name;
		this.enviroment = enviroment;
	}

	/**
	 * Checks that a rectangle is withing the space of the room.
	 *
	 * @param point
	 *            - 3dPoint that represents the middle point of the "Thing" in
	 *            3d space.
	 * @param rectangle3D
	 *            - the bounding box just width and height and length.
	 * @return - returns true only if the place contains the point.
	 */
	public boolean contains(Point3D point, Rectangle3D rectangle3D) {
		Rectangle3D newRec = rectangle3D.apply3Dpoint(point);
		// checks that all 4 points of the rectangle are in the bounds of the 
		// place.
		if (contains(new Point3D(newRec.getX(), newRec.getY(), newRec.getZ()))
				&& contains(new Point3D(newRec.getX() + newRec.getWidth(),
						newRec.getY(), newRec.getZ()))
				&& contains(new Point3D(newRec.getX() + newRec.getWidth(),
						newRec.getY(), newRec.getZ() + newRec.getLength()))
				&& contains(new Point3D(newRec.getX(), newRec.getY(),
						newRec.getZ() + newRec.getLength()))) {
			return true;
		}
		return false;
	}

	/**
	 * Sets the player a point. Should only be used if the player is not in any
	 * place.
	 *
	 * @param player
	 *            - to be placed.
	 * @return - returns true if placed.
	 */
	public boolean setStartPoint(Player player) {
		players.add(player);
		for (float x = 0; x < floor.getBounds().x + floor.getBounds().width; x+=1f) {
			for (float z = 0; z < floor.getBounds().y + floor.getBounds().height; z+=1f) {
				// Will return true when a sutable point in the place is found to put the player.
				if (MovementHandler.playerMove(player, new Point3D(x, 0, z),
						this)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the floor of the room a floor is a arraylist of 3D points
	 *
	 * @return - the floor.
	 */
	public Floor getFloor() {
		int[] x = floor.xpoints;
		int[] z = floor.ypoints;
		Point3D[] points = new Point3D[x.length];
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], 0, z[i]);
		}
		return new Floor(points);
	}

	/**
	 * Gets all the drawable items in the room.
	 *
	 * @return - returns a iterator over all the drawable things in the room.
	 */
	public Iterator<Drawable> getDrawable() {
		List<Drawable> drawables = new ArrayList<Drawable>(exits);
		drawables.addAll(players);
		drawables.addAll(items);
		drawables.addAll(enviroment);
		return drawables.iterator();
	}

	/**
	 * Checks weather a point is in a place or not.
	 *
	 * @param point
	 *            - point to be checked
	 * @return - returns true if the point is within the place.
	 */
	public boolean contains(Point point) {
		return floor.contains(point);
	}

	/**
	 * Checks weather a point in the 3D space is in the place
	 *
	 * @param point
	 *            - 3D point is space.
	 * @return - true if it is in the place.
	 */
	public boolean contains(Point3D point) {
		return floor.contains(point.getX(), point.getZ());
	}

	/**
	 * Should return the iterator of exits that the place has. Exits can't be
	 * remove from a room.
	 *
	 * @return - Iterator of exits.
	 */
	public Iterator<Exit> getExits() {
		return exits.iterator();
	}

	/**
	 * Should return the list of Items, items can be remove form the place to
	 * something that is in that place for example a container or player.
	 *
	 * @return - List of items currently in the place.
	 */
	public Iterator<Item> getItems() {
		return items.iterator();
	}

	/**
	 * Should return a list of players that are in a room player can move from
	 * room to room so player can be removed and added to the list.
	 *
	 * @return - list of players.
	 */
	public Iterator<Player> getPlayers() {
		return players.iterator();
	}


	/**
	 * Removes the item from the room.
	 *
	 * @param item
	 *            - item to be removed.
	 */
	public void removeItem(Item item) {
		items.remove(item);
	}

	/**
	 * Adds a item to the room
	 *
	 * @param item
	 *            - item to be added.
	 */
	public void addItem(Item item) {
		items.add(item);
	}

	/**
	 * Removes a player from the room.
	 *
	 * @param player
	 *            - player to be removed.
	 */
	public void removePlayer(Player player) {
		players.remove(player);
	}

	/**
	 * Adds a player to the place.
	 *
	 * @param player
	 *            - player to be added.
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}

	/**
	 * Gets the name of the place.
	 *
	 * @return - returns the name of the place this should be unique to this
	 *         place.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds the exit to the place.
	 *
	 * @param exit
	 *            - the exit to be added
	 */
	public void addExit(Exit exit) {
		exits.add(exit);
	}

	/**
	 * Checks if a place coentain a item.
	 */
	public boolean containsItem(Item item){
		return items.contains(item);
	}

	/**
	 * Checks if a place coentain a player.
	 */
	public boolean containsPlayer(Player player){
		return players.contains(player);
	}	

	/**
	 * Gets the enviroment for the place.
	 *
	 * @return - the enviroment iterator.
	 */
	public Iterator<Enviroment> getEnviroment() {
		return enviroment.iterator();
	}
//========================Hash and equals=================================//
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
