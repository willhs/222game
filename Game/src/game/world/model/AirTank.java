package game.world.model;

import game.world.dimensions.*;

/**
 * @author Shane Brewer
 */
public class AirTank implements UsableItem{

	private final String name;
	private Point3D position;
	private final String imageName;


	public AirTank(String name, Point3D position){
		this.name = name;
		this.position = position;
		imageName = "AirTank";
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public Point3D getPosition(){
		return position;
	}

	@Override
	public boolean canPickUp(){
		return true;
	}

	@Override
	public void setPosition(Point3D point){
		position = point;
	}

	@Override
	public boolean isSelected(){
		return isSelected;
	}

	@Override
	public void setSelected(boolean change){
		isSelected = change;
	}

	@Override
	public Point3D getPosition(){
		return position;
	}

	@Override
	public Point3D getPosition(Place place){
		return position;
	}

	@Override
	public String getImageName(){
		return imageName;
	}

	@Override
	public boolean use(Player player){

	}
}