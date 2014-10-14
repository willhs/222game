package game.world.model;

import game.world.dimensions.*;
import game.world.logic.MovementHandler;
import game.world.util.*;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.*;

/**
 * Room Class This class defines every thing to do with a room.
 *
 * @author Shane Brewer 300289850
 *
 */
public class Room extends Place {

	public Room(List<Item> items, List<Enviroment> enviroment, Polygon floor, String name) {
		super(name,floor,enviroment,items);
	}

}
