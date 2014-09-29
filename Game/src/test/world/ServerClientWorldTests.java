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
				"FattyBoomBoom"));

		List<String> commands = serverWorld.applyCommand(command);
		System.out.println("helpers");
		System.out.println(commands);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		Iterator<Player> players = clientWorld.getPlayers();
		System.out.println(players);
		boolean um = false;
		while(players.hasNext()){
			if (players.next().getName().equals("FattyBoomBoom")){
				assertTrue(true);
				um = true;
			}
		}
		if (!um){
			fail("Not yet implemented");
		}
	}

}
