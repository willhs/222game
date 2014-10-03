package game.world.model;

import game.ui.render.util.Transform;
import game.world.dimensions.*;
import game.world.logic.MovementHandler;
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
			command = getInteractionCommand();
		}
		// get the viewing direction from will's static stuff
		// returns a single command like played.x += 10 or something
		// or move player 10 or whatever
		return command;
	}

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
			if (scan.hasNext("Exit")){
				clientHandleExiting(scan);
			}
		}
		// applies command to the world
		// returns a list of commands that resulted from running the given
		// command
		// returns an empty string array if the command was invalid or whatever

		// I will call this one by one on the list of commands returned by the
		// server
		return commandList;
	}

	private void clientHandleExiting(Scanner scan) {
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		String playerName = Parser.parseName(scan);
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		String exitName = Parser.parseName(scan);
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		String placeName = Parser.parseName(scan);
		while (!scan.hasNext("Position")){
			scan.next();
		}
		Point3D playerPosition = Parser.parsePosition(scan);

		Player player = getPlayerByName(playerName);
		Place place = getPlaceByName(placeName);
		Exit exit = getExitByName(exitName);

		place.removePlayer(player);

		Place otherPlace = exit.getOtherPlace(place);
		otherPlace.addPlayer(player);

		player.move(playerPosition);

		if (player.name.equals(clientsPlayer.name)){
			currentPlace = otherPlace;
		}
	}

	private void clientHandleMove(Scanner scan) {
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		String playerName = Parser.parseName(scan);

		while (!scan.hasNext("Point")) {
			scan.next();
		}
		Point3D playerPosition = Parser.parsePosition(scan);
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		Player player = getPlayerByName(playerName);
		player.move(playerPosition);
	}

	public Place getCurrentPlace() {
		return currentPlace;
	}

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
		if (getPlayerByName(player.getName()) != null){
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
		while (!scan.hasNext("Name")) {
			scan.next();
		}
		String name = Parser.parseName(scan);
		while (!scan.hasNext("Position")) {
			scan.next();
		}
		Point3D position = Parser.parsePosition(scan);
		Place place = getStartPlace();
		if (name.equals(clientsPlayer.name)) {
			System.out.println("Made it here");
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

}
