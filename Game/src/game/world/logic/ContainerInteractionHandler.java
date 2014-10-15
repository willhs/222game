package game.world.logic;

import game.world.model.*;

/**
 * 
 * @author Shane Brewer 300289850
 *
 */
public class ContainerInteractionHandler {

	/**
	 * Checks the logic and removes items and adds to the player if 
	 * it is ok to do so.
	 */
	public static boolean getItemsFromContainer(Player player,
			Container container, Place place) {
		// None of the parameters should be null
		if (player == null || container == null || place == null) {
			return false;
		}
		// The container should be within range
		if (!ItemInteractionHandler.checkProximity(player.getPosition(),
				player.getBoundingBox(), container.getPosition(),
				container.getBoundingBox())) {
			return false;
		}
		// the place and the container must be in the place.
		if (!place.containsPlayer(player)
				&& !place.containsItem(container)) {
			return false;
		}
		// the container could be locked.
		if (container.isLocaked()) {
			// the player may unlock it.
			if (!container.unlock(player.getInventory())) {
				return false;
			}
		}
		// adds all items to the player
		for (Item item: container.getContents()){
			player.addItem(item);
		}
		// removes them from the inventory
		for (Item item: player.getInventory()){
			container.getContents().removeItem(item);
		}
		return true;
	}

}
