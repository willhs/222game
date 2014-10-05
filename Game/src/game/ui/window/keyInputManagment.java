package game.ui.window;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * @author Nicky van Hulst
 * */
public class keyInputManagment implements KeyEventDispatcher{
	
	private BlankPanel panel;
	
	//map mapping strings to keys 
	private static HashMap<String , Integer> keyMap;
	
	//the last key that was pressed
	private static KeyEvent lastKeyEvent;
	
	//set the escape key
	private int escapeKey;


	/**
	 * The constructor for the keyManagement class
	 * */
	public keyInputManagment(BlankPanel panel){
		this.panel = panel;

		//assign keys to the map
		keyMap = setUpkeys();
		this.escapeKey = KeyEvent.VK_ESCAPE;//set the escape key here so the user cannot change it
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {

		//set the last key event
		lastKeyEvent = e;

		//if a key is pressed
		if(e.getID() == KeyEvent.KEY_PRESSED){
			if(e.getKeyCode() == escapeKey){
				panel.keyPressed("escape");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_UP){
				panel.keyPressed("up");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				panel.keyPressed("down");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				panel.keyPressed("enter");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				panel.keyPressed("backspace");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_1){
				panel.keyPressed("1");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_2){
				panel.keyPressed("2");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_3){
				panel.keyPressed("3");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_4){
				panel.keyPressed("4");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_5){
				panel.keyPressed("5");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_6){
				panel.keyPressed("6");
				return false;
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				panel.keyPressed("space");
				return false;
			}
			
			for(String key : keyMap.keySet() ){
				if(keyMap.get(key) == e.getKeyCode()){
					panel.keyPressed(key);//send the key press to the panel
					return false;
				}
			}
			panel.keyPressed("unbound key");//unknown key pressed
		}
		return false;
	}
	
	
	/**
	 * Sets up the key values corresponding to the actions
	 * */
	private HashMap<String,Integer> setUpkeys(){
		HashMap<String, Integer> tempKeyMap = new HashMap<String, Integer>();

		//place the keys into the map
		tempKeyMap.put("Up", KeyEvent.VK_W);//w
		tempKeyMap.put("Down", KeyEvent.VK_S);//s
		tempKeyMap.put("Right", KeyEvent.VK_D);//d
		tempKeyMap.put("Left", KeyEvent.VK_A);//a
		tempKeyMap.put("inventory", KeyEvent.VK_I);//i
		tempKeyMap.put("Interact", KeyEvent.VK_F);//f
		tempKeyMap.put("rotate right", KeyEvent.VK_RIGHT);//f
		tempKeyMap.put("rotate left", KeyEvent.VK_LEFT);//f

		return tempKeyMap;
	}

	
	/**
	 * Returns the keyMap
	 * */
	public static HashMap<String,Integer> getKeyMap(){return keyMap;}


	/**
	 * Returns the last key press from the user
	 * */
	public static KeyEvent getLastKeyEvent(){return lastKeyEvent;}

}
