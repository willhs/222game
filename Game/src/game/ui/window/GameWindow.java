package game.ui.window;

import game.ui.renderer.GraphicsPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class GameWindow extends JFrame{
	private MainMenu mainMenu;
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

		this.mainMenu = new MainMenu();
		add(mainMenu);


		mainMenu.repaint();
	}

	public static void main(String[] args){
		new GameWindow();
	}

}
