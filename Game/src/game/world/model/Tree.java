package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.ItemImageNames;

/**
 * @author Shane Brewer 300289850
 */
public class Tree extends Enviroment{

	private String imageName = ItemImageNames.treeNames[(int)(ItemImageNames.treeNames.length*Math.random())];

	public Tree(String name, Point3D poisition) {
		super(name, poisition, new Rectangle3D(20, 50+(int)(50*Math.random()), 20));
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public Point3D getPosition(Place place) {
		return getPosition();
	}

}
