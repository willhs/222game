package game.world.model;

import game.ui.render.util.Transform;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;
import game.world.logic.MovementHandler;
import game.world.util.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public abstract class ClientWorld extends ServerWorld {

	private float movmentScaler = 10.0f;
	private HashMap<String, Transform> keyPressToDirection;
	private Player clientsPlayer;
	private Place currentPlace;

	public ClientWorld() {
		keyPressToDirection = new HashMap<String, Transform>();
		keyPressToDirection.put("Right",
				Transform.newYRotation((float) (Math.PI / 2.0f)));
		keyPressToDirection.put("Up", Transform.newYRotation(0));
		keyPressToDirection.put("Left",
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
			command = "Server Move Name ( " + clientsPlayer.getName() + " ) Point "
					+ newPosition.toString() + " Name ( "
					+ getPlaceOfPlayer(clientsPlayer).getName() + " ) ";
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

			if (scan.hasNext("Move")){
				clientHandleMove(scan);
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

	private void clientHandleMove(Scanner scan) {
		while(!scan.hasNext("Name")){
			scan.next();
		}
		String playerName = Parser.parseName(scan);
		while (!scan.hasNext("Point")) {
			scan.next();
		}
		Point3D playerPosition = Parser.parsePosition(scan);
		while(!scan.hasNext("Name")){
			scan.next();
		}
		clientsPlayer.move(playerPosition);
	}

	public void replaceCurrentPlace(Place place) {
		currentPlace = place;
	}

	public String getSetClientPlayer(Player player) {
		clientsPlayer = player;
		return "Server PlayerPlacement Name ( " + player.name + " )";
	}

	private boolean setClientPlayer(Scanner scan) {
		while (!scan.hasNext("Position")) {
			scan.next();
		}
		Point3D position = Parser.parsePosition(scan);
		Place place = getStartPlace();
		currentPlace = place;
		this.addPlayer(clientsPlayer);
		clientsPlayer.move(position);
		place.addPlayer(clientsPlayer);
		return true;
	}

}
