package test.world;

import static org.junit.Assert.*;

import org.junit.Test;

import test.world.util.DoubleRoomWorldTest;
import game.world.dimensions.*;
import game.world.logic.ContainerInteractionHandler;
import game.world.logic.MovementHandler;
import game.world.model.*;


/**
 *
 * @author Shane Brewer 300289850
 *
 */
public class ContainerHandlerTest{

	@Test
	/**
	 * Checks if the player can get items from a chest.
	 */
	public void lootingContianerTest(){
		World world = new DoubleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);

		MovementHandler.playerMove(player, new Point3D(440,0,440), world.getStartPlace());
		Exit exit = world.getStartPlace().getExits().next();

		MovementHandler.exitPlace(player,  world.getStartPlace(), exit);

		Place place = exit.getOtherPlace(world.getStartPlace());
		world.getPlaceOfPlayer(player).equals(place);

		MovementHandler.playerMove(player, new Point3D(265,0,265), place);
		assertTrue(ContainerInteractionHandler.getItemsFromContainer(player, (Container)place.getItems().next(), place));

	}

	@Test
	public void cantLootContainersTest(){
		World world = new DoubleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);

		MovementHandler.playerMove(player, new Point3D(440,0,440), world.getStartPlace());
		Exit exit = world.getStartPlace().getExits().next();

		MovementHandler.exitPlace(player,  world.getStartPlace(), exit);

		Place place = exit.getOtherPlace(world.getStartPlace());
		world.getPlaceOfPlayer(player).equals(place);

		assertFalse(ContainerInteractionHandler.getItemsFromContainer(player, (Container)place.getItems().next(), place));

	}

}