package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;


/**
 * Container
 * defines what it is to be a container in this game.
 * @author Shane Brewer
 *
 */
public abstract class Container implements Item{
	
	private final Inventory contents;
	private final String name;
	private final Rectangle3D boundingBox;
	private Point3D position;
	protected boolean isLocked;
	
	public Container (String name, Inventory contents, Rectangle3D boundingBox, Point3D position){
		this.contents = contents;
		this.name = name;
		this.position = position;
		this.boundingBox = boundingBox;
	}
	
	public Inventory getContents(){
		return contents;
	}
	
	public abstract boolean isLocaked();
	
	public abstract boolean unlock(Inventory playersInventory);
	
	public Point3D getPosition() {
		return position;
	}
	
	public void  setPosition(Point3D point) {
		position = point;
	}
	
	public String getName() {
		return name;
	}
	
	public Rectangle3D getBoundingBox(){
		return boundingBox;
	}
}
