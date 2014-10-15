package game.world.logic;

import game.ui.render.util.Transform;
import game.world.dimensions.*;
import game.world.model.*;
import game.world.util.Parser;

import java.util.*;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

/**
 * Handles all the client side modeling and some interation.
 *
 * @author Shane Brewer 300289850
 *
 */
public abstract class ClientWorld extends ServerWorld {

	private boolean hasWon;
	private float movmentScaler = 8.0f;
	private HashMap<String, Transform> keyPressToDirection;
	private Map<String, ClientCommandStratagy> clientCommands;
	private Player clientsPlayer;
	private Place currentPlace;

	/**
	 * Used to set up all the key to transform mappings.
	 */
	public ClientWorld() {
		super();
		// Adds all posible player move directions to hashmap for ease
		// of getting.
		keyPressToDirection = new HashMap<String, Transform>();
		keyPressToDirection.put("Up",
				Transform.newYRotation((float) (Math.PI / 2.0f)));
		keyPressToDirection.put("Right", Transform.newYRotation(0));
		keyPressToDirection.put("Down",
				Transform.newYRotation((float) -(Math.PI / 2.0f)));
		keyPressToDirection.put("Left",
				Transform.newYRotation((float) (Math.PI)));

		// Adds all the stratagys to  a hashmap for ease of getting.
		clientCommands = new HashMap<String, ClientCommandStratagy>();
		clientCommands.put("PlayerPlacement", new ClientAddPlayerStratagy());
		clientCommands.put("Move", new ClientMoveStratagy());
		clientCommands.put("Exit", new ClientExitStratagy());
		clientCommands.put("ItemPickUp", new ClientItemPickupStratagy());
		clientCommands.put("ItemDrop", new ClientItemDropStratagy());
		clientCommands.put("Container", new ClientContainerStratagy());
		clientCommands.put("Use", new ClientUseStratagy());
	}

	public void tick(){
		if(clientsPlayer != null){
			clientsPlayer.tickAirLevel();
		}
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
		// If the clients player doesent exsist.
		if (clientsPlayer == null || getPlayerByName(clientsPlayer.name) == null) return command;
		// checks if the any of the commands are movement related.
		if (action.equals("Up") || action.equals("Down")
				|| action.equals("Right") || action.equals("Left")) {
			command = getMoveCommand(action, viewAngle);

		} // checks if the interaction was happening.
		else if (action.equals("Interact")) {
			command = getInteractionCommand();
		} // checks if a pick up was to happen
		else if (action.equals("PickUp")) {
			command = getContainerInteractCommand();
			// if it wasent a container loot maby they where looting a container.
			if (command.equals("")){
				command = getItemPickUpCommand();
			}
		} // checks if it was an item that was getting droped.
		else if (action.equals("Drop")) {
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
			ClientCommandStratagy cmd = clientCommands.get(scan.next());
			if(cmd == null) return commandList;
			cmd.clientSetCommand(scan, this);
		}

		return commandList;
	}

	//====================================Getters======================================//
	/**
	 * Gets the movement based on the view direction and the key that was pressed.
	 *
	 * @param action -  the action to be preforemed used for getting the key translation.
	 *
	 * @param viewAngle - used for changing the direction the player is going.
	 */
	public String getMoveCommand(String action, float viewAngle){
		String command = "";
		Vector3D newDirection = keyPressToDirection.get(action)
				.multiply(clientsPlayer.getDirection()).unitVector();

		// Changes it based ont he view direction
		// Needed to be negitive becasue the render is using the view wiredly.
		newDirection = Transform.newYRotation(-viewAngle)
				.multiply(newDirection).unitVector();

		// Scales it to the movment amount.
		Vector3D newMove = new Vector3D(newDirection.x * movmentScaler,
				newDirection.y * movmentScaler, newDirection.z
						* movmentScaler);
		// Gets the new position the player wishes to be.
		Point3D newPosition = clientsPlayer.getPosition()
				.getTranslatedPoint(newMove);
		// Sructures a command to get the player to move.
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
		// if the player already exsists then we dont need to make the player.
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
		// looks through all the exits.
		while (exits.hasNext()) {
			Exit temp = exits.next();
			// checks if that exit is in proximity.
			if (MovementHandler.checkProximity(clientsPlayer.getPosition(),
					clientsPlayer.getBoundingBox(), temp.getPosition(place),
					temp.getBoundingBox())) {
				// then structure a command for the server.
				command = "Server Exit Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + temp.getName() + " ) Name ( "
						+ place.getName() + " )";
			}

		}
		if (command.equals("")){
			for (Item item : clientsPlayer.getInventory()){
				if(item.isSlelected() && item.canUse()){
					command = "Server Use Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + item.getName() +" ) ";
				}
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
		// looks through all the items in the place.
		while (items.hasNext()) {
			Item item = items.next();
			// checks item proximity
			if (ItemInteractionHandler.checkProximity(
					clientsPlayer.getPosition(),
					clientsPlayer.getBoundingBox(), item.getPosition(),
					item.getBoundingBox())) {
				// structers a command for the server.
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
		// Checks the inventory
		for (Item item : inventory) {
			// if the item is selected then we can drop it.
			if (item.isSlelected()) {
				// structures a command for the server.
				command = "Server ItemDrop Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + item.getName() + " )  Name ( "
						+ currentPlace.getName() + " )";
			}
		}
		return command;
	}

	/**
	 * Gets the command for interacting with a container.
	 * @return - return a structured command for interation or and empty string.
	 */
	private String getContainerInteractCommand() {
		String command = "";
		Iterator<Item> items = currentPlace.getItems();
		// looks thorught all the items.
		while (items.hasNext()) {
			Item item = items.next();
			// checks if they are a container.
			// and in proximity
			if (item instanceof Container
					&& ItemInteractionHandler.checkProximity(
							item.getPosition(), item.getBoundingBox(),
							clientsPlayer.getPosition(),
							clientsPlayer.getBoundingBox())) {
				// if they are then we will make the command.
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

	/**
	 * Check for if the game is over
	 * @return - true if the game is over.
	 */
	public boolean hasWon(){
		return hasWon;
	}

	/**
	 * Change the hasWon boolean
	 * @param change - change for hasWon.
	 */
	protected void changeHasWon(boolean change){
		hasWon = change;
	}

	//=============================================Getters End===================================//

	protected void setCurrentPlace(Place place){
		currentPlace = place;
	}

	protected Player getClientsPlayer(){
		return clientsPlayer;
	}

}
