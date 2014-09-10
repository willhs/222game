package game.ui.window;

import game.ui.render.GraphicsPanel;

import java.awt.Dimension;

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

	/**
	 * Constructor for the GameFrame
	 * */
	public GameWindow(){
		super(title);

		this.graphicsPanel = new GraphicsPanel();


		//set the size of the frame
		setResizable(false);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);

		this.blankPanel = new BlankPanel();
		add(blankPanel);


		blankPanel.repaint();
	}

	public static void main(String[] args){
		new GameWindow();
	}

}
