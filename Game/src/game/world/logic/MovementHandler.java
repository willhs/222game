package game.world.logic;

import game.world.dimensions.*;
import game.world.model.*;

import java.util.Iterator;

/**
 * Handles all player movement at the moment.
 *
 * @author Shane Brewer 300289850
 *
 */
public class MovementHandler {

	private static final float PLAYER_EXIT_DISTANCE = 10;
	private static final float EXITING_DISTANCE = 20;

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
		if (player == null || to == null || place == null){
			return false;
		}
		// Makes sure the player is in the place.
		if (!place.contains(to)) {
			return false;
		}
		// makes sure the players bounding box is in the place.
		if (!place.contains(to, player.getBoundingBox())) {
			return false;
		}
		// checks if there are any item collitions.
		if (checkItemCollision(player, to, place.getItems(), toIgnore)) {
			return false;
		}
		// checks if there are player collitions
		if (checkPlayerCollision(player, to, place.getPlayers())) {
			return false;
		}
		// checks if there are enviromental oliitions.
		if (checkEnviromentalCollision(player, to, place.getEnviroment())){
			return false;
		}
		// checks for eixt coliitions
		if (checkExitCollision(player, to, place,  place.getExits())){
			return false;
		}
		player.move(to);
		return true;
	}

	/**
	 * Check the enviroment for colitions.
	 * @param player - player to be checked.
	 * @param playerPoint - point that it is at.
	 * @param enviroments - the enviroment that there is to collide with.
	 * @return - true if there is a collition.
	 */
	private static boolean checkEnviromentalCollision(Player player,
			Point3D playerPoint, Iterator<Enviroment> enviroments) {
		Rectangle3D playerBox = player.getBoundingBox();
		while (enviroments.hasNext()) {
			Enviroment enviroment = enviroments.next();
			if (enviroment.isCollidable() && playerBox.collisionDetection(playerPoint,
					enviroment.getBoundingBox(), enviroment.getPosition())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player collids with any exits.
	 * @param player - the player that is moving.
	 * @param playerPoint - the point that the player is at.
	 * @param place - the place that they player is in.
	 * @param exits - the exits that are being checked.
	 * @return return if colition detected.
	 */
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
		// None of the parameters should be null.
		if (player == null || exit == null || place == null){
			return false;
		}
		// The player needs to be in proximity of the exit
		if (!checkProximity(player.getPosition(), player.getBoundingBox(),
				exit.getPosition(place), exit.getBoundingBox())) {
			return false;
		}
		// Checks if the exit is locked.
		if (exit.isLocked()) {
			// will try to unlock the exit
			if (!exit.unlock(player.getInventory())) {
				return false;
			}
		}
		// gets the place the player is moving to.
		Place otherPlace = exit.getOtherPlace(place);
		// will set a new psoition for the player and remove and add him/her.
		if (setPlayerExitPosition(player, exit, otherPlace)){
			place.removePlayer(player);
			otherPlace.addPlayer(player);
			return true;
		}
		// will fail if no position in other place is found.
		return false;
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
		if ((Math.abs(pointOne.x - pointTwo.x) <= PLAYER_EXIT_DISTANCE + (boundingOne.getWidth() + boundingTwo
				.getWidth()) / 2)
				&& (Math.abs(pointOne.z - pointTwo.z) <= PLAYER_EXIT_DISTANCE + (boundingOne
						.getLength() + boundingTwo.getLength()) / 2)) {
			return true;
		}
		return false;
	}

	/**
	 * Sets the players position appon exiting the exit
	 * @param player - the player neededing a new poition.
	 * @param exit - the eixt they are exiting from.
	 * @param place - the place they are in.
	 * @return - true if they have been placed.
	 */
	private static boolean setPlayerExitPosition(Player player, Exit exit, Place place){
		Rectangle3D rect = exit.getBoundingBox().apply3Dpoint(exit.getPosition(place));
		Rectangle3D playerRect = player.getBoundingBox();
		float x = (float)(rect.x-EXITING_DISTANCE);
		float maxX = (int)(rect.x+rect.width+EXITING_DISTANCE);
		float maxZ = (int)(rect.z+rect.length+EXITING_DISTANCE);
		for (;x < maxX; x+=0.1f){
			float z = (int)(rect.z-EXITING_DISTANCE);
			for (;z < maxZ; z+=0.1f){
				if (MovementHandler.playerMove(player, new Point3D(x, 0, z), place)){
					return true;
				}
			}
		}
		return false;
	}
	// ===============================END===================================//

}
