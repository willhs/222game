package game.world.model;

public class Player implements Character{
	
	public final String name;
	private final Inventory inventory;
	
	public Player(String name, Inventory inventory){
		this.name = name;
		this.inventory = inventory;
	}
	
	public String getName(){
		return name;
	}
	
	public Inventory getInventory(){
		return inventory;
	}
	
}
