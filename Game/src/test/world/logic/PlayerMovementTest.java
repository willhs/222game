package test.world.logic;

import static org.junit.Assert.fail;
import game.world.dimensions.Point3D;
import game.world.logic.MovementHandler;
import game.world.model.Place;
import game.world.model.Player;
import game.world.model.World;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;

public class PlayerMovementTest {

	@Test
	public void test() {
		World world = makeWorld();
		world.addPlayerToGameWorld(new Player("Jim"));

		Player player =  world.getPlayers().next();
		Place place = world.getPlaces().next();
		Point3D point = player.getPosition();

		for (int i = 0; i < 1; i++){
			MovementHandler.playerMove(player, new Point3D(point.x, point.y, point.z+30), place);
		}

		if (point.z == player.getPosition().z){
			fail("Player should have moved");
		}


	}

	private World makeWorld(){
		return new SingleRoomWorldTest().world;
	}

}
