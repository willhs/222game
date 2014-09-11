package game.world.model;

import java.util.List;

/**
 * Container
 * defines what it is to be a container in this game.
 * @author Shane Brewer
 *
 */
public interface Container {
	
	/**
	 * Gets the list of items that are in the container.
	 * @return - returns a list of items from the container.
	 */
	public List<Item> getItems();
}
