package game.world.model;

import game.world.dimensions.*;
import game.world.util.Floor;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	//private final Dimension dimension;
	private final Polygon floor;


	public Room (List<Exit> exits, List<Item> items, Polygon floor){
		this.exits = exits;
		this.players = new ArrayList<Player>();
		this.items = items;
		this.floor = floor;
	}

	@Override
	public Iterator<Exit> getExits() {
		return exits.iterator();
	}

	@Override
	public Iterator<Item> getItems() {
		return items.iterator();
	}

	@Override
	public Iterator<Player> getPlayers() {
		return players.iterator();
	}

	@Override
	public boolean contains(Point point) {
		return false;//dimension.contains(point.x, point.y);
	}

	@Override
	public boolean contains(Point3D point) {
		return false;//dimension.contains(point.getX(), point.getZ());
	}

	
}
