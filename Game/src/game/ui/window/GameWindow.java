package game.ui.window;

import game.ui.render.Res;
import game.world.model.Room;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.util.HashMap;

import javax.swing.JFrame;
/**
 * @author Nicky van Hulst 300295657
 * */
public class GameWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	//panel to that is drawn on
	private BlankPanel blankPanel;

	private static final String title = "Game";

	//size fields
	private static final int WINDOW_SIZE = 85;
	public static int FRAME_HEIGHT = 9*WINDOW_SIZE;//WINDOW_SIZE*9;
	public static int FRAME_WIDTH = 16*WINDOW_SIZE;//WINDOW_SIZE*16;

	//768
	//1400

	//the map containing the key combinations
	public static HashMap<String , Integer> keyMap;

	//the players current room
	public static Room currentRoom;


	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		System.out.println("Width = " + FRAME_WIDTH);
		System.out.println("Height = " + FRAME_HEIGHT);


		this.blankPanel = new BlankPanel();

		//set up the globalKey listener
		setUpKeyListner();

		//set the size of the frame
		setResizable(true);
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

	public static void setWidth(int width){
		FRAME_WIDTH = width;
	}

	public static void setHeight(int height){
		FRAME_HEIGHT = height;
	}

	/**
	 * Starts the game
	 * */
	public static void main(String[] args){

		//read in the resources
		Res.readInAllCommonImages();
		GameWindow window = new GameWindow();
		Thread thread = new WindowThread(20, window);
		thread.start();//start the thread
	}
}
