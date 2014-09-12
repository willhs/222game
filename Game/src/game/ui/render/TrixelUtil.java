package game.ui.render;

import game.world.dimensions.Point3D;
import game.world.dimensions.Trixel;
import game.world.dimensions.Trixition;

/**
 * @author hardwiwill
 * For helper methods for converting between trixitions and 3d points in the world.
 */
public class TrixelUtil {

	/**
	 * @param position
	 * @return the position in the trixel grid
	 */
	public static Trixition positionToTrixition(Point3D position){
		return new Trixition((int)Math.floor(position.getX() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE));
	}

	/**
	 * @param trixition (position in the trixel grid)
	 * @return the position in the 3d world of the ** top, left, far ** point of the trixition
	 */
	public static Point3D trixitionToPosition(Trixition trixition){
		return new Point3D(trixition.x * Trixel.SIZE,
				trixition.y * Trixel.SIZE,
				trixition.z * Trixel.SIZE);
	}
}
