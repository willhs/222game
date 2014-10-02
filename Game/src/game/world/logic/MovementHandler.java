package game.world.logic;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Exit;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Player;

import java.util.Iterator;

/**
 * Handles all player movement at the moment.
 *
 * @author Shane Brewer
 *
 */
public class MovementHandler {

	/**
	 * This will move the player from its old position to the new one This
	 * method will handle all the rules for movement.
	 *
	 * @param player
	 *            - player to be moved.
	 * @param to
	 *            - position the player wants to move to.
	 * @param place
	 *            - the place that the player is moving in.
	 * @param toIgnore
	 *            - Items to ignore collisions from.
	 * @return returns true if a change has been made to the players position.
	 */
	public static boolean playerMove(Player player, Point3D to, Place place,
			Item... toIgnore) {
//		if (!place.contains(to)) {
//			return false;
//		}
		if (!place.contains(to, player.getBoundingBox())) {
			return false;
		}
		if (checkItemCollision(player, to, place.getItems(), toIgnore)) {
			return false;
		}
		if (checkPlayerCollision(player, to, place.getPlayers())) {
			return false;
		}
		if (checkExitCollision(player, to, place,  place.getExits())){

		}
		player.move(to);
		return true;
	}

	private static boolean checkExitCollision(Player player, Point3D playerPoint, Place place,
			Iterator<Exit> exits) {
		Rectangle3D playerBox = player.getBoundingBox();
		while (exits.hasNext()) {
			Exit exit = exits.next();
			if (playerBox.collisionDetection(playerPoint,
					exit.getBoundingBox(), exit.getPosition(place))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player is going to collied with any items.
	 *
	 * @param player
	 *            - player that wishes to move.
	 * @param items
	 *            - items that the player my be in.
	 * @param toIgnore
	 *            - items to ignore.
	 * @return - returns true only if the player collieds with a item.
	 */
	private static boolean checkItemCollision(Player player, Point3D playerPoint,
			Iterator<Item> items, Item... toIgnore) {
		Rectangle3D playerBox = player.getBoundingBox();
		while (items.hasNext()) {
			Item item = items.next();
			if (playerBox.collisionDetection(playerPoint,
					item.getBoundingBox(), item.getPosition())
					&& !checkItemsEqual(item, toIgnore)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if to items are equal to the check item.
	 *
	 * @param itemToCheck
	 *            - item to check.
	 * @param toChackAgainst
	 *            - items that are to be chacked against.
	 * @return - returns true if one of the items in the check list is equal to
	 *         the check item.
	 */
	private static boolean checkItemsEqual(Item itemToCheck,
			Item... toChackAgainst) {
		for (Item item : toChackAgainst) {
			if (item.equals(itemToCheck)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player is going to collied with another player.
	 *
	 * @param player
	 *            - player that is moving.
	 * @param players
	 *            - players in the place.
	 * @return - true if the player collides
	 */
	private static boolean checkPlayerCollision(Player player, Point3D playerPoint,
			Iterator<Player> players) {
		Rectangle3D playerBox = player.getBoundingBox();
		while (players.hasNext()) {
			Player otherPlayer = players.next();
			Rectangle3D otherPlayerBox = otherPlayer.getBoundingBox();
			Point3D otherPlayerPoint = otherPlayer.getPosition();
			if (!player.equals(otherPlayer)
					&& (playerBox.collisionDetection(playerPoint,
							otherPlayerBox, otherPlayerPoint) || playerPoint
							.equals(otherPlayerPoint))) {
				return true;
			}
		}
		return false;
	}

	// ==========================Exiting Room Logic=========================//
	/**
	 * Helps a player exit from one room to another.
	 *
	 * @param player
	 *            - player to be moved
	 * @param place
	 *            - place the player is moving from.
	 * @param exit
	 *            - the exit the player is going to use.
	 * @return - returns true if the player exited correctly.
	 */
	public static boolean exitPlace(Player player, Place place, Exit exit) {
		if (!checkProximity(player.getPosition(), player.getBoundingBox(),
				exit.getPosition(), exit.getBoundingBox())) {
			return false;
		}
		if (exit.isLocked()) {
			if (!exit.unlock(player.getInventory())) {
				System.out.println();
				return false;
			}
		}
		Place otherPlace = exit.getOtherPlace(place);
		place.removePlayer(player);
		otherPlace.addPlayer(player);
		player.move(findExitPosition(player, exit, otherPlace));
		return true;
	}

	/**
	 * Makes sure that the item and player are within a certain proximity of
	 * each other
	 *
	 * @param pointOne
	 *            - point of the player.
	 * @param pointTwo
	 *            - point of the item.
	 * @return - true if they are close enough
	 */
	public static boolean checkProximity(Point3D pointOne,
			Rectangle3D boundingOne, Point3D pointTwo, Rectangle3D boundingTwo) {
		if ((Math.abs(pointOne.x - pointTwo.x) <= 5 + (boundingOne.getWidth() + boundingTwo
				.getWidth()) / 2)
				&& (Math.abs(pointOne.z - pointTwo.z) <= 5 + (boundingOne
						.getLength() + boundingTwo.getLength()) / 2)) {
			System.out.println("Make it here");
			return true;
		}
		return false;
	}

	private static Point3D findExitPosition(Player player, Exit exit, Place place){

		return null;
	}
	// ===============================END===================================//

}
