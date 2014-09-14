package game.ui.render;

import game.ui.render.util.Transform;

/**
 * @author will
 * Able to be transformed with 3d vector transformations e.g. things with points, vectors
 */
public interface Transformable {

	/**
	 * @param transform
	 * Applies a 3d vector transformation
	 */
	public abstract void transform(Transform transform);
	
}
