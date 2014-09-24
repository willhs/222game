package test.world.util;

import java.awt.Polygon;
import java.util.*;

import game.world.model.*;
import game.world.dimensions.*;

public class SingleRoomWorldTest {

	public World world;

	/**
	 * Makes a simple world that has nothing but a world in it for testing by others.
	 */
	public SingleRoomWorldTest(){
		int[] xpoints = new int[]{0,400,400,0};
		int[] ypoints = new int[]{0,0,400,400};
		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		items.add(new Table("Table1", new Point3D(100, 100, 100), new Rectangle3D(50, 50, 50)));
		Room room = new Room(new ArrayList<Exit>(), items, p);
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);

		world = new World(new ArrayList<Player>(),rooms);
	}
}
