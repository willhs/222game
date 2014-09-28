package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.io.Serializable;
import java.util.*;

public abstract class ServerWorld implements Serializable {

	public List<String> applyCommand(String command) {
		List<String> commands = new ArrayList<String>();
		Scanner scan = new Scanner(command);
		if (scan.hasNext("ServerPlayerPlacement")) {
			scan.next();
			System.out.println(command);
			Player player = parsePlayer(scan);
			if (addPlayerToGameWorld(player)) {
				Place place = getPlaceOfPlayer(player);
				System.out.println("Made it here.");
				String newCommand = "ClientPlayerPlacement  Position ( "
						+ player.getPosition().toString() + " )";
				commands.add(newCommand);
				System.out.println(commands.size());
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

	protected abstract Place getStartPlace();

	protected abstract void addPlayer(Player player);

	protected Player parsePlayer(Scanner scan) {
		String name = "Unknowen";
		if (scan.hasNext("Name")) {
			System.out.println("Scanning name");
			name = parseName(scan);
		}
		Inventory inventory = new Inventory();
		if (scan.hasNext("Inventory")) {
			System.out.println("Scanning inventory");
			inventory = parseInventory(scan);
		}
		Point3D point = new Point3D(0, 0, 0);
		if (scan.hasNext("Position")) {
			System.out.println("Scanning poiunt");
			point = parsePosition(scan);
		}
		Rectangle3D boundingBox = new Rectangle3D(0, 0, 0);
		if (scan.hasNext("BoundingBox")) {
			System.out.println("scanning bounding box");
			boundingBox = parseBoundingBox(scan);
		}
		return new Player(name, inventory, point, boundingBox);
	}

	protected String parseName(Scanner scan) {
		String name = "";
		scan.next();
		scan.next();
		name = scan.next();
		scan.next();
		return name;
	}

	protected Rectangle3D parseBoundingBox(Scanner scan) {
		while(!scan.hasNextDouble() && scan.hasNext()){
			System.out.println("removing strings");
			scan.next();
		}
		float width = (float)scan.nextDouble();
		System.out.println(width);
		while(!scan.hasNextDouble() && scan.hasNext()){
			scan.next();
		}
		float length = (float)scan.nextDouble();
		while(!scan.hasNextDouble() && scan.hasNext()){
			scan.next();
		}
		float height = (float)scan.nextDouble();
		return new Rectangle3D(width, height, length);
	}

	protected Point3D parsePosition(Scanner scan) {
		while (!scan.hasNextFloat()){
			scan.next();
		}
		float x = scan.nextFloat();
		while (!scan.hasNextFloat()){
			scan.next();
		}
		float y = scan.nextFloat();
		while (!scan.hasNextFloat()){
			scan.next();
		}
		float z = scan.nextFloat();
		return new Point3D(x, y, z);
	}

	protected Inventory parseInventory(Scanner scan) {
		return null;
	}
}
