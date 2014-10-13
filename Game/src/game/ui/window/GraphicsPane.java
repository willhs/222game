package game.ui.window;


import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * @author Nicky van Hulst 300294657
 * */
public interface GraphicsPane{

	/**
	 * Draws all of the components of the menu onto the graphics object passed in
	 * */
	public void render(Graphics g);

	/**
	 * This method is called on the menu when a mouse is moved
	 * */
	public void handleMouseMoved(MouseEvent e);

	/**
	 * This method is called on the menu object when the mouse click is released
	 * */
	public void handleMouseReleased(MouseEvent e);

	/**
	 * Handles a key being pressed
	 * */
	public void keyPressed(String keyEvent);


	/**
	 * Handles the mouse being pressed
	 * */
	public void handleMousePressed(MouseEvent e);

}
