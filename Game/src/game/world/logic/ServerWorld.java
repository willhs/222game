package game.world.logic;

import game.world.dimensions.Point3D;
import game.world.model.*;
import game.world.util.Parser;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author Shane Brewer
 * 
 */
public abstract class ServerWorld implements Serializable {

	/**
	 * Applys commands that are sent by the client.
	 * 
	 * @param command
	 *            - clients command.
	 * @return - returns a list of commands.
	 */
	public List<String> applyCommand(String command) {
		List<String> commands = new ArrayList<String>();
		Scanner scan = new Scanner(command);
		if (scan.hasNext("Server")) {
			scan.next();
			if (scan.hasNext("PlayerPlacement")) {
				commands = serverPlayerPlacement(scan, command);
			} else if (scan.hasNext("Move")) {
				commands = serverHandleMove(scan, command);
			} else if (scan.hasNext("Exit")) {
				commands = serverExitInteraction(scan, command);
			} else if (scan.hasNext("ItemPickUp")){
				commands = serverItemPickUp(scan, command);
			} else if (scan.hasNext("ItemDrop")){
				commands = serverItemDrop(scan, command);
			} else if (scan.hasNext("Container")){
				commands = serverContainerInteraction(scan, command);
			}
		}
		return commands;
	}
	
	/**
	 * Handles the player placement at the start of the game.
	 * 
	 * @param scan
	 *            - scanner that has the command in it.
	 * @param command
	 *            - the command that is used later.
	 * @return - return the client vertion of the command for all clients to
	 *         user.
	 */
	private List<String> serverPlayerPlacement(Scanner scan, String command) {
		List<String> commands = new ArrayList<String>();
		scan.next();
		Player player = Parser.parsePlayer(scan);
		Parser.removeUnneedText("Image", scan);
		String imageName = Parser.parseName(scan);
		player.setImageName(imageName);
		if (addPlayerToGameWorld(player)) {
			String newCommand = "Client PlayerPlacement Name ( "
					+ player.getName() + " ) Image ( " +imageName+" ) Position ( "
					+ player.getPosition().toString() + " )";
			commands.add(newCommand);
		}
		return commands;
	}

	/**
	 * server handling of the handle move checks if the player can move and
	 * moves them if they can.
	 * 
	 * @param scan
	 *            - scanner with the command in it.
	 * @param command
	 *            - the command for later use.
	 * @return - a list of commands could have the sucsessful command in it.
	 */
	private List<String> serverHandleMove(Scanner scan, String command) {
		List<String> commands = new ArrayList<String>();
		// Gets the players name
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the point the player is to move to.
		Parser.removeUnneedText("Point", scan);
		Point3D playerPosition = Parser.parsePosition(scan);
		// get the place which the player is to move in.
		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		// Checks the logic to sort all of the players movement.
		if (MovementHandler.playerMove(getPlayerByName(playerName),
				playerPosition, getPlaceByName(placeName))) {
			// Makes the command for the client.
			Scanner sc = new Scanner(command);
			sc.next();
			String newCommand = "Client " + sc.nextLine();
			commands.add(newCommand);
			sc.close();
		}
		
		// Returns the new command.
		return commands;
	}

	/**
	 * Handles the server checking for the exit interaction
	 * 
	 * @param scan
	 *            - scanner with the command in it.
	 * @param command
	 *            - the command needed for concatatantion to make a new command.
	 * @return - a list of commands that are to be returns will have one of none
	 *         in it.
	 */
	private List<String> serverExitInteraction(Scanner scan, String command) {
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String exitName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		if (MovementHandler.exitPlace(getPlayerByName(playerName),
				getPlaceByName(placeName), getExitByName(exitName))) {
			Scanner sc = new Scanner(command);
			sc.next();
			Player player = getPlayerByName(playerName);
			commands.add("Client " + sc.nextLine() + " Position ( "
					+ player.getPosition() + " )");
			sc.close();
		}
		return commands;
	}

	/**
	 * Checks for the the logic for item pickup.
	 * @param scan - the scanner with the command it in.
	 * @param command - the command that is to be parsed.
	 * @return - returns a string that is the command for the client.
	 */
	private List<String> serverItemPickUp(Scanner scan, String command) {
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);
		Place place = getPlaceByName(placeName);
		
		if (ItemInteractionHandler.pickupItem(player, item, place)){
			Scanner sc = new Scanner(command);
			sc.next();
			commands.add("Client "+ sc.nextLine());
			sc.close();
		}
		return commands;
	}
	
	/**
	 * Handles the servers item dropping logic makes a call to a handler.
	 * @param scan - the scanner with command in it.
	 * @param command -  the command.
	 * @return - a string with a command in it or and empty string.
	 */
	private List<String> serverItemDrop(Scanner scan, String command){
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		
		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);
		Place place = getPlaceByName(placeName);
		
		if (ItemInteractionHandler.dropItem(player, item, place)){
			Scanner sc = new Scanner(command);
			sc.next();
			commands.add("Client "+ sc.nextLine() + " Point " + item.getPosition());
			sc.close();
		}
		return commands;
	}
	
	/**
	 * Logic for containers
	 * @param scan - scanner with the container command in it.
	 * @param command - the command it self.
	 * @return - returns a new command to.
	 */
	private List<String> serverContainerInteraction(Scanner scan, String command){
		List<String> commands = new ArrayList<String>();
		// Get the player name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// get the item name.
		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);
		// get the place name.
		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		
		// Get the player , item and Place.
		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);
		Place place = getPlaceByName(placeName);
		Container container;
		
		// Checks if the item is a container if not returns the empty command.
		if (item instanceof Container){
			container = (Container)item;
		}
		else {
			return commands;
		}
		
		// Make a request of the container handler to check logic
		if (ContainerInteractionHandler.getItemsFromContainer(player, container, place)){
			// if true then make the client command.
			Scanner sc = new Scanner(command);
			sc.next();
			commands.add("Client "+ sc.nextLine());
			sc.close();
		}
		// returns the command for the client to do.
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

	/**
	 * Gets the start place of the world.
	 * 
	 * @return - returns the stating place.
	 */
	protected abstract Place getStartPlace();

	/**
	 * Adds a player to the list of players in the game world.
	 * 
	 * @param player
	 *            - player to be added.
	 */
	protected abstract void addPlayer(Player player);

	/**
	 * Gets the player based on this name.
	 * 
	 * @param playerName
	 *            - player name that is to be looked for.
	 * @return - the player if found else null.
	 */
	protected abstract Player getPlayerByName(String playerName);

	/**
	 * gets the place by the name passed in.
	 * 
	 * @param placeName
	 *            - name of the place that we are looking for.
	 * @return - return the place or null if not found.
	 */
	protected abstract Place getPlaceByName(String placeName);

	/**
	 * get the exit by the name passed in.
	 * 
	 * @param exitName
	 *            - the exit name.
	 * @return - returns the exit or null if none was found.
	 */
	protected abstract Exit getExitByName(String exitName);

	/**
	 * Gets the items that matches the name.
	 * 
	 * @param itemName
	 *            - the items name.
	 * @return - the item that matches the name.
	 */
	protected abstract Item getItemByName(String itemName);

	/**
	 * Adds the exit to the game world.
	 * 
	 * @param exit
	 *            - the exit to be added.
	 */
	public abstract void addExit(Exit exit);

	}
