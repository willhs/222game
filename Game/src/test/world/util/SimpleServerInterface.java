package test.world.util;

import java.util.List;

import game.world.model.ClientWorld;
import game.world.model.Player;
import game.world.model.ServerWorld;

public class SimpleServerInterface {
	ServerWorld serverW;
	ClientWorld serverC;

	public SimpleServerInterface(){
		serverC = new SingleRoomWorldTest().world;
		serverW = serverC;
		Player p = new Player("FattyBoomBoom");
		String command = serverC.getSetClientPlayer(p);

		List<String> commands = serverW.applyCommand(command);
		for (String c: commands){
			serverC.applyCommand(c);
		}
	}

	public void MakeMove(String move){
		String cmd = serverC.getCommand(move);
		List<String> commands = serverW.applyCommand(cmd);
		for (String s:commands){
			serverC.applyCommand(s);
		}
	}
}
