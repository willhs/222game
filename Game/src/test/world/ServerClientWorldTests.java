package test.world;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import game.world.logic.ClientWorld;
import game.world.logic.ServerWorld;
import game.world.model.Player;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import test.world.util.SingleRoomWorldTest;

public class ServerClientWorldTests {

	@Test
	public void makePlayerTest() {
		ServerWorld serverWorld = new SingleRoomWorldTest().world;
		ClientWorld clientWorld = new SingleRoomWorldTest().world;

		String command = clientWorld.getSetClientPlayer(new Player(
				"FattyBoomBoom"));

		List<String> commands = serverWorld.applyCommand(command);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		Iterator<Player> players = clientWorld.getPlayers();
		boolean wasFound = false;
		while(players.hasNext()){
			if (players.next().getName().equals("FattyBoomBoom")){
				assertTrue(true);
				wasFound = true;
			}
		}
		if (!wasFound){
			fail("Not yet implemented");
		}
	}


	@Test
	public void movePlayerTest() {
		ServerWorld serverWorld = new SingleRoomWorldTest().world;
		ClientWorld clientWorld = new SingleRoomWorldTest().world;
		Player p = new Player("FattyBoomBoom");
		String command = clientWorld.getSetClientPlayer(p);

		List<String> commands = serverWorld.applyCommand(command);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		String cmd = clientWorld.getCommand("Up", 0);
		commands = serverWorld.applyCommand(cmd);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		Iterator<Player> players = clientWorld.getPlayers();
		boolean wasFound = false;
		while(players.hasNext()){
			Player pp = players.next();
			if (pp.getName().equals("FattyBoomBoom")){
				wasFound = true;
			}
		}
		if (!wasFound){
			fail("Not yet implemented");
		}
	}
}
