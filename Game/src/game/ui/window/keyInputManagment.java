package game.ui.window;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class keyInputManagment  implements KeyEventDispatcher{
	private BlankPanel panel;
	private static HashMap<String , Integer> keyMap;


	private static KeyEvent lastKeyEvent;

	public keyInputManagment(BlankPanel panel){
		super();

		this.panel = panel;

		//assign keys to the map
		keyMap = setUpkeys();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		System.out.println(e.getKeyChar());
		lastKeyEvent = e;
		if(e.getID() == KeyEvent.KEY_PRESSED){
			for(String key : keyMap.keySet() ){
				if(keyMap.get(key) == e.getKeyCode()){
					System.out.println(key);
					panel.keyPressed(key);
					return false;
				}
			}
			panel.keyPressed("unbound key");
		}
		return false;

	}

	/**
	 * Sets up the key values corrosponding to the actions
	 * */
	private HashMap<String,Integer> setUpkeys(){
		HashMap<String, Integer> tempKeyMap = new HashMap<String, Integer>();

		tempKeyMap.put("escape", KeyEvent.VK_ESCAPE);//esc
		tempKeyMap.put("move up", KeyEvent.VK_W);//w
		tempKeyMap.put("move down", KeyEvent.VK_S);//s
		tempKeyMap.put("move right", KeyEvent.VK_D);//d
		tempKeyMap.put("move left", KeyEvent.VK_A);//a
		tempKeyMap.put("inventory", KeyEvent.VK_I);//i
		tempKeyMap.put("interact", KeyEvent.VK_F);//f

		return tempKeyMap;
	}

	public static HashMap<String,Integer> getKeyMap(){
		return keyMap;
	}


	/**
	 * Returns the last key press from the user
	 * */
	public static KeyEvent getLastKeyEvent(){
		return lastKeyEvent;
	}


}
