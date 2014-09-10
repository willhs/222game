package game.ui.window;

/**
 * @author Nicky van Hulst
 * */
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public interface Menu{

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
}
