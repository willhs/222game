package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.logic.MovementHandler;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private final String name;
	private final List<Enviroment> enviroment;

	public Room(List<Item> items, List<Enviroment> enviroment, Polygon floor, String name) {
		this.exits = new ArrayList<Exit>();
		this.players = new ArrayList<Player>();
		this.items = items;
		this.floor = floor;
		this.name = name;
		this.enviroment = enviroment;
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addExit(Exit exit) {
		exits.add(exit);
	}

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
		Room other = (Room) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public Iterator<Enviroment> getEnviroment() {
		return enviroment.iterator();
	}

}
