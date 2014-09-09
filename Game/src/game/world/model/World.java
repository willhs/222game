package game.world.model;

import java.util.*;

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
}
