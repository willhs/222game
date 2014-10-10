package game.world.model;

import game.world.dimensions.*;
import game.world.util.*;

/**
 * @author Shane Brewer 300289850
 */
public class Crystal extends Item {

	private static final Rectangle3D boundingBox = new Rectangle3D(20f, 20f, 20f);
	private final String imageName;

	//==================Constructor=================//
	
	public Crystal(String name,
			Point3D position) {
		super(name, position);
		this.imageName = ItemImageNames.crystalNames[(int)(ItemImageNames.crystalNames.length*Math.random())];
	}

	//====================End=======================//

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public boolean canPickUp() {
		return true;
	}

	@Override
	public boolean canDrop() {
		return true;
	}

}
