package game.world.model;

import game.world.dimensions.*;
import game.world.logic.MovementHandler;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Point;
import java.awt.Polygon;
import java.util.*;
import java.io.Serializable;

/**
 * Room Class This class defines every thing to do with a room.
 *
 * @author Shane Brewer
 *
 */
public class Room implements Place, Serializable {

	private final List<Exit> exits;
	private final List<Player> players;
	private final List<Item> items;
	private final Polygon floor;

	public Room(List<Exit> exits, List<Item> items, Polygon floor) {
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
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], 0, z[i]);
		}
		return new Floor(points);
	}

	@Override
	public boolean contains(Point3D point, Rectangle3D rectangle3d) {
		Rectangle3D newRec = rectangle3d.apply3Dpoint(point);
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

	@Override
	public boolean setStartPoint(Player player) {
		players.add(player);
		for (int x = 0; x < floor.getBounds().x+floor.getBounds().width; x++){
			for (int z = 0; z < floor.getBounds().y+floor.getBounds().height; z++){
				if (MovementHandler.playerMove(player, new Point3D(x, 0, z), this)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void removeItem(Item item) {
		items.remove(item);
	}

	@Override
	public void addItem(Item item) {
		items.add(item);
	}

	@Override
	public void removePlayer(Player player) {
		players.remove(player);
	}

	@Override
	public void addPlayer(Player player) {
		players.add(player);
	}

}
