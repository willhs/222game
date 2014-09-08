package game.ui.window;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameWindow extends JFrame{

	private static final String title = "Game";

	private static final int WINDOW_SIZE = 100;
	private static final int FRAME_HEIGHT = WINDOW_SIZE*9;
	private static final int FRAME_WIDTH = WINDOW_SIZE*16;

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		//set the size of the frame
		setResizable(false);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);

	}

}
