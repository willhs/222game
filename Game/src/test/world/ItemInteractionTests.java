package test.world;


import static org.junit.Assert.*;

import java.util.Iterator;

import game.world.dimensions.Point3D;
import game.world.logic.ItemInteractionHandler;
import game.world.logic.MovementHandler;
import game.world.model.*;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;



/**
 *
 * @author Shane Brewer 300289850
 *
 */
public class ItemInteractionTests{

	@Test
	/**
	 * Checks if items can be picked up.
	 */
	public void itemPickUpTests(){
		World world = new SingleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);
		MovementHandler.playerMove(player, new Point3D(745, 0 ,745), world.getStartPlace());


		Iterator<Item> items = world.getStartPlace().getItems();


		assertTrue(ItemInteractionHandler.pickupItem(player, items.next() , world.getStartPlace()));
	}

	@Test
	/**
	 * Checks if items should not be picked up are picked up.
	 */
	public void failedItemPickUpTests(){
		World world = new SingleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);

		Iterator<Item> items = world.getStartPlace().getItems();

		assertFalse(ItemInteractionHandler.pickupItem(player, items.next() , world.getStartPlace()));
	}

	@Test
	/**
	 * Checks that an item can be droped.
	 */
	public void itemDropTests(){
		World world = new SingleRoomWorldTest().world;
		Player player = new Player("Jim");

		world.addPlayerToGameWorld(player);
		System.out.println(player.getPosition());
		Item item = new Crystal("ccc2", new Point3D(0,0,0));
		player.addItem(item);

		assertTrue(ItemInteractionHandler.dropItem(player, item, world.getStartPlace()));
	}


}