package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;

/**
 *
 * @author Shane Brewer
 *
 */
public class Player implements Character{

	public final String name;
	private final Inventory inventory;
	private Point3D position;
	private static final Rectangle3D boundngBox = new Rectangle3D(20, 20, 20);
	private Vector3D direction = new Vector3D(1,0,1);
	private String imageName = "Player";

	public Player(String name){
		this.name = name;
		this.inventory = new Inventory();
		this.position = new Point3D(0, 0, 0);
	}

	public String getName(){
		return name;
	}

	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundngBox;
	}

	public Vector3D getDirection(){
		return direction;
	}
	/**
	 * When the player moves this is used to change his position.
	 * @param newPosition - new position that the player should now be in.
	 */
	public void move(Point3D newPosition){
		direction = Point3D.subtractPoint3D(newPosition, position).unitVector();
		position = newPosition;
	}

	/**
	 * Should add an item to the players inventory.
	 * @param item - item to be added to the inventory.
	 */
	public void addItem(Item item) {
		inventory.addItem(item);
	}

	@Override
	public String getImageName(){
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
