package game.world.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.*;

import java.awt.Polygon;
import java.util.*;

public class WorldFactory {
	
	int counter = 0;
	public World world;
	
	public WorldFactory(){
		List<Place> places = new ArrayList<Place>();
		places.add(makeRandomPlace());
		places.add(makeRandomPlace());
		world = new World(places);
	}
	
	private Place makeRandomPlace() {
		List<Item> items = new ArrayList<Item>();;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		int[] xpoints = new int[]{0, (int)(1000*Math.random()), (int)(1000*Math.random()), 0};
		int[] ypoints = new int[]{0 ,0 , (int)(1000*Math.random()), (int)(1000*Math.random())};
		Polygon poygon = new Polygon(xpoints, ypoints ,xpoints.length);
		
		Place place = new Room(items, enviroment, poygon, "Room"+getID());
		 
		float x = (float)(1000*Math.random());
		float z = (float)(1000*Math.random());
		Item i = new Key("Key"+getID(), new Point3D(x, 0, z));
		while (!place.contains(i.getPosition(), i.getBoundingBox())
				|| checkCollitions(i, items, enviroment)){
			x = (float)(1000*Math.random());
			z = (float)(1000*Math.random());
			i = new Key("Key"+getID(), new Point3D(x, 0, z));
		}
		items.add(i);
		
		do{
			x = (float)(1000*Math.random());
			z = (float)(1000*Math.random());
			i = new Table("Table"+getID(), new Point3D(x, 0, z));
		}while (!place.contains(i.getPosition(), i.getBoundingBox())
				|| checkCollitions(i, items, enviroment));
		items.add(i);
		
		
		items.add(new Table("Table"+getID(), new Point3D(400, 0, 30)));
		
		return place;
	}

	public Place makeStartPlace(){
		List<Item> items = new ArrayList<Item>();;
		List<Enviroment> enviroment = new ArrayList<Enviroment>();
		
		int[] xpoints = new int[]{0, 500, 500, 0};
		int[] ypoints = new int[]{0 ,0 , 300, 300};
		Polygon poygon = new Polygon(xpoints, ypoints ,xpoints.length);
		
		items.add(new Table("Table"+getID(), new Point3D(400, 0, 30)));
		
		return new Room(items, enviroment, poygon, "Room"+getID());
	}
	
	public boolean checkCollitions(Item item, List<Item> items, List<Enviroment> enviroment){
		for (Item i: items){
			if(i.getBoundingBox().collisionDetection(i.getPosition(), item.getBoundingBox(), item.getPosition())){
				return true;
			}
		}
		for (Enviroment e: enviroment){
			if(e.getBoundingBox().collisionDetection(e.getPosition(), item.getBoundingBox(), item.getPosition())){
				return true;
			}
		}
		return false;
	}
	
	private int getID(){
		return counter++;
	}
	
}
