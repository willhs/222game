package test.world;

import static org.junit.Assert.*;
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
		for (Player player : room.getPlayers()) {
			fail("No player were added to the room");
		}
	}
	
	@Test
	public void chcekGetterTests3() {
		Room room = getARoom();
		if (room.getItems() == null) {
			fail("A List of items should never be null");
		}
		for (Item item: room.getItems()) {
			fail("No Items were previded or added to the room");
		}
	}

	// ===============HelperMethods================//
	public Room getARoom() {
		return new Room(new ArrayList<Exit>(), new ArrayList<Item>());
	}

}
