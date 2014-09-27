package game.world.model;

import java.util.Vector;

import game.world.dimensions.*;

/**
 *
 * @author Shane Brewer
 *
 */
public class Player implements Character{

	public final String name;
	private final Inventory inventory;
	private Point3D position;
	private final Rectangle3D boundngBox;
	private Vector3D direction = new Vector3D(1,0,1);

	public Player(String name, Inventory inventory, Point3D position, Rectangle3D size){
		this.name = name;
		this.inventory = inventory;
		this.position = position;
		this.boundngBox = size;
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

	@Override
	public String getImageName() {
		return "Player";
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

}
