package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.util.*;

public abstract class ServerWorld {

	public List<String> applyCommand(String command) {
		List<String> commands = new ArrayList<String>();
		Scanner scan = new Scanner(command);
		if (scan.hasNext("ServerPlayerPlacement")) {
			scan.next();
			Player player = parsePlayer(scan);
			if (addPlayerToGameWorld(player)) {
				Place place = getPlaceOfPlayer(player);
				String newCommand = "ClientPlayerPlacement Player ( Name ( "
						+ player.name + " ) Position ( " + player.getPosition()
						+ " ) ) Place ( Name ( " + place.getName() + " ) )";
				commands.add(newCommand);
				return commands;
			}
		}
		// applies command to the world
		// returns a list of commands that resulted from running the given
		// command
		// returns an empty string array if the command was invalid or whatever
		return null;
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

	protected Player parsePlayer(Scanner scan) {
		String name = "Unknowen";
		if (scan.hasNext("Name")) {
			name = parseName(scan);
		}
		Inventory inventory = new Inventory();
		if (scan.hasNext("Inventory")) {
			inventory = parseInventory(scan);
		}
		Point3D point = new Point3D(0, 0, 0);
		if (scan.hasNext("Position")) {
			point = parsePosition(scan);
		}
		Rectangle3D boundingBox = new Rectangle3D(0, 0, 0);
		if (scan.hasNext("BoundingBox")) {
			boundingBox = parseBoundingBox(scan);
		}
		return new Player(name, inventory, point, boundingBox);
	}

	protected String parseName(Scanner scan) {
		String name = "";
		scan.next();
		scan.next("(");
		name = scan.next();
		scan.next(")");
		return name;
	}

	protected Rectangle3D parseBoundingBox(Scanner scan) {
		scan.next("BoundingBox");
		scan.next("(");
		scan.next("width=");
		float width = scan.nextFloat();
		scan.next(",");
		scan.next("length=");
		float length = scan.nextFloat();
		scan.next(",");
		scan.next("height=");
		float height = scan.nextFloat();
		return new Rectangle3D(width, height, length);
	}

	protected Point3D parsePosition(Scanner scan) {
		return null;
	}

	protected Inventory parseInventory(Scanner scan) {
		return null;
	}
}
