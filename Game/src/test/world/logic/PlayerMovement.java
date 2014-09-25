package test.world.logic;

import static org.junit.Assert.*;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Inventory;
import game.world.model.Player;
import game.world.model.World;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;

public class PlayerMovement {
	
	@Test
	public void test() {
		World world = makeWorld();
		world.addPlayerToGameWorld(new Player("Jim", new Inventory(), new Point3D(0, 0, 0), new Rectangle3D(20, 20, 20)));
		fail("Not yet implemented");
	}
	
	private World makeWorld(){
		return new SingleRoomWorldTest().world;
	}
	
}
