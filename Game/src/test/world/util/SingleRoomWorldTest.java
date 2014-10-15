
package test.world.util;

import game.world.dimensions.*;
import game.world.model.*;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shane Brewer 300289850
 *
 */
public class SingleRoomWorldTest {

	public World world;

	/**
	 * Makes a simple world that has nothing but a world in it for testing by others.
	 */
	public SingleRoomWorldTest(){
		int[] xpoints = new int[]{200,800,800,200};
		int[] ypoints = new int[]{200,200,800,800};

		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		items.add(new Table("Table1", new Point3D(700, 0, 700), new Rectangle3D(50, 50, 50)));
		Room room = new Room(items, new ArrayList<Enviroment>(),  p, "Room1");
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);

		world = new World(rooms);
	}

}
