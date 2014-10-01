package game.ui.window;

import game.ui.render.Renderer;
import game.world.model.Player;
import game.world.model.Room;

import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;

import test.render.RotationTest;
/**
 * @author Nicky van HUlst
 * */

public class GameWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private BlankPanel blankPanel;
	private RotationTest graphicsPanel;
	private static Queue<String> keyCodeQueue;

	private static final String title = "Game";

	private static final int WINDOW_SIZE = 90;
	public static final int FRAME_HEIGHT = WINDOW_SIZE*9;
	public static final int FRAME_WIDTH = WINDOW_SIZE*16;

	public static HashMap<String , Integer> keyMap;

	private Room currentRoom;
	private Player player;//TODO make this work

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		this.blankPanel = new BlankPanel();
		this.keyCodeQueue = new LinkedList<String>();
		//set up the globalKey listener
		setUpKeyListner();

		//set the size of the frame
		setResizable(false);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);

		//adds the main panel where everythin will be drawn on
		add(blankPanel);

		//repaint the main panel
		blankPanel.repaint();
	}


	/**
	 * Sets up a global key listener
	 * */
	public void setUpKeyListner(){
		//get the keyboard manager
		KeyboardFocusManager manager  = KeyboardFocusManager.getCurrentKeyboardFocusManager();

		//add my own custom event dispatcher
		manager.addKeyEventDispatcher( new keyInputManagment(blankPanel));
	}

	public static Queue<String> getKeyQueue(){
		return keyCodeQueue;
	}

	public void setRoom(Room currentRoom){
		this.currentRoom = currentRoom;
	}

	public static void main(String[] args){
		Renderer r = new Renderer();
		GameWindow window = new GameWindow();
		TestThread t = new TestThread(20, window);
		t.start();
	}

	public Player getPlayer(){
		return this.player;
	}
	
	public Room getRoom(){
		return null;
	}
}
