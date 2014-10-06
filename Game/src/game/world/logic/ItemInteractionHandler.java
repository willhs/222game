package game.world.logic;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;
import game.world.model.Enviroment;
import game.world.model.Exit;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Player;

import java.util.Iterator;

/**
 * 
 * @author Shane Brewer
 * 
 */
public class ItemInteractionHandler {
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
		if (!checkPlayers(place, player) && !checkItems(place, item)) {
			return false;
		}
		if (!checkProximity(player.getPosition(), player.getBoundingBox(),
				item.getPosition(), item.getBoundingBox())) {
			return false;
		}
		if (!item.canPickUp()) {
			return false;
		}
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
		if ((Math.abs(pointOne.x - pointTwo.x) <= 5 + (boundingOne.getWidth() + boundingTwo
				.getWidth()) / 2)
				&& (Math.abs(pointOne.z - pointTwo.z) <= 5 + (boundingOne
						.getLength() + boundingTwo.getLength()) / 2)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks that the player is in the room
	 * 
	 * @param place
	 *            - place player should be in.
	 * @param player
	 *            - player to check.
	 * @return - true only if player is in the room.
	 */
	private static boolean checkPlayers(Place place, Player player) {
		Iterator<Player> players = place.getPlayers();
		while (players.hasNext()) {
			if (players.next().equals(player)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks that the item is in the room.
	 * 
	 * @param place
	 *            -place that the item should be.
	 * @param item
	 *            - item that should be in the room.
	 * @return - true only if the item is in the room.
	 */
	private static boolean checkItems(Place place, Item item) {
		Iterator<Item> items = place.getItems();
		while (items.hasNext()) {
			if (items.next().equals(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 * @param item
	 * @param place
	 * @return
	 */
	public static boolean dropItem(Player player, Item item, Place place) {
		if (!checkPlayers(place, player)) {
			return false;
		}
		if (!item.canDrop()) {
			return false;
		}
		if (!player.getInventory().removeItem(item)) {
			return false;
		}
		if (!setItemDropPoint(player, item, place)){
			return false;
		}
		place.addItem(item);
		return true;
	}

	/**
	 * Used to to get the drop point for an item using the players direction.
	 * 
	 * @param player
	 *            - player dropping things.
	 * @param item
	 *            - item the player is droping.
	 * @return
	 */
	private static boolean setItemDropPoint(Player player, Item item,
			Place place) {
		Rectangle3D rect = player.getBoundingBox().apply3Dpoint(
				player.getPosition(place));
		int x = (int) rect.x - 20;
		int z = (int) rect.z - 20;
		int maxX = (int) (rect.x + rect.width + 20);
		int maxZ = (int) (rect.z + rect.length + 20);
		for (; x < maxX; x++) {
			for (; z < maxZ; z++) {
				Point3D position = new Point3D(x, 0, z);
				if (canItemBeHere(item, position, place)) {
					item.setPosition(position);
					return true;
				}
			}
		}
		return false;
	}

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
			Exit exit= exits.next();
			if (exit.getBoundingBox().collisionDetection(exit.getPosition(),
					currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		Iterator<Enviroment> enviroment = place.getEnviroment();
		while (enviroment.hasNext()) {
			Enviroment envir = enviroment.next();
			if (envir.getBoundingBox().collisionDetection(envir.getPosition(),
					currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		Iterator<Player> players = place.getPlayers();
		while (players.hasNext()) {
			Player player = players.next();
			if (player.getBoundingBox().collisionDetection(player.getPosition(),
					currnetItem.getBoundingBox(), point)) {
				return false;
			}
		}
		return true;
	}

}
