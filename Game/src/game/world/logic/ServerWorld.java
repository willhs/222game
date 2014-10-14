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


	private final Map<String, ServerCommandStratagy> serverCommands;

	public ServerWorld (){
		serverCommands = new HashMap<String, ServerCommandStratagy>();
		serverCommands.put("PlayerPlacement", new ServerPlayerPlacementStratagy());
		serverCommands.put("Move", new ServerMoveStratagy());
		serverCommands.put("Exit", new ServerExitStratagy());
		serverCommands.put("ItemPickUp", new ServerItemPickupStratagy());
		serverCommands.put("ItemDrop", new ServerItemDropStratagy());
		serverCommands.put("Container", new ServerContainerStratagy());
		serverCommands.put("Use", new ServerUseStratagy());
	}

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
	 		ServerCommandStratagy stratagy = serverCommands.get(scan.next());
			if (stratagy == null) return commands;
			commands = stratagy.handleCommand(scan, command, this);
		}
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
