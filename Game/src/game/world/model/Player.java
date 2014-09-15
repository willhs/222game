package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

public class Player implements Character{
	
	public final String name;
	private final Inventory inventory;
	private final Point3D position;
	private final Rectangle3D boundngBox;
	
	public Player(String name, Inventory inventory, Point3D position, Rectangle3D size){
		this.name = name;
		this.inventory = inventory;
		this.position = position;
		this.boundngBox = size;
	}
	
	public String getName(){
		return name;
	}
	
	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundngBox;
	}
	
}
