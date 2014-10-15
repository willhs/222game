package game.world.logic;

import game.world.dimensions.*;
import game.world.model.*;
import game.world.util.Parser;

import java.io.Serializable;
import java.util.*;


/**
 * @author Shane Brewer 300289850
 */
public interface ClientCommandStratagy extends Serializable{
	/**
	 * Used to set values in the world depending on if the server
	 *
	 * @param scan - has the command in it.
	 *
	 * @param world - the current world that is to be modifyed by the setters.
	 */
	public void clientSetCommand(Scanner scan, ClientWorld world);
}

class ClientExitStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// gets the name of the player that is to be moved.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// gets the name of the exit the player moved through
		Parser.removeUnneedText("Name", scan);
		String exitName = Parser.parseName(scan);
		// gets the name of the place that the player will be in.
		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		// gets the new position in the new room.
		Parser.removeUnneedText("Position", scan);
		Point3D playerPosition = Parser.parsePosition(scan);
		// gets the all from the world.
		Player player = world.getPlayerByName(playerName);
		Place place = world.getPlaceByName(placeName);
		Exit exit = world.getExitByName(exitName);
		exit.setLocked(false);
		if (!exit.isLocked() && exit instanceof FinishPortal){
			world.changeHasWon(true);
			System.out.println("Should win");
		}
		// removes the player form his old place.
		place.removePlayer(player);
		// adds him to the new one.
		Place otherPlace = exit.getOtherPlace(place);
		otherPlace.addPlayer(player);
		// and moves him to his new position.
		player.move(playerPosition);
		// and if the players that moved is the clients on then the new current
		// place is the other place
		if (player.name.equals(world.getClientsPlayer().name)) {
			world.setCurrentPlace(otherPlace);
		}
	}
}


class ClientItemPickupStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the players name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the items name.
		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);
		// Gets the place name.
		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		// gets the place, player and items from the world.
		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		Place place = world.getPlaceByName(placeName);
		// removes item from place
		// adds item to the players inventory
		// sets items position.
		place.removeItem(item);
		player.getInventory().addItem(item);
		item.setPosition(new Point3D(0, 0, 0));
	}
}

class ClientItemDropStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the players name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the item name.
		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);
		// Gets the placeName.
		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);
		// Gets the point that the item will fall.
		Parser.removeUnneedText("Point", scan);
		Point3D position = Parser.parsePosition(scan);
		// Gets player item and place from the world.
		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		Place place = world.getPlaceByName(placeName);
		// handles all mnoving and setting logic that
		// comes with droping and item.
		item.setSelected(false); // items that are nolonger in the inventory cant be selected.
		player.getInventory().removeItem(item);
		place.addItem(item);
		item.setPosition(position);
	}
}

class ClientContainerStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the player name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the item name.
		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);
		// gets the item and player from the world.
		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);
		// Item is a container server confermed.
		Container container = (Container) item;
		// Adds and removes items.
		for (Item in: container.getContents()){
			player.addItem(in);
		}
		for (Item in: player.getInventory()){
			container.getContents().removeItem(in);
		}
	}
}

class ClientMoveStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the players name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the players position.
		Parser.removeUnneedText("Point", scan);
		Point3D playerPosition = Parser.parsePosition(scan);
		// moves the player to the new position.
		Player player = world.getPlayerByName(playerName);
		player.move(playerPosition);
	}
}

class ClientAddPlayerStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the player name.
		Parser.removeUnneedText("Name", scan);
		String name = Parser.parseName(scan);
		// Gets the image name of the player.
		Parser.removeUnneedText("Image", scan);
		String imageName = Parser.parseName(scan);
		// Gets the postion of the player.
		Parser.removeUnneedText("Position", scan);
		Point3D position = Parser.parsePosition(scan);
		// Get the players place form the world
		Place place = world.getStartPlace();
		// Logic for if the player is this clients player.
		if (name.equals(world.getClientsPlayer().name)) {
			world.setCurrentPlace(place);
			world.addPlayer(world.getClientsPlayer());
			world.getClientsPlayer().move(position);
			place.addPlayer(world.getClientsPlayer());
		}
		// Logic for if it is not the clients player.
		else {
			Player player = new Player(name);
			world.addPlayer(player);
			world.getPlayerByName(name).move(position);
			place.addPlayer(player);
			player.setImageName(imageName);
		}
	}
}

class ClientUseStratagy implements ClientCommandStratagy{
	public void clientSetCommand(Scanner scan, ClientWorld world){
		// Gets the player name.
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);
		// Gets the item of the player.
		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Player player = world.getPlayerByName(playerName);
		Item item = world.getItemByName(itemName);

		if (item.canUse() && item instanceof UsableItem){
			UsableItem usable = (UsableItem) item;
			usable.use(player);
			player.getInventory().removeItem(item);
		}
	}
}