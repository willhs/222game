package game.world.logic;

import java.util.Iterator;

import game.world.dimensions.*;
import game.world.model.*;

/**
 * 
 * @author Shane Brewer
 *
 */
public class PickupHandler {
	/**
	 * Used by the Game Event handler to have a Player pick up an item.
	 * @param player - player that is to pick up the item
	 * @param item - item to be picked up.
	 * @param place - place the player and the item should be.
	 * @return - true if and only if the the player now has the item.
	 */
	public static boolean PickupItem(Player player, Item item, Place place){
		if(!checkPlayers(place, player) && !checkItems(place, item)){
			return false;
		}
		if (!checkProximity(player.getPosition(), item.getPosition())){
			return false;
		}
		if(!item.canPickUp()){
			return false;
		}
		place.removeItem(item);
		item.setPosition(new Point3D(0,0,0));
		player.addItem(item);
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

	/**
	 * Checks that the player is in the room
	 * @param place - place player should be in.
	 * @param player - player to check.
	 * @return - true only if player is in the room.
	 */
	private static boolean checkPlayers(Place place, Player player){
		Iterator<Player> players = place.getPlayers();
		while(players.hasNext()){
			if(players.next().equals(player)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks that the item is in the room.
	 * @param place -place that the item should be.
	 * @param item - item that should be in the room.
	 * @return - true only if the item is in the room.
	 */
	private static boolean checkItems(Place place, Item item){
		Iterator<Item> items = place.getItems();
		while(items.hasNext()){
			if(items.next().equals(item)){
				return true;
			}
		}
		return false;
	}

}
