package game.ui.window;

import game.ui.render.temp.RotationTest;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.security.acl.LastOwnerException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
/**
 * @author Nicky van HUlst
 * */

public class GameWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private BlankPanel blankPanel;
	private RotationTest graphicsPanel;


	private static final String title = "Game";

	private static final int WINDOW_SIZE = 90;
	public static final int FRAME_HEIGHT = WINDOW_SIZE*9;
	public static final int FRAME_WIDTH = WINDOW_SIZE*16;

	public static HashMap<String , Integer> keyMap;

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		this.graphicsPanel = new RotationTest();
		this.blankPanel = new BlankPanel();

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


	public static void main(String[] args){
		new GameWindow();
	}
}
