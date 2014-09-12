package game.world.model;

import game.world.dimensions.Point3D;
import game.world.util.Floor;

import java.awt.Point;
import java.util.*;

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
	
}
