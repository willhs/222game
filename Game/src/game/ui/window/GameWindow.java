package game.ui.window;

import game.ui.render.ImageStorage;
import game.world.model.Room;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.util.HashMap;

import javax.swing.JFrame;
/**
 * @author Nicky van HUlst
 * */
public class GameWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	//panel to that is drawn on
	private BlankPanel blankPanel;

	private static final String title = "Game";

	//size fields
	private static final int WINDOW_SIZE = 90;
	public static final int FRAME_HEIGHT = WINDOW_SIZE*9;
	public static final int FRAME_WIDTH = WINDOW_SIZE*16;

	//the map containing the key combinations
	public static HashMap<String , Integer> keyMap;

	public static Room currentRoom;
	public static Thread thread;

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		this.blankPanel = new BlankPanel();

		//set up the globalKey listener
		setUpKeyListner();

		//set the size of the frame
		setResizable(false);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//adds the main panel where everything will be drawn on
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


	/**
	 * Sets the room
	 * */
	public static void setRoom(Room currentRoom){
		GameWindow.currentRoom = currentRoom;
	}


	public static void main(String[] args){
<<<<<<< HEAD
		//read in the resources
		Res.readInAllCommonImages();
=======
		//read in the resources 
		ImageStorage.readInAllCommonImages();
>>>>>>> 818c36ab38f13713776e341fbc8131422b1fc602
		GameWindow window = new GameWindow();
		thread = new TestThread(30, window);
		thread.start();//start the thread
	}
}
