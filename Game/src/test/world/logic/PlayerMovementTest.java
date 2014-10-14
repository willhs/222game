package test.world.logic;

import static org.junit.Assert.*;
import game.world.dimensions.Point3D;
import game.world.logic.MovementHandler;
import game.world.model.*;

import org.junit.Test;

import test.world.util.DoubleRoomWorldTest;
import test.world.util.SingleRoomWorldTest;

/**
 * @author Shane Brewer 300289850
 */
public class PlayerMovementTest {

	/**
	 * Tests that a player can move to a place that is free.
	 */
	@Test
	public void movementTest() {
		World world = makeWorld();
		world.addPlayerToGameWorld(new Player("Jim"));

		Player player =  world.getPlayers().next();
		Place place = world.getPlaces().next();
		Point3D point = player.getPosition();

		for (int i = 0; i < 1; i++){
			MovementHandler.playerMove(player, new Point3D(point.x+30, point.y, point.z), place);
		}

		if (point.x == player.getPosition().x){
			fail("Player should have moved");
		}
	}

	/**
	 * Test that a player should not beable to move outside a room.
	 */
	@Test
	public void failedMovmentTest(){
		World world = makeWorld();
		world.addPlayerToGameWorld(new Player("Jim"));

		Player player =  world.getPlayers().next();
		Place place = world.getPlaces().next();

		assertFalse(MovementHandler.playerMove(player, new Point3D(0,0,0), place));
	}

	/**
	 * Test that a player can exit a room
	 */
	@Test
	public void exitTest(){
		DoubleRoomWorldTest test = new DoubleRoomWorldTest();

		World world = test.getWorld();

		Player player = new Player("Jim");
		world.addPlayerToGameWorld(player);

		Place place = world.getStartPlace();


		MovementHandler.playerMove(player, new Point3D(435,0, 435), place);

		assertTrue(MovementHandler.exitPlace(player, place, place.getExits().next()));
	}

	/**
	 * Tests that the player cant enter a portal from anywhere.
	 */
	@Test
	public void failedExitTest(){
		DoubleRoomWorldTest test = new DoubleRoomWorldTest();

		World world = test.getWorld();

		Player player = new Player("Jim");
		world.addPlayerToGameWorld(player);

		Place place = world.getStartPlace();

		assertFalse(MovementHandler.exitPlace(player, place, place.getExits().next()));
	}

	private World makeWorld(){
		return new SingleRoomWorldTest().world;
	}

}
