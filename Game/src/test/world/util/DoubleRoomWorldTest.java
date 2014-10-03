package test.world.util;

import java.awt.Polygon;
import java.util.*;

import game.world.dimensions.Point3D;
import game.world.logic.MovementHandler;
import game.world.model.*;

public class DoubleRoomWorldTest {
	public World world;

	public DoubleRoomWorldTest() {
		int[] xpoints = new int[] { 200, 800, 800, 200 };
		int[] ypoints = new int[] { 200, 200, 800, 800 };
		Polygon firstFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> firstItems = new ArrayList<Item>();
		Room firstRoom = new Room(firstItems, firstFloor, "Room1");
		Polygon secontFloor = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> secondItems = new ArrayList<Item>();
		Room secondRoom = new Room(secondItems, secontFloor, "Room2");

		List<Place> places = new ArrayList<Place>();
		places.add(firstRoom);
		places.add(secondRoom);

		Exit exit = new Door("Door1", firstRoom, new Point3D(200, 0, 400),
				secondRoom, new Point3D(400, 0, 500));
		World world = new World(places);
		world.addExit(exit);
		firstRoom.addExit(exit);
		secondRoom.addExit(exit);

		System.out.println(exit.getPosition(firstRoom));
		System.out.println();
		System.out.println(exit.getPosition(secondRoom));
		System.out.println();

		Player player = new Player("Jim");
		world.addPlayerToGameWorld(player);

		System.out.println(player.name);
		System.out.println(player.getPosition());
		System.out.println();

		System.out.println(world.getPlaceOfPlayer(player).getName());
		System.out.println();

		System.out.println(MovementHandler.playerMove(player, new Point3D(226, 0,
				426), world.getPlaceOfPlayer(player)));

		System.out.println();
		System.out.println(MovementHandler.exitPlace(player, firstRoom, exit));

		System.out.println();
		System.out.println(world.getPlaceOfPlayer(player).getName());

		System.out.println();
		System.out.println(player.getPosition());



	}

	public static void main(String[] args) {
		DoubleRoomWorldTest world = new DoubleRoomWorldTest();
	}
}
