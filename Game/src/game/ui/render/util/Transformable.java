package game.ui.render.util;


/**
 * @author will
 * Able to be transformed with 3d vector transformations.
 */
public interface Transformable {

	/**
	 * Transforms this object
	 * @param transform
	 */
	public abstract void transform(Transform transform);

}
