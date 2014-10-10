package game.world.model;

import game.world.dimensions.*;

/**
 * @author Shane Brewer
 */
public class AirTank extends UsableItem{

	private final String imageName;
	private final Rectangle3D boundingBox = new Rectangle3D(20, 20 , 20);


	public AirTank(String name, Point3D position){
		super(name, position);
		imageName = "oxygen_tank_labelled";
	}

	@Override
	public boolean canPickUp(){
		return true;
	}

	@Override
	public String getImageName(){
		return imageName;
	}

	@Override
	public boolean use(Player player){
		player.addAir();
		return true;
	}

	@Override
	public boolean canDrop() {
		return false;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}
}