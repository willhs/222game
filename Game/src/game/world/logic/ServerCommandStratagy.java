package game.world.logic;

import game.world.dimensions.*;
import game.world.model.*;
import game.world.util.Parser;

import java.io.Serializable;
import java.util.*;

/**
 * Stratagy for player commands being prossesed.
 * @author brewershan
 *
 */
public interface ServerCommandStratagy extends Serializable{
	/**
	 * Handles commands for the server
	 * @param scan - has the clients command in it.
	 * @param command - the command that is the clients.
	 */
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world);
}
//=============================Implementations=============================================//

class ServerPlayerPlacementStratagy implements ServerCommandStratagy{
	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
		List<String> commands = new ArrayList<String>();
		Player player = Parser.parsePlayer(scan);
		Parser.removeUnneedText("Image", scan);
		String imageName = Parser.parseName(scan);
		player.setImageName(imageName);
		if (world.addPlayerToGameWorld(player)) {
			String newCommand = "Client PlayerPlacement Name ( "
					+ player.getName() + " ) Image ( " +imageName+" ) Position ( "
					+ player.getPosition().toString() + " )";
			commands.add(newCommand);
		}
		return commands;
	}
}


/**
 * Handles any move being made by the player.
 * @author brewershan
 *
 */
class ServerMoveStratagy implements ServerCommandStratagy{

	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
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
		if (MovementHandler.playerMove(world.getPlayerByName(playerName),
				playerPosition, world.getPlaceByName(placeName))) {
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
}

/**
 * Handles any exits being taken by players.
 * @author brewershan
 *
 */
class ServerExitStratagy implements ServerCommandStratagy{

	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String exitName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		if (MovementHandler.exitPlace(world.getPlayerByName(playerName),
				world.getPlaceByName(placeName), world.getExitByName(exitName))) {
			Scanner sc = new Scanner(command);
			sc.next();
			Player player = world.getPlayerByName(playerName);
			commands.add("Client " + sc.nextLine() + " Position ( "
					+ player.getPosition() + " )");
			sc.close();
		}
		return commands;
	}
}

/**
 * Handles any items being picked up by players.
 * @author brewershan
 *
 */
class ServerItemPickupStratagy implements ServerCommandStratagy{

	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		Place place = world.getPlaceByName(placeName);

		if (ItemInteractionHandler.pickupItem(player, item, place)){
			Scanner sc = new Scanner(command);
			sc.next();
			commands.add("Client "+ sc.nextLine());
			sc.close();
		}
		return commands;
	}
}

/**
 * Handles any Players droping there items.
 * @author brewershan
 *
 */
class ServerItemDropStratagy implements ServerCommandStratagy{

	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
		List<String> commands = new ArrayList<String>();

		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		Place place = world.getPlaceByName(placeName);

		if (ItemInteractionHandler.dropItem(player, item, place)){
			Scanner sc = new Scanner(command);
			sc.next();
			commands.add("Client "+ sc.nextLine() + " Point " + item.getPosition());
			sc.close();
		}
		return commands;
	}
}

/**
 * Handles any server container interactions.
 * @author Shane Brewer
 *
 */
class ServerContainerStratagy implements ServerCommandStratagy{

	@Override
	public List<String> handleCommand(Scanner scan, String command, ServerWorld world){
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
		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		Place place = world.getPlaceByName(placeName);
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
}

