package game.world.logic;

import game.world.model.*;

/**
 * 
 * @author Shane Brewer 300289850
 *
 */
public class ContainerInteractionHandler {

	public static boolean getItemsFromContainer(Player player,
			Container container, Place place) {
		if (player == null || container == null || place == null) {
			return false;
		}
		if (!ItemInteractionHandler.checkProximity(player.getPosition(),
				player.getBoundingBox(), container.getPosition(),
				container.getBoundingBox())) {
			return false;
		}
		if (!ItemInteractionHandler.checkPlayers(place, player)
				&& !ItemInteractionHandler.checkItems(place, container)) {
			return false;
		}
		if (container.isLocaked()) {
			if (!container.unlock(player.getInventory())) {
				return false;
			}
		}
		for (Item item: container.getContents()){
			player.addItem(item);
		}
		for (Item item: player.getInventory()){
			container.getContents().removeItem(item);
		}
		return true;
	}

}
