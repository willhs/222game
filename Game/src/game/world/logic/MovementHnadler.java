package game.world.logic;

import game.world.dimensions.*;
import game.world.model.*;

public class MovementHnadler {
	
	/**
	 * This will move the player from its old position to the new one
	 * This method will handle all the rules for movement.
	 * @param player - player to be moved.
	 * @param to - position the player wants to move to.
	 * @param place - the place that the player is moving in.
	 * @param toIgnore - Items to ignore collisions from.
	 * @return returns true if a change has been made to the players position.
	 */
	public static boolean playerMove(Player player, Point3D to,  Place place, Item...toIgnore){
		if (place.contains(to)){
			return false;
		}
		return true;
	}
}
