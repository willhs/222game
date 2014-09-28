
package test.world.util;

import game.ui.render.util.Trixel;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Exit;
import game.world.model.Inventory;
import game.world.model.Item;
import game.world.model.Key;
import game.world.model.Place;
import game.world.model.Player;
import game.world.model.Room;
import game.world.model.Table;
import game.world.model.World;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shane Brewer
 *
 */
public class SingleRoomWorldTest {

	public World world;

	/**
	 * Makes a simple world that has nothing but a world in it for testing by others.
	 */
	public SingleRoomWorldTest(){
		int[] xpoints = new int[]{200,400,400,200};
		int[] ypoints = new int[]{200,200,400,400};
		//int[] xpoints = new int[]{0, 400, 400, 0};
		//int[] ypoints = new int[]{0, 0, 400, 400};

		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		items.add(new Table("Table1", new Point3D(250, 0, 250), new Rectangle3D(50, 50, 50)));
		Room room = new Room(new ArrayList<Exit>(), items, p, "Room1");
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);

		world = new World(new ArrayList<Player>(),rooms);
		Player player = new Player("Jim", new Inventory(), new Point3D(0, 0, 0), new Rectangle3D(20, 20, 20));
		// first test of new method in the game world class that adds a player to the world.

		world.addPlayerToGameWorld(player);

		player.getInventory().addItem(new Key(new Rectangle3D(0.5f, 0.5f, 0.5f), "Key1", new Point3D(0,0,0)));
	}

	public static void main(String[] args) {
		SingleRoomWorldTest n = new SingleRoomWorldTest();
	}
}
