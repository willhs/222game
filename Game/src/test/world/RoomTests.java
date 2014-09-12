package test.world;

import static org.junit.Assert.*;
import game.world.dimensions.Dimension;
import game.world.dimensions.Rectangle;
import game.world.model.*;

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
		return new Room(new ArrayList<Exit>(), new ArrayList<Item>(), new Rectangle(0,0,200,200));
	}

}
