package game.world.logic;

import game.ui.render.util.Transform;
import game.world.dimensions.*;
import game.world.model.*;
import game.world.util.Parser;

import java.util.*;

/**
 * Handles all the client side modeling and some interation.
 *
 * @author Shane Brewer.
 *
 */
public abstract class ClientWorld extends ServerWorld {

	private float movmentScaler = 8.0f;
	private HashMap<String, Transform> keyPressToDirection;
	private Player clientsPlayer;
	private Place currentPlace;

	/**
	 * Used to set up all the key to transform mappings.
	 */
	public ClientWorld() {
		keyPressToDirection = new HashMap<String, Transform>();
		keyPressToDirection.put("Up",
				Transform.newYRotation((float) (Math.PI / 2.0f)));
		keyPressToDirection.put("Right", Transform.newYRotation(0));
		keyPressToDirection.put("Down",
				Transform.newYRotation((float) -(Math.PI / 2.0f)));
		keyPressToDirection.put("Left",
				Transform.newYRotation((float) (Math.PI)));
	}

	/**
	 * Takes a commands form the client and returns a command intended for the
	 * server World.
	 *
	 * @param action
	 *            - the action the client wishes to do.
	 * @return - a string the represents a command or the empty string.
	 */
	public String getCommand(String action, float viewAngle) {
		// where action is like "up", "down", "right", etc
		String command = "";
		if (action.equals("Up") || action.equals("Down")
				|| action.equals("Right") || action.equals("Left")) {
			command = getMoveCommand(action, viewAngle);
		} else if (action.equals("Interact")) {
			command = getInteractionCommand();
		} else if (action.equals("PickUp")) {
			command = getContainerInteractCommand();
			if (command.equals("")){
				command = getItemPickUpCommand();
			}
		} else if (action.equals("Drop")) {
			command = getItemDropCommand();
		}
		return command;
	}

	@Override
	public List<String> applyCommand(String command) {
		List<String> commandList = super.applyCommand(command);
		Scanner scan = new Scanner(command);
		if (scan.hasNext("Client")) {
			scan.next();
			if (scan.hasNext("PlayerPlacement")) {
				setClientPlayer(scan);
			} else if (scan.hasNext("Move")) {
				clientHandleMove(scan);
			} else if (scan.hasNext("Exit")) {
				clientHandleExiting(scan);
			} else if (scan.hasNext("ItemPickUp")) {
				clientHandleItemPickUp(scan);
			} else if (scan.hasNext("ItemDrop")) {
				clientHandleItemDrop(scan);
			} else if (scan.hasNext("Container")){
				clientHandleContainer(scan);
			}
		}

		return commandList;
	}

	//=================================Setters===================================//

	/**
	 * Handles the server commands for exiting a room.
	 *
	 * @param scan
	 *            - scanner that has the command in it.
	 */
	private void clientHandleExiting(Scanner scan) {
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
		Player player = getPlayerByName(playerName);
		Place place = getPlaceByName(placeName);
		Exit exit = getExitByName(exitName);

		// removes the player form his old place.
		place.removePlayer(player);
		// adds him to the new one.
		Place otherPlace = exit.getOtherPlace(place);
		otherPlace.addPlayer(player);
		// and moves him to his new position.
		player.move(playerPosition);
		// and if the players that moved is the clients on then the new current
		// place is the other place
		if (player.name.equals(clientsPlayer.name)) {
			currentPlace = otherPlace;
		}
	}

	/**
	 * Handles the item pick up on the client side.
	 *
	 * @param scan
	 *            - the scanner in the clinet.
	 */
	private void clientHandleItemPickUp(Scanner scan) {
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);
		Place place = getPlaceByName(placeName);

		place.removeItem(item);
		player.getInventory().addItem(item);
		item.setPosition(new Point3D(0, 0, 0));
	}

	private void clientHandleItemDrop(Scanner scan) {
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String placeName = Parser.parseName(scan);

		Parser.removeUnneedText("Point", scan);
		Point3D position = Parser.parsePosition(scan);

		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);
		Place place = getPlaceByName(placeName);

		item.setSelected(false);
		player.getInventory().removeItem(item);
		place.addItem(item);
		item.setPosition(position);
	}

	private void clientHandleContainer(Scanner scan){
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Name", scan);
		String itemName = Parser.parseName(scan);

		Player player = getPlayerByName(playerName);
		Item item = getItemByName(itemName);

		Container container = (Container) item;
		for (Item in: container.getContents()){
			player.addItem(in);
		}
		for (Item in: player.getInventory()){
			container.getContents().removeItem(in);
		}
	}

	/**
	 * Handles a server request to move a player in the client world.
	 *
	 * @param scan
	 *            - scanner with the command in it.
	 */
	private void clientHandleMove(Scanner scan) {
		Parser.removeUnneedText("Name", scan);
		String playerName = Parser.parseName(scan);

		Parser.removeUnneedText("Point", scan);
		Point3D playerPosition = Parser.parsePosition(scan);

		Parser.removeUnneedText("Name", scan);
		// moves the player to the new position.
		Player player = getPlayerByName(playerName);
		player.move(playerPosition);
	}

	

	/**
	 * Sets the client player.
	 *
	 * @param scan
	 *            - used to scan the text to get the player out.
	 * @return - return true if the player was moved.
	 */
	private boolean setClientPlayer(Scanner scan) {

		Parser.removeUnneedText("Name", scan);
		String name = Parser.parseName(scan);

		Parser.removeUnneedText("Image", scan);
		String imageName = Parser.parseName(scan);

		Parser.removeUnneedText("Position", scan);
		Point3D position = Parser.parsePosition(scan);

		Place place = getStartPlace();
		if (name.equals(clientsPlayer.name)) {
			currentPlace = place;
			this.addPlayer(clientsPlayer);
			clientsPlayer.move(position);
			place.addPlayer(clientsPlayer);
		} else {
			Player player = new Player(name);
			this.addPlayer(player);
			getPlayerByName(name).move(position);
			place.addPlayer(player);
			player.setImageName(imageName);
		}
		return true;
	}
	
	//===================================Setters End===================================//

	//====================================Getters======================================//
	
	public String getMoveCommand(String action, float viewAngle){
		String command = "";
		Vector3D newDirection = keyPressToDirection.get(action)
				.multiply(clientsPlayer.getDirection()).unitVector();

		newDirection = Transform.newYRotation(-viewAngle)
				.multiply(newDirection).unitVector();

		Vector3D newMove = new Vector3D(newDirection.x * movmentScaler,
				newDirection.y * movmentScaler, newDirection.z
						* movmentScaler);
		Point3D newPosition = clientsPlayer.getPosition()
				.getTranslatedPoint(newMove);
		command = "Server Move Name ( " + clientsPlayer.getName()
				+ " ) Point " + newPosition.toString() + " Name ( "
				+ getPlaceOfPlayer(clientsPlayer).getName() + " ) ";
		return command;
	}

	/**
	 * Makes the first player.
	 *
	 * @param player
	 *            - player that is to be the player of this client.
	 * @return - a string only ment to be parsed by the server.
	 */
	public String getSetClientPlayer(Player player) {
		clientsPlayer = player;
		if (getPlayerByName(player.getName()) != null) {
			return "";
		}
		String command = "Server PlayerPlacement Name ( " + player.name + " ) Image ( " +player.getImageName()+" ) ";

		return command;
	}

	/**
	 * Gets the Interaction command from the exit. This one dose some
	 * computation to find a exit nere the player that they can interact with.
	 *
	 * @return - returns the string intended for the server world to handle.
	 */
	private String getInteractionCommand() {
		Place place = getPlaceOfPlayer(clientsPlayer);
		Iterator<Exit> exits = place.getExits();
		String command = "";
		while (exits.hasNext()) {
			Exit temp = exits.next();
			if (MovementHandler.checkProximity(clientsPlayer.getPosition(),
					clientsPlayer.getBoundingBox(), temp.getPosition(place),
					temp.getBoundingBox())) {

				command = "Server Exit Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + temp.getName() + " ) Name ( "
						+ place.getName() + " )";
			}

		}
		return command;
	}

	/**
	 * Gets the commands for picking up items.
	 *
	 * @return - return a string for the pickup command if unable string will be
	 *         empty.
	 */
	private String getItemPickUpCommand() {
		Place place = getCurrentPlace();
		Iterator<Item> items = place.getItems();
		String command = "";
		while (items.hasNext()) {
			Item item = items.next();
			if (ItemInteractionHandler.checkProximity(
					clientsPlayer.getPosition(),
					clientsPlayer.getBoundingBox(), item.getPosition(),
					item.getBoundingBox())) {
				command = "Server ItemPickUp Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + item.getName() + " ) Name ( "
						+ place.getName() + " )";
			}
		}
		return command;
	}

	/**
	 * Gets the command for droping an item
	 *
	 * @return - returns the command for the server or an expty string.
	 */
	private String getItemDropCommand() {
		String command = "";
		Inventory inventory = clientsPlayer.getInventory();
		for (Item item : inventory) {
			if (item.isSlelected()) {
				command = "Server ItemDrop Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + item.getName() + " )  Name ( "
						+ currentPlace.getName() + " )";
			}
		}
		return command;
	}

	private String getContainerInteractCommand() {
		String command = "";
		Iterator<Item> items = currentPlace.getItems();
		while (items.hasNext()) {
			Item item = items.next();
			if (item instanceof Container
					&& ItemInteractionHandler.checkProximity(
							item.getPosition(), item.getBoundingBox(),
							clientsPlayer.getPosition(),
							clientsPlayer.getBoundingBox())) {
				command =  "Server Container Name ( "+ clientsPlayer.getName()
						+ " ) Name ( " + item.getName() + " )  Name ( "
						+ currentPlace.getName() + " )";
			}
		}
		return command;
	}

	/**
	 * Gets the cunnrent place the player is in.
	 *
	 * @return - reutns the current place .. might be null.
	 */
	public Place getCurrentPlace() {
		return currentPlace;
	}



	//=============================================Getters End===================================//



	/**
	 * replaces the current place with a new one.
	 *
	 * @param place
	 *            - the place that this current player should be in.
	 */
	public void replaceCurrentPlace(Place place) {
		place.removePlayer(clientsPlayer);
		place.addPlayer(clientsPlayer);
		currentPlace = place;
	}

	
}
