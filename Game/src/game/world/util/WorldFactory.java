package game.world.util;

import game.world.dimensions.*;
import game.world.model.*;

import java.awt.Polygon;
import java.util.*;

public class WorldFactory {

	int counter = 0;
	public World world;
	int internalPlaceSize = 500;
	int randomSize = 500;
	int lockSize = 0;

	public WorldFactory() {
		List<Place> places = new ArrayList<Place>();
		places.add(makeRandomPlaceTwo());
		places.add(makeRandomPlace());
		world = new World(places);
	}

	private Place makeRandomPlace() {
		List<Item> items = new ArrayList<Item>();
		;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[] { 0,
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()), 0 };
		int[] ypoints = new int[] { 0, 0,
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()) };
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		Place place = new Room(items, enviroment, poygon, "Room" + getID());


		items.add(makeKey(place, items, enviroment));
		items.add(makeTable(place, items, enviroment));

		return place;
	}


	private Place makeRandomPlaceTwo() {
		List<Item> items = new ArrayList<Item>();
		;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[] { 0,
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()), 0 };
		int[] ypoints = new int[] { 0, 0,
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()),
				internalPlaceSize + (int) (randomSize * Math.random()) };
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		Place place = new Room(items, enviroment, poygon, "Room" + getID());

		items.add(makeKey(place, items, enviroment));
		items.add(makeTable(place, items, enviroment));

		return place;
	}

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

	public Place makeStartPlace() {
		List<Item> items = new ArrayList<Item>();

		List<Enviroment> enviroment = new ArrayList<Enviroment>();

		int[] xpoints = new int[] { 0, 500, 500, 0 };
		int[] ypoints = new int[] { 0, 0, 300, 300 };
		Polygon poygon = new Polygon(xpoints, ypoints, xpoints.length);

		items.add(new Table("Table" + getID(), new Point3D(400, 0, 30)));

		return new Room(items, enviroment, poygon, "Room" + getID());
	}

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


	private int getLocksize(){
		return lockSize++;
	}


	private int getID(){
		return counter++;
	}


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

	private Exit makeExit(Place placeOne, Place placeTwo){
//		do{}while();
		return null;
	}
}
