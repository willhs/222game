package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.util.*;

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
