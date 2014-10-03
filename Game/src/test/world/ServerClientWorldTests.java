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
	public void makePlayerTest() {
		ServerWorld serverWorld = new SingleRoomWorldTest().world;
		ClientWorld clientWorld = new SingleRoomWorldTest().world;

		String command = clientWorld.getSetClientPlayer(new Player(
				"FattyBoomBoom"));

		List<String> commands = serverWorld.applyCommand(command);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		clientWorld.getCommand("Up");
		clientWorld.getCommand("Down");
		clientWorld.getCommand("Left");
		clientWorld.getCommand("Right");
		Iterator<Player> players = clientWorld.getPlayers();
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
		String cmd = clientWorld.getCommand("Up");
		System.out.println(p.getPosition());
		commands = serverWorld.applyCommand(cmd);
		for (String c: commands){
			clientWorld.applyCommand(c);
		}
		System.out.println(p.getPosition());
		Iterator<Player> players = clientWorld.getPlayers();
		boolean um = false;
		while(players.hasNext()){
			Player pp = players.next();
			if (pp.getName().equals("FattyBoomBoom")){
				um = true;
			}
		}
		if (!um){
			fail("Not yet implemented");
		}
	}
}
