package game.world.logic;

import game.ui.render.util.Transform;
import game.world.dimensions.*;
import game.world.model.Exit;
import game.world.model.Place;
import game.world.model.Player;
import game.world.util.Parser;

import java.util.*;

/**
 * Handles all the client side modeling and some interation.
 * 
 * @author Shane Brewer.
 * 
 */
public abstract class ClientWorld extends ServerWorld {

	private float movmentScaler = 10.0f;
	private HashMap<String, Transform> keyPressToDirection;
	private Player clientsPlayer;
	private Place currentPlace;

	/**
	 * Used to set up all the key to transform mappings.
	 */
	public ClientWorld() {
		keyPressToDirection = new HashMap<String, Transform>();
		keyPressToDirection.put("Left",
				Transform.newYRotation((float) (Math.PI / 2.0f)));
		keyPressToDirection.put("Up", Transform.newYRotation(0));
		keyPressToDirection.put("Right",
				Transform.newYRotation((float) -(Math.PI / 2.0f)));
		keyPressToDirection.put("Down",
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
	public String getCommand(String action) {
		// where action is like "up", "down", "right", etc
		String command = "";
		if (action.equals("Up") || action.equals("Down")
				|| action.equals("Right") || action.equals("Left")) {

			Vector3D newDirection = keyPressToDirection.get(action)
					.multiply(clientsPlayer.getDirection()).unitVector();
			Vector3D newMove = new Vector3D(newDirection.x * movmentScaler,
					newDirection.y * movmentScaler, newDirection.z
							* movmentScaler);
			Point3D newPosition = clientsPlayer.getPosition()
					.getTranslatedPoint(newMove);
			command = "Server Move Name ( " + clientsPlayer.getName()
					+ " ) Point " + newPosition.toString() + " Name ( "
					+ getPlaceOfPlayer(clientsPlayer).getName() + " ) ";
		}
		if (action.equals("Interact")) {
			System.out.println("made it here");
			command = getInteractionCommand();
		}
		// get the viewing direction from will's static stuff
		// returns a single command like played.x += 10 or something
		// or move player 10 or whatever
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
			}
			if (scan.hasNext("Move")) {
				clientHandleMove(scan);
			}
			if (scan.hasNext("Exit")) {
				clientHandleExiting(scan);
			}
		}

		return commandList;
	}

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
	 * Gets the cunnrent place the player is in.
	 * 
	 * @return - reutns the current place .. might be null.
	 */
	public Place getCurrentPlace() {
		return currentPlace;
	}

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
		return "Server PlayerPlacement Name ( " + player.name + " )";
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
		}
		return true;
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
			if (MovementHandler.checkProximity(
					clientsPlayer.getPosition(),
					clientsPlayer.getBoundingBox(), temp.getPosition(place),
					temp.getBoundingBox())) {

				command = "Server Exit Name ( " + clientsPlayer.getName()
						+ " ) Name ( " + temp.getName() + " ) Name ( "
						+ place.getName() + " )";
			}

		}
		return command;
	}

}
