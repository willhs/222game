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
public class World {
	
	// both of these once set should not change.
	private final List<Player> players;
	private final List<Place> places;
	
	//==================Constructors===================//
	public World (){
		players = new ArrayList<Player>();
		places = new ArrayList<Place>();
	}
	
	public World(List<Player> players){
		this.players = players;
		places = new ArrayList<Place>();
	}
	
	public World(List<Player> players, List<Place> places){
		this.players = players;
		this.places = places;
	}
	
	//======================End========================//
	
	/**
	 * getPlaces method returns all the places in the form
	 * of a iterator
	 * @return - iterator over the list of places.
	 */
	public Iterator<Place> getPlaces(){
		return places.iterator();
	}
	
	/**
	 * getPlayers method returns all the players that are 
	 * in the list in the form of a iterator
	 * @return
	 */
	public Iterator<Player> getPlayers(){
		return players.iterator();
	}
	
	/**
	 * This method will return the place that the player is currently 
	 * in.
	 * @param player - the player that will be used to find the room.
	 * @return -returns the place the player is currently.
	 */
	public Place getPlaceOfPlayer(Player player){
		return null;
	}
	
	/**
	 * This method will return a iterator of players in a place.
	 * not really needed if you have the place but added.
	 * @param place - place that the players are in.
	 * @return - returns the list of players in that place.
	 */
	public Iterator<Player> getPlayerOfPlace(Place place){
		return place.getPlayers();
	}
}
