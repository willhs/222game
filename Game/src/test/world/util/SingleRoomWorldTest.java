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
		int[] xpoints = new int[]{0,200,200,0,0};
		int[] ypoints = new int[]{0,0,200,200,0};
		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		Room room = new Room(new ArrayList<Exit>(), new ArrayList<Item>(), p);
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);
		
		world = new World(new ArrayList<Player>(),rooms);
	}
}
