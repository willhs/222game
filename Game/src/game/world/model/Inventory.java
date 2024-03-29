package game.world.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

/**
 * Inventory of a player 
 * All player should have a inventory
 * @author Shane Brewer 300289850
 *
 */
public class Inventory implements Iterable<Item>, Serializable{

	private final List<Item> items;
	
	//===================Constructors=====================//
	public Inventory(){
		items = new ArrayList<Item>();
	}
	
	public Inventory(List<Item> items){
		this.items = items;
	}
	//=======================end==========================//
	
	/**
	 * Used to add a items to the players inventory.
	 * @param item - item that the player wishes to add.
	 * @return - true if the item was added.
	 */
	public boolean addItem(Item item){
		return items.add(item);
	}
	
	/**
	 * Used to remove a item form a player inventory
	 * @param item - item to remove.
	 * @return - true only if the item was removed.
	 */
	public boolean removeItem(Item item){
		return items.remove(item);
	}
	
	@Override
	public Iterator<Item> iterator() {
		return items.iterator();
	}
	
	/**
	 * returns true if the item isin the inventory.
	 */
	public boolean isIn(Item item){
		return items.contains(item);
	}
	
	/**
	 * return true if the inventory is empty.
	 */
	public boolean isEmpty(){
		return items.isEmpty();
	}
	
}
