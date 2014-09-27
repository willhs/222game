package game.world.model;

import java.util.*;

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

	//==================Constructors===================//
	public World (){
		players = new ArrayList<Player>();
		places = new ArrayList<Place>();
	}

	/**
	 * Places list must not be empty.
	 * @param places
	 */
	public World(List<Place> places){
		this.players = new ArrayList<Player>();
		this.places = places;
		startPlace = places.get(0);
	}

	/**
	 * Makes the game world.
	 * @param players - players list must not be empty.
	 * @param places - places must not be empty.
	 */
	public World(List<Player> players, List<Place> places){
		this.players = players;
		this.places = places;
		startPlace = places.get(0);
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
		return null;
	}

	@Override
	public Iterator<Player> getPlayerOfPlace(Place place){
		return place.getPlayers();
	}

	@Override
	public boolean addPlayerToGameWorld(Player player){
		players.add(player);
		return startPlace.setStartPoint(player);
	}
}
