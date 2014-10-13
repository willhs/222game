package test.world;

import static org.junit.Assert.fail;

import org.junit.Test;

import game.world.dimensions.*;
import game.world.model.*;
import game.world.util.Drawable;

public class PlayerTests {

	@Test
	public void playerNameTest() {
		Player player = new Player("Bob");
		if (!"Bob".equals(player.getName())){
			fail("Name should have been bob.");
		}
	}

	@Test
	public void playerInventoryTest(){
		Player player = new Player("Bob");
		for (Item i: player.getInventory()){
			fail("Starting player have an empty inventory.");
		}
	}

	@Test
	public void playerDrawableTest(){
		Drawable player = new Player("Bob");
		if (!player.getPosition().equals(new Point3D(0, 0, 0))){
			fail("Players should start at 0,0,0");
		}
		if (player.getBoundingBox() == null){
			fail("players all have the same bounding box.");
		}
		if (player.getImageName() == null){
			fail("Players should have a image name.");
		}
	}

	@Test
	public void playerAirLevelsTest(){
		Player player = new Player("Bob");
		if (player.getAirLevel() != 100){
			fail("Players air level should be 100 at the start");
		}
		player.tickAirLevel();
		if (player.getAirLevel() != 99){
			fail("Players air level should be 99 after one tick");
		}
		player.addAir();
		if (player.getAirLevel() != 100){
			fail("Players air level should be 100 at after adding air.");
		}
	}

	@Test
	public void playerMoveTest(){
		Player player = new Player("Bob");
		Point3D point = new Point3D(20,20,20);
		player.move(point);
		if (!player.getPosition().equals(point)){
			fail("The new positions should be the same.");
		}
	}

	@Test
	public void playerDirectionTest(){
		Player player = new Player("Bob");
		Point3D point = new Point3D(20,20,20);
		Vector3D direction = player.getDirection();
		player.move(point);
		if(!player.getDirection().equals(direction)){
			fail("The direction should not change.");
		}
	}
}
