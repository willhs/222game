package test.world.util;

import java.util.List;

import game.world.logic.ClientWorld;
import game.world.logic.ServerWorld;
import game.world.model.Player;

public class SimpleServerInterface {
	public final ServerWorld serverW;
	public final ClientWorld serverC;

	public SimpleServerInterface(){
		serverC = new SingleRoomWorldTest().world;
		serverW = serverC;
		Player p = new Player("FattyBoomBoom");

		System.out.println(p.getPosition());
		String command = serverC.getSetClientPlayer(p);

		System.out.println();
		System.out.println(command);
		List<String> commands = serverW.applyCommand(command);
		System.out.println(commands);
		for (String c: commands){
			System.out.println(serverC.applyCommand(c));
		}
		System.out.println(p.getPosition());
	}

	public void MakeMove(String move){
		String cmd = serverC.getCommand(move, 0);
		List<String> commands = serverW.applyCommand(cmd);
		for (String s:commands){
			serverC.applyCommand(s);
		}
	}

	public static void main (String[] ard){
		SimpleServerInterface l = new SimpleServerInterface();
	}
}
