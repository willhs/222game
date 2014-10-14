package test.world;

import static org.junit.Assert.*;

import java.util.Iterator;

import game.world.model.*;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;

public class WorldTests {

	@Test
	public void playerAddTest() {
		World world = new SingleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);

		Iterator<Player> players = world.getPlayers();
		while (players.hasNext()){
			if (!players.next().equals(player)){
				fail("the only player in the world should equal the other players.");
			}
		}
	}

	@Test
	public void playerAddTestTwo(){
		World world = new SingleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);

		Place place = world.getStartPlace();
		assertTrue(place.containsPlayer(player));
	}

}
