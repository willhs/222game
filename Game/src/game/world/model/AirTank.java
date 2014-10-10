package game.world.model;

import game.world.dimensions.*;

/**
 * @author Shane Brewer
 */
public class AirTank extends UsableItem{

	private final String name;
	private Point3D position;
	private final String imageName;
	private boolean isSelected;
	private final Rectangle3D boundingBox = new Rectangle3D(20, 20 , 20);


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
		player.addAir();
		return true;
	}

	@Override
	public boolean canDrop() {
		return false;
	}

	@Override
	public boolean isSlelected() {
		return isSelected;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}
}