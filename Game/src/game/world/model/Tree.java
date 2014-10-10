package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

/**
 * @author Shane Brewer 300289850
 */
public class Tree extends Enviroment{

	public Tree(String name, Point3D poisition) {
		super(name, poisition, new Rectangle3D(20, 50, 20));
	}



}
