package game.world.model;

import game.world.dimensions.Dimension;

import java.awt.Point;
import java.util.*;

/**
 * Room Class 
 * This class defines every thing to do with a room.
 * @author Shane Brewer
 *
 */
public class Room implements Place{

	private final List<Exit> exits;
	private final List<Player> players;
	private final List<Item> items;
	private final Dimension dimension;
	
	
	public Room (List<Exit> exits, List<Item> items, Dimension dimension){
		this.exits = exits;
		this.players = new ArrayList<Player>();
		this.items = items;
		this.dimension = dimension;
	}
	
	@Override
	public Iterator<Exit> getExits() {
		return exits.iterator();
	}

	@Override
	public List<Item> getItems() {
		return items;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public boolean isIn(Point point) {
		return dimension.contains(point.x, point.y);
	}
	
}
