package game.ui.window.menus;

/**
 * @author Nicky van Hulst 300294657
 * */
public interface Animated {

	/**
	 * Returns whether or not the menu is currently animating
	 * */
	public boolean isAnimating();


	/**
	 * Starts the animations of the menu
	 * */
	public void animate();


	/**
	 * Sets the type of animation true if the menu is coming into the screen
	 * which means it will be the new current menu and false if the menu is leaving
	 * the screen.
	 * */
	public void setAnimating(boolean in);
}
