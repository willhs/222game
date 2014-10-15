package game.ui.render.able;

import game.ui.render.util.DepthComparable;


/**
 * Ready for rendering.
 *
 * @author hardwiwill	300285801
 */
public interface Renderable extends DepthComparable{

	/**
	 * flips the shape
	 */
	public void flipAroundY(int top);
}
