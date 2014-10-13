package game.world.util;

import game.world.dimensions.*;
import game.world.model.*;

import java.awt.Polygon;
import java.util.*;

/**
 * @author Shane Brewer 300289850
 */
public class WorldFactory {

	int counter = 0;
	public World world;
	int internalPlaceSize = 500;
	int randomSize = 500;
	int lockSize = 0;

	public WorldFactory() {
		List<Place> places = new ArrayList<Place>();
		Place start  = makeStartPlace();
		places.add(start);
		Place place2 = makeSecondPlace();
		places.add(place2);
		Place place3 = makeThirdPlace();
		places.add(place3);
		Place place4 = makeForthPlace();
		places.add(place4);
		Exit exit = new Portal("Portal"+getID(), start, new Point3D(500, 0, 300), place2, new Point3D(50, 0, 300));
		place2.addExit(exit);
		start.addExit(exit);
		world = new World(places);
		world.addExit(exit);
		exit = new Portal("Portal"+getID(), start, new Point3D(300, 0, 500), place3, new Point3D(350, 0, 250));
		place3.addExit(exit);
		start.addExit(exit);
		world.addExit(exit);
		exit = new Portal("Portal"+getID(), place3, new Point3D(50, 0, 300), place4, new Point3D(750, 0, 500));
		place4.addExit(exit);
		place3.addExit(exit);
		world.addExit(exit);
	}

	/**
 	 * Makes a rendom place with some set items in it.
	 */
	private Place makeSecondPlace() {
		List<Item> items = new ArrayList<Item>();
		;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[] { 200, 400, 400, 200, 200, 400, 400, 200, 200, 0, 0};
		int[] ypoints = new int[] { 0, 0, 200, 200, 400, 600, 800, 800, 600, 400, 200};
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		Place place = new Room(items, enviroment, poygon, "Room" + getID());


		items.add(makeKey(place, items, enviroment));
		items.add(makeTable(place, items, enviroment));

		return place;
	}


	/**
 	 * Makes a rendom place with more random points with some set items in it.
	 */
	private Place makeThirdPlace() {
		List<Item> items = new ArrayList<Item>();
		;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[] { 0, 50, 250, 450, 650, 850, 850, 0};
		int[] ypoints = new int[] { 0, 0 , 200, 200, 0, 200, 400, 400};
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		Place place = new Room(items, enviroment, poygon, "Room" + getID());

		items.add(makeKey(place, items, enviroment));
		items.add(makeTable(place, items, enviroment));

		return place;
	}

	private Place makeForthPlace() {
		List<Item> items = new ArrayList<Item>();
		;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[] { 200, 400, 400, 800, 800, 400, 400, 200, 0, 0};
		int[] ypoints = new int[] { 0, 0 , 400, 400, 600, 600, 1000, 1000, 600, 400};
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		Place place = new Room(items, enviroment, poygon, "Room" + getID());

		items.add(makeKey(place, items, enviroment));
		items.add(makeTable(place, items, enviroment));

		return place;
	}

	/**
 	 * Makes a table at a random point in the place.
	 */
	private Item makeTable(Place place, List<Item> items,
			List<Enviroment> enviroment) {
		Table i = null;
		do {
			float x = (float) (1000 * Math.random());
			float z = (float) (1000 * Math.random());
			i = new Table("Key" + getID(), new Point3D(x, 0, z));
		} while (!place.contains(i.getPosition(), i.getBoundingBox())
				|| checkCollitions(i, items, enviroment));
		return i;
	}


	/**
 	 * Makes randoms a set sized start place.
	 */
	public Place makeStartPlace() {
		List<Item> items = new ArrayList<Item>();

		List<Enviroment> enviroment = new ArrayList<Enviroment>();

		int[] xpoints = new int[] {  200, 400, 600, 600, 400, 200, 0, 0 };
		int[] ypoints = new int[] {  0, 0, 200, 400, 600, 600, 400, 200 };
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		//items.add(new Table("Table" + getID(), new Point3D(400, 0, 30)));

		return new Room(items, enviroment, poygon, "Room" + getID());
	}


	/**
 	 * Checks for collistions a item and other things in a level.
	 */
	public boolean checkCollitions(Item item, List<Item> items, List<Enviroment> enviroment){
		for (Item i: items){
			if(i.getBoundingBox().collisionDetection(i.getPosition(), item.getBoundingBox(), item.getPosition())){

				return true;
			}
		}
		for (Enviroment e : enviroment) {
			if (e.getBoundingBox().collisionDetection(e.getPosition(),
					item.getBoundingBox(), item.getPosition())) {
				return true;
			}
		}
		return false;
	}


	/**
 	 * A way of always incresing lock size
	 */
	private int getLocksize(){
		return lockSize++;
	}


	/**
 	 * a way of always increasing the id atached to a name.
	 */
	private int getID(){
		return counter++;
	}


	/**
 	 * Makes a randomly place crystal in game.
	 */
	private Crystal makeKey(Place place, List<Item> items, List<Enviroment> enviroment){
		Crystal i = null;
		do{
			float x = (float)(1000*Math.random());
			float z = (float)(1000*Math.random());
			i = new Crystal("Key"+getID(), new Point3D(x, 0, z));
		}while (!place.contains(i.getPosition(), i.getBoundingBox())
				|| checkCollitions(i, items, enviroment));
		return i;
	}


	/**
 	 * Todo
	 */
	private Exit makeExit(Place placeOne, Place placeTwo){
//		do{}while();
		return null;
	}
}
