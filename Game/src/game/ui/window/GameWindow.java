package game.ui.window;

import game.ui.render.GraphicsPanel;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
/**
 * @author Nicky van HUlst
 * */

public class GameWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private BlankPanel blankPanel;
	private GraphicsPanel graphicsPanel;


	private static final String title = "Game";

	private static final int WINDOW_SIZE = 90;
	public static final int FRAME_HEIGHT = WINDOW_SIZE*9;
	public static final int FRAME_WIDTH = WINDOW_SIZE*16;

	Map<String , Integer> keyMap;

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		this.graphicsPanel = new GraphicsPanel();
		this.blankPanel = new BlankPanel();

		//set up the globalKey listener
		setUpKeyListner();
		keyMap = setUpkeys();

		//set the size of the frame
		setResizable(false);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);

		add(blankPanel);


		blankPanel.repaint();
	}

	/**
	 * Sets up a global key listner
	 * */
	public void setUpKeyListner(){
		//get the keyboard manager
		KeyboardFocusManager manager  = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher( new MyKeyDispatcher());
	}



	//Custom dispatcher
	class MyKeyDispatcher implements KeyEventDispatcher {
	    public boolean dispatchKeyEvent(KeyEvent e) {
	    	System.out.println(e.getKeyChar());
	        if(e.getID() == KeyEvent.KEY_PRESSED){
	            for(String key : keyMap.keySet() ){
	            	if(keyMap.get(key) == e.getKeyCode()){
	            		System.out.println(key);
	            		blankPanel.keyPressed(key);
	            	}
	            }
	        }
	        return false;
	    }
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

	public static void main(String[] args){
		//GameWindow gWindow =
		new GameWindow();
		//new TestWindow(gWindow);
	}

}
