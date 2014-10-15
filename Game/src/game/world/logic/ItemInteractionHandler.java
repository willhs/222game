package game.world.logic;

import game.world.dimensions.Point3D;
import game.world.dimensions.*;
import game.world.model.*;

import java.util.Iterator;

/**
 *  Handles item interaction.
 * @author Shane Brewer 300289850
 *
 */
public class ItemInteractionHandler {

	private static final float PLAYER_ITEM_DISTANCE = 8;

	/**
	 * Used by the Game Event handler to have a Player pick up an item.
	 *
	 * @param player
	 *            - player that is to pick up the item
	 * @param item
	 *            - item to be picked up.
	 * @param place
	 *            - place the player and the item should be.
	 * @return - true if and only if the the player now has the item.
	 */
	public static boolean pickupItem(Player player, Item item, Place place) {
		// None of the paramters should be null
		if (player == null || item == null || place == null) {
			return false;
		}
		// the place must contain both the player and the item.
		if (!place.containsPlayer(player) && !place.containsItem(item)) {
			return false;
		}
		// the player must be withing range to pick up an item.
		if (!checkProximity(player.getPosition(), player.getBoundingBox(),
				item.getPosition(), item.getBoundingBox())) {
			return false;
		}
		// item must be able to be picked up.
		if (!item.canPickUp()) {
			return false;
		}
		// removes it from place and adds it to the player.
		place.removeItem(item);
		item.setPosition(new Point3D(0, 0, 0));
		player.addItem(item);
		return true;

	}

	/**
	 * Makes sure that the item and player are within a certain proximity of
	 * each other
	 *
	 * @param pointOne
	 *            - point of the player.
	 * @param boundingOne
	 *            - the bounding box around pointOne.
	 * @param pointTwo
	 *            - point of the item.
	 * @param boundingTwo
	 *            - the bounding box around pointTwo.
	 * @return - true if they are close enough
	 */
	public static boolean checkProximity(Point3D pointOne,
			Rectangle3D boundingOne, Point3D pointTwo, Rectangle3D boundingTwo) {
		if ((Math.abs(pointOne.x - pointTwo.x) <= PLAYER_ITEM_DISTANCE
				+ (boundingOne.getWidth() + boundingTwo.getWidth()) / 2)
				&& (Math.abs(pointOne.z - pointTwo.z) <= PLAYER_ITEM_DISTANCE
						+ (boundingOne.getLength() + boundingTwo.getLength())
						/ 2)) {
			return true;
		}
		return false;
	}

	/**
	 * Handles the droping of items in the game world.
	 *
	 * @param player
	 *            - the player that is droping the item.
	 * @param item
	 *            - the item that is to be droped.
	 * @param place
	 *            - the place that it is to be droped in.
	 * @return - true only if the item was droped propperly.
	 */
	public static boolean dropItem(Player player, Item item, Place place) {
		// None of the values should be null.
		if (player == null || item == null || place == null) {
			return false;
		}
		// Player must be in the place to drop the item there.
		if (!place.containsPlayer(player)) {
			return false;
		}
		// Check if the item can be droped.
		if (!item.canDrop()) {
			return false;
		}
		// Checks both that the item was in the inventory and is able to be remove.
		if (!player.getInventory().removeItem(item)) {
			return false;
		}
		// checks if it can fina the item a drop point if it can
		// then it will set that items point.
		if (!setItemDropPoint(player, item, place)) {
			player.addItem(item);
			return false;
		}
		// at this point the item can be added to the game world.
		place.addItem(item);
		return true;
	}

	/**
	 * Sets the items drop point.
	 *
	 * @param player
	 *            - the player that is to drop the item.
	 * @param item
	 *            - the item that is to be droped.
	 * @param place
	 *            - the place that the item will be droped.
	 * @return true if the item was droped there.
	 */
	private static boolean setItemDropPoint(Player player, Item item,
			Place place) {
		Rectangle3D rect = player.getBoundingBox().apply3Dpoint(
				player.getPosition(place));
		Rectangle3D itemRect = item.getBoundingBox().apply3Dpoint(
				item.getPosition());
		// Sets up the valsues for the x and z and xMax and yMax.
		int x = (int) (rect.x - PLAYER_ITEM_DISTANCE - itemRect.width / 2);
		int maxX = (int) (rect.x + rect.width + PLAYER_ITEM_DISTANCE + itemRect.width);
		int maxZ = (int) (rect.z + rect.length + PLAYER_ITEM_DISTANCE + itemRect.length);
		for (; x < maxX; x++) {
			int z = (int) (rect.z - PLAYER_ITEM_DISTANCE - itemRect.length / 2);
			for (; z < maxZ; z++) {
				//make a poistion for the item to be droped
				Point3D position = new Point3D(x, 0, z);
				// Makes a check if te tiem can be droped there.
				if (canItemBeHere(item, position, place)
						&& place.contains(position, item.getBoundingBox())) {
					//if it can then this is the items new position.
					item.setPosition(position);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the placement at this position is ok.
	 *
	 * @param currnetItem
	 *            - the current item.
	 * @param point
	 *            - the point at which the item needs to be placed.
	 * @param place
	 *            - the place in which the item should be droped.
	 * @return true if van be droped here.
	 */
	private static boolean canItemBeHere(Item currnetItem, Point3D point,
			Place place) {
		Iterator<Item> items = place.getItems();
		while (items.hasNext()) {
			Item item = items.next();
			if (item.getBoundingBox().collisionDetection(item.getPosition(),
					currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		Iterator<Exit> exits = place.getExits();
		while (exits.hasNext()) {
			Exit exit = exits.next();
			if (exit.getBoundingBox().collisionDetection(exit.getPosition(),
					currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		Iterator<Enviroment> enviroment = place.getEnviroment();
		while (enviroment.hasNext()) {
			Enviroment envir = enviroment.next();
			if (envir.getBoundingBox().collisionDetection(envir.getPosition(),
					currnetItem.getBoundingBox(), point) && envir.isCollidable()) {
				return false;
			}
		}
		Iterator<Player> players = place.getPlayers();
		while (players.hasNext()) {
			Player player = players.next();
			if (player.getBoundingBox().collisionDetection(
					player.getPosition(), currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		return true;
	}

}
