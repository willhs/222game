package game.world.model;

import java.util.*;

public abstract class ClientWorld extends ServerWorld{
	public String getCommand(String action){
		//where action is like "up", "down", "right", etc

		//get the viewing direction from will's static stuff
		//returns a single command like played.x += 10 or something
		//or move player 10 or whatever
		return null;
	}

	public List<String> applyCommand(String command){
		//applies command to the world
		//returns a list of commands that resulted from running the given command
		//returns an empty string array if the command was invalid or whatever

		//I will call this one by one on the list of commands returned by the server
		return null;
	}

	public void replaceCurrentRoom(Room room){
		//replaces the current room with the given room object
	}


}
