package game.world.model;

import game.world.util.Parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public abstract class ServerWorld implements Serializable {

	public List<String> applyCommand(String command) {
		List<String> commands = new ArrayList<String>();
		Scanner scan = new Scanner(command);
		if (scan.hasNext("Server")) {
			scan.next();
			if (scan.hasNext("PlayerPlacement")) {
				commands = serverPlayerPlacement(scan, command);
			}
			if (scan.hasNext("Move")) {
				commands = handleMove(scan, command);
			}
		}
		// applies command to the world
		// returns a list of commands that resulted from running the given
		// command
		// returns an empty string array if the command was invalid or whatever
		return commands;
	}

	/**
	 * getPlaces method returns all the places in the form of a iterator
	 *
	 * @return - iterator over the list of places.
	 */
	public abstract Iterator<Place> getPlaces();

	/**
	 * getPlayers method returns all the players that are in the list in the
	 * form of a iterator
	 *
	 * @return
	 */
	public abstract Iterator<Player> getPlayers();

	/**
	 * This method will return the place that the player is currently in.
	 *
	 * @param player
	 *            - the player that will be used to find the room.
	 * @return -returns the place the player is currently.
	 */
	public abstract Place getPlaceOfPlayer(Player player);

	/**
	 * This method will return a iterator of players in a place. not really
	 * needed if you have the place but added.
	 *
	 * @param place
	 *            - place that the players are in.
	 * @return - returns the list of players in that place.
	 */
	public abstract Iterator<Player> getPlayerOfPlace(Place place);

	/**
	 * Finds a place in the game world for the player.
	 *
	 * @param player
	 *            - player that is to be added to the fame world.
	 * @return TODO
	 */
	public abstract boolean addPlayerToGameWorld(Player player);

	protected abstract Place getStartPlace();

	protected abstract void addPlayer(Player player);

	private List<String> serverPlayerPlacement(Scanner scan, String command) {
		List<String> commands = new ArrayList<String>();
		scan.next();
		Player player = Parser.parsePlayer(scan);
		if (addPlayerToGameWorld(player)) {
			Place place = getPlaceOfPlayer(player);
			String newCommand = "Client PlayerPlacement  Position ( "
					+ player.getPosition().toString() + " )";
			commands.add(newCommand);
		}
		return commands;
	}

	private List<String> handleMove(Scanner scan, String command) {

		return null;
	}
}
