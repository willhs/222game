package test.world;

import static org.junit.Assert.fail;
import game.world.model.Enviroment;
import game.world.model.Exit;
import game.world.model.Item;
import game.world.model.Player;
import game.world.model.Room;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

public class RoomTests {

	@Test
	public void chcekGetterTests1() {
		Room room = getARoom();
		if (room.getExits() == null) {
			fail("A iterator of exits should never be null");
		}
		Iterator<Exit> iterator = room.getExits();
		while (iterator.hasNext()) {
			fail("No exits were pervided to the room");
		}
	}

	@Test
	public void chcekGetterTests2() {
		Room room = getARoom();
		if (room.getPlayers() == null) {
			fail("A list of players should never be null");
		}
		Iterator<Player> iterator = room.getPlayers();
		while (iterator.hasNext()) {
			fail("No player were added to the room");
		}
	}

	@Test
	public void chcekGetterTests3() {
		Room room = getARoom();
		if (room.getItems() == null) {
			fail("A List of items should never be null");
		}
		Iterator<Item> iterator = room.getItems();
		while (iterator.hasNext()) {
			fail("No Items were previded or added to the room");
		}
	}

	// ===============HelperMethods================//
	public Room getARoom() {
		int[] xpoints = new int[]{0,200,200,0,0};
		int[] ypoints = new int[]{0,0,200,200,0};
		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		return new Room(new ArrayList<Item>(), new ArrayList<Enviroment>() , p, "Room1");
	}

}
