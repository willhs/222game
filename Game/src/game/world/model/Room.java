package game.world.model;

import game.world.dimensions.*;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Point;
import java.awt.Polygon;
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
	public Iterator<Drawable> getDrawable() {
		List<Drawable> drawables = new ArrayList<Drawable>(exits);
		drawables.addAll(players);
		drawables.addAll(items);
		return drawables.iterator();
	}

	@Override
	public boolean contains(Point point) {
		return floor.contains(point);
	}

	@Override
	public boolean contains(Point3D point) {
		return floor.contains(point.getX(), point.getZ());
	}

	@Override
	public Floor getFloor() {
		int[] x = floor.xpoints;
		int[] z = floor.ypoints;
		Point3D[] points = new Point3D[x.length];
		for (int i =0; i < x.length; i++){
			points[i] = new Point3D(x[i], 0, z[i]);
		}
		return new Floor(points);
	}
	
}
