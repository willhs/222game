package test.world.util;

import java.util.*;

import game.world.model.*;
import game.world.dimensions.*;

public class SingleRoomWorldTest {
	
	public World world;
	
	/**
	 * Makes a simple world that has nothing but a world in it for testing by others.
	 */
	public SingleRoomWorldTest(){
		Dimension d = new Complement(new Rectangle(0,0,200,200), new Rectangle(0,0,100,100));
		Room room = new Room(new ArrayList<Exit>(), new ArrayList<Item>(), d);
		List<Place> rooms = new ArrayList<Place>();
		rooms.add(room);
		
		world = new World(new ArrayList<Player>(),rooms);
	}
}
