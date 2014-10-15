package test.world.util;

import java.awt.Polygon;
import java.util.*;

import game.world.dimensions.Point3D;
import game.world.logic.MovementHandler;
import game.world.model.*;

/**
 *
 * @author Shane Brewer 300289850
 *
 */
public class DoubleRoomWorldTest {
	public World world;

	public DoubleRoomWorldTest() {
		int[] xpoints = new int[] { 200, 800, 800, 200 };
		int[] ypoints = new int[] { 200, 200, 800, 800 };
		Polygon firstFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> firstItems = new ArrayList<Item>();
		Room firstRoom = new Room(firstItems, new ArrayList<Enviroment>(), firstFloor, "Room1");
		Polygon secontFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> secondItems = new ArrayList<Item>();
		Room secondRoom = new Room(secondItems, new ArrayList<Enviroment>(), secontFloor, "Room2");

		List<Place> places = new ArrayList<Place>();
		places.add(firstRoom);
		places.add(secondRoom);

		Exit exit = new Portal("Door1", firstRoom, new Point3D(400, 0, 400),
				secondRoom, new Point3D(400, 0, 500));

		world = new World(places);
		world.addExit(exit);
		firstRoom.addExit(exit);
		secondRoom.addExit(exit);

	}

	public World getWorld(){
		return world;
	}

}
