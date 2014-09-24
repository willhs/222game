package game.world.model;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

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

	/**
	 * When the player moves this is used to change his position.
	 * @param newPosition - new position that the player should now be in.
	 */
	public void move(Point3D newPosition){
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
