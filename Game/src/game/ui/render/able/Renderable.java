package game.ui.render.able;

import game.ui.render.util.DepthComparable;


/**
 * Ready for rendering.
 *
 * @author hardwiwill
 */
public interface Renderable extends DepthComparable{

	/**
	 * flips the shape
	 */
	public void flipAroundY(int top);
}
