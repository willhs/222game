package game.world.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * World
 * This describes what it is to be a world
 * Worlds have places in then that players can go to
 * they have a list of player that is all the players
 * the world.
 * @author Shane Brewer
 *
 */
public class World extends ClientWorld{

	private Place startPlace;
	// both of these once set should not change.
	private final List<Player> players;
	private final List<Place> places;

	private final Map<String, Exit> allExits;
	private final Map<String, Player> allPlayers;
	private final Map<String, Place> allPlaces;
	private final Map<String, Item> allItems;

	//==================Constructor===================//
	/**
	 * Places list must not be empty.
	 * @param places
	 */
	public World(List<Place> places){
		super();
		this.players = new ArrayList<Player>();
		this.places = places;
		startPlace = places.get(0);
		allExits = new HashMap<String, Exit>();
		allPlayers = new HashMap<String, Player>();
		allPlaces = new HashMap<String, Place>();
		allItems = new HashMap<String, Item>();
		for (Place place: places){
			allPlaces.put(place.getName(), place);
			Iterator<Item> it = place.getItems();
			while (it.hasNext()){
				Item i = it.next();
				allItems.put(i.getName(), i);
			}
		}

	}

	//======================End========================//

	@Override
	public Iterator<Place> getPlaces(){
		return places.iterator();
	}

	@Override
	public Iterator<Player> getPlayers(){
		return players.iterator();
	}

	@Override
	public Place getPlaceOfPlayer(Player player){
		for(Place place: places){
			Iterator<Player> iPlayers = place.getPlayers();
			while(iPlayers.hasNext()){
				if (iPlayers.next().getName().equals(player.getName())){
					return place;
				}
			}
		}
		return null;
	}

	@Override
	public Iterator<Player> getPlayerOfPlace(Place place){
		return place.getPlayers();
	}

	@Override
	public boolean addPlayerToGameWorld(Player player){
		players.add(player);
		allPlayers.put(player.getName(), player);
		return startPlace.setStartPoint(player);
	}

	@Override
	protected Place getStartPlace() {
		return startPlace;
	}

	@Override
	protected void addPlayer(Player player) {
		allPlayers.put(player.getName(), player);
		players.add(player);
	}

	@Override
	protected Player getPlayerByName(String playerName) {
		return allPlayers.get(playerName);
	}

	@Override
	protected Place getPlaceByName(String placeName) {
		return allPlaces.get(placeName);
	}
}
