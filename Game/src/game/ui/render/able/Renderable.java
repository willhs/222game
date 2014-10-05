package game.ui.render.able;

import game.ui.render.util.ZComparable;


/**
 * Ready for rendering
 *
 * @author hardwiwill
 */
public interface Renderable extends ZComparable{

	/**
	 * flips the shape
	 */
	public void flipAroundY(int top);
}
