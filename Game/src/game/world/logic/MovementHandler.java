package game.world.logic;

import java.util.Iterator;

import game.world.dimensions.*;
import game.world.model.*;

/**
 * Handles all player movement at the moment.
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
		if (!place.contains(to)) {
			return false;
		}
		if (!place.contains(to, player.getBoundingBox())){
			return false;
		}
		if (checkItemCollision(player, place.getItems(), toIgnore)) {
			return false;
		}
		if (checkPlayerCollision(player, place.getPlayers())){
			return false;
		}
		player.move(to);
		return true;
	}

	/**
	 * Checks if the player is going to collied with any items.
	 * @param player - player that wishes to move.
	 * @param items - items that the player my be in.
	 * @param toIgnore - items to ignore.
	 * @return - returns true only if the player collieds with a item.
	 */
	private static boolean checkItemCollision(Player player,
			Iterator<Item> items, Item... toIgnore) {
		Rectangle3D playerBox = player.getBoundingBox();
		Point3D playerPoint = player.getPosition();
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
	 *Checks if to items are equal to the check item.
	 * @param itemToCheck - item to check.
	 * @param toChackAgainst -  items that are to be chacked against.
	 * @return - returns true if one of the items in the check list is equal to the check item.
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
	 * @param player - player that is moving.
	 * @param players - players in the place.
	 * @return - true if the player collides
	 */
	private static boolean checkPlayerCollision(Player player, Iterator<Player> players){
		Rectangle3D playerBox = player.getBoundingBox();
		Point3D playerPoint = player.getPosition();
		while(players.hasNext()){
			Player otherPlayer = players.next();
			Rectangle3D otherPlayerBox = otherPlayer.getBoundingBox();
			Point3D otherPlayerPoint = otherPlayer.getPosition();
			if (!player.equals(otherPlayer) && playerBox.collisionDetection(playerPoint, otherPlayerBox, otherPlayerPoint)){
				return true;
			}
		}
		return false;
	}

	//==========================Exiting Room Logic=========================//
	/**
	 * Helps a player exit from one room to another.
	 * @param player - player to be moved
	 * @param place - place the player is moving from.
	 * @param exit - the exit the player is going to use.
	 * @return - returns true if the player exited correctly.
	 */
	public static boolean exitPlace(Player player, Place place, Exit exit){
		if(!checkProximity(player.getPosition(), exit.getPosition())){
			return false;
		}
		if(exit.isLocked()){
			if(!exit.unlock(player.getInventory())){
				return false;
			}
		}
		Place otherPlace = exit.getOtherPlace(place);
		place.removePlayer(player);
		otherPlace.addPlayer(player);
		return true;
	}

	/**
	 * Makes sure that the item and player are within a certain proximity of each other
	 * @param pointOne - point of the player.
	 * @param pointTwo - point of the item.
	 * @return - true if they are close enough
	 */
	private static boolean checkProximity(Point3D pointOne, Point3D pointTwo){
		if ((Math.abs(pointOne.x -pointTwo.x) <= 5) && (Math.abs(pointOne.z -pointTwo.z) <= 5)){
			return true;
		}
		return false;
	}
	//===============================END===================================//


}
