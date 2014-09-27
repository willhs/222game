package game.world.model;

import java.util.*;

public abstract class ServerWorld {

	public List<String> applyCommand(String command){
		//applies command to the world
		//returns a list of commands that resulted from running the given command
		//returns an empty string array if the command was invalid or whatever
		return null;
	}

	/**
	 * getPlaces method returns all the places in the form
	 * of a iterator
	 * @return - iterator over the list of places.
	 */
	public abstract Iterator<Place> getPlaces();

	/**
	 * getPlayers method returns all the players that are
	 * in the list in the form of a iterator
	 * @return
	 */
	public abstract Iterator<Player> getPlayers();

	/**
	 * This method will return the place that the player is currently
	 * in.
	 * @param player - the player that will be used to find the room.
	 * @return -returns the place the player is currently.
	 */
	public abstract Place getPlaceOfPlayer(Player player);

	/**
	 * This method will return a iterator of players in a place.
	 * not really needed if you have the place but added.
	 * @param place - place that the players are in.
	 * @return - returns the list of players in that place.
	 */
	public abstract Iterator<Player> getPlayerOfPlace(Place place);

	public abstract void addPlayerToGameWorld(Player player);
}
