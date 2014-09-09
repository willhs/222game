package game.world.model;

import java.util.*;

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
	public List<Item> getItems();
	
	/**
	 * Should return a list of players that are in a room 
	 * player can move from room to room so player can be 
	 * removed and added to the list.
	 * @return - list of players.
	 */
	public List<Player> getPlayers();
	
	
}
