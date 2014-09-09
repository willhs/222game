package game.world.model;

import java.util.*;

public class Room implements Place{

	private final List<Exit> exits;
	private final List<Player> players;
	private final List<Item> items;
	
	public Room (List<Exit> exits, List<Item> items){
		this.exits = exits;
		this.players = new ArrayList<Player>();
		this.items = items;
	}
	
	@Override
	public Iterator<Exit> getExits() {
		return exits.iterator();
	}

	@Override
	public List<Item> getItems() {
		return items;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}
	
}
