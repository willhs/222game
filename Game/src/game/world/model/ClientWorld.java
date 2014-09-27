package game.world.model;

import game.ui.render.util.Transform;

import java.util.*;

public abstract class ClientWorld extends ServerWorld{

	private float movmentScaler = 10.0f;
	private HashMap<String, Transform> keyPressToDirection;
	private Player clientsPlayer;

	public ClientWorld(){
		keyPressToDirection = new HashMap<String, Transform>();
		keyPressToDirection.put("Right", Transform.newYRotation((float)(Math.PI/2.0f)));
		keyPressToDirection.put("Up", Transform.newYRotation(0));
		keyPressToDirection.put("Left", Transform.newYRotation((float)-(Math.PI/2.0f)));
		keyPressToDirection.put("Down", Transform.newYRotation((float)(Math.PI)));
	}

	public String getCommand(String action){
		//where action is like "up", "down", "right", etc

		//get the viewing direction from will's static stuff
		//returns a single command like played.x += 10 or something
		//or move player 10 or whatever
		return null;
	}

	public List<String> applyCommand(String command){
		List<String> commandList = super.applyCommand(command);
		//applies command to the world
		//returns a list of commands that resulted from running the given command
		//returns an empty string array if the command was invalid or whatever

		//I will call this one by one on the list of commands returned by the server
		return null;
	}

	public void replaceCurrentRoom(Room room){
		//replaces the current room with the given room object
	}

	public String getSetClientPlayer(Player player){
		clientsPlayer = player;
		return "ServerPlayerPlacement Name ( "+player.name+" ) BoundingBox ( "+player.getBoundingBox()+" )";
	}
}
