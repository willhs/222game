package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.ItemImageNames;

/**
 * @author Shane Brewer 300289850
 */
public class Plant extends Enviroment{

	private String imageName = ItemImageNames.plantNames[(int)(ItemImageNames.plantNames.length*Math.random())];

	public Plant(String name, Point3D position){
		super(name, position, new Rectangle3D((float)(20+50*Math.random())));
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