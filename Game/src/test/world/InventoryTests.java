package test.world;

import static org.junit.Assert.fail;
import game.world.dimensions.Point3D;
import game.world.model.*;

import org.junit.Test;

public class InventoryTests {

	@Test
	public void addTest() {
		Inventory inventory = new Inventory();
		Item item = new Table("table", new Point3D(0,0,0));
		inventory.addItem(item);
		for (Item i: inventory){
			if (!item.equals(i)){
				fail("Should be the only item in there.");
			}
		}
	}

	@Test
	public void removeTest(){
		Inventory inventory = new Inventory();
		Item item = new Table("table", new Point3D(0,0,0));
		inventory.addItem(item);
		inventory.removeItem(item);
		for (Item i:inventory){
			fail("should be empty");
		}
		if (!inventory.isEmpty()){
			fail("should be empty");
		}
	}

	@Test
	public void isInTest(){
		Inventory inventory = new Inventory();
		Item item = new Table("table", new Point3D(0,0,0));
		inventory.addItem(item);
		if (!inventory.isIn(item)){
			fail("item should be in the inventory");
		}
	}

}
