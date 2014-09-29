package test.world;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import game.world.model.ClientWorld;
import game.world.model.Player;
import game.world.model.ServerWorld;

import java.util.Iterator;
import java.util.List;

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
		System.out.println("up");
		clientWorld.getCommand("Up");
		System.out.println("down");
		clientWorld.getCommand("Down");
		System.out.println("left");
		clientWorld.getCommand("Left");
		System.out.println("right");
		clientWorld.getCommand("Right");
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
