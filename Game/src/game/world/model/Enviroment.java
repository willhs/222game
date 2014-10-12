package game.world.model;

import java.io.Serializable;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.Drawable;

/**
 * Used for adding enviromental elements to the game world.
 * @author Shane Brewer 300289850
 *
 */
public  abstract class Enviroment implements Serializable, Drawable{

	private String name;
	private Point3D position;
	private final Rectangle3D boundingBox;

	public Enviroment(String name, Point3D poisition, Rectangle3D boundingBox){
		this.name = name;
		this.position = poisition;
		this.boundingBox = boundingBox;
	}

	public String getName() {
		return name;
	}

	public Point3D getPosition() {
		return position;
	}

	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

}
