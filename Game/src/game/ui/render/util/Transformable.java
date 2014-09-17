package game.ui.render.util;


/**
 * @author will
 * Able to be transformed with 3d vector transformations e.g. things with points, vectors
 */
public interface Transformable {

	/**
	 * Applies a 3d vector transformation
	 * @param transform
	 */
	public abstract void transform(Transform transform);

}
