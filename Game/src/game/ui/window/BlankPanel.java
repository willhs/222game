package game.ui.window;

import game.ui.window.menus.Animated;
import game.ui.window.menus.MainMenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *@author Nicky van Hulst 300294657
 * */
public class BlankPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	//the menu the panel is rendering at the moment
	private GraphicsPane currentMenu;

	//the main frame
	private JFrame frame;

	/**
	 * Constructor for the blank panel sets up the panel
	 * with the current menu being the main menu
	 * */
	public BlankPanel(JFrame frame){

		//set the frame
		this.frame = frame;

		//set the size of the panel
		setSize(new Dimension(GameWindow.FRAME_WIDTH,GameWindow.FRAME_HEIGHT));

		//background color
		setBackground(Color.black);

		//set up the mouse listener
		setUpMouseListner();

		//set the main menu to be the current menu
		currentMenu = new MainMenu(this);
	}


	/**
	 *Repaints the graphics of the panel
	 * */
	public void paint(Graphics g){
		if(currentMenu instanceof Animated){
			if(((Animated) currentMenu).isAnimating())((Animated) currentMenu).animate();
		}

		//make the current menu draw itself on the graphics object
		currentMenu.render(g);
	}


	/**
	 *handles a key being pressed on the keyboard
	 *passes the key pressed on to the current menu
	 * */
	public void keyPressed(String keyPressed){
		currentMenu.keyPressed(keyPressed);
	}


	/**
	 * Sets up the mouse listener for the panel
	 * */
	private void setUpMouseListner(){
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e){
				currentMenu.handleMouseMoved(e);//passes info on to current menu
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e){
				currentMenu.handleMouseReleased(e);//passes info on to current menu
			}

			@Override
			public void mousePressed(MouseEvent e){
				currentMenu.handleMousePressed(e);//passes info on to current menu
			}
		});
	}

	@Override
	public void setPreferredSize(Dimension preferredSize){
		super.setPreferredSize(preferredSize);
		repaint();
		frame.setSize(preferredSize);
		
		//frame.pack();
	}


	/**
	 * Sets the current menu
	 * */
	public void setMenu(GraphicsPane menu){
		this.currentMenu = menu;
	}
}
