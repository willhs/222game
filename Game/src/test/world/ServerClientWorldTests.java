package test.world;

import static org.junit.Assert.*;

import java.util.*;

import game.world.dimensions.*;
import game.world.model.*;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;

public class ServerClientWorldTests {

	@Test
	public void test() {
		ServerWorld serverWorld = new SingleRoomWorldTest().world;
		ClientWorld clientWorld = new SingleRoomWorldTest().world;

		String command = clientWorld.getSetClientPlayer(new Player(
				"FattyBoomBoom", new Inventory(), new Point3D(0, 0, 0),
				new Rectangle3D(20, 20, 20)));

		List<String> commands = serverWorld.applyCommand(command);

		for (String c: commands){
			clientWorld.applyCommand(command);
		}
		Iterator<Player> players = clientWorld.getPlayers();
		while(players.hasNext()){
			if (players.next().getName().equals("FattyBoomBoom")){
				assertTrue(true);
			}
		}
		fail("Not yet implemented");
	}

}
