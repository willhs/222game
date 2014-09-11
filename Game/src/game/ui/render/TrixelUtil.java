package game.ui.render;

import game.world.dimensions.Point3D;
import game.world.dimensions.Trixel;
import game.world.dimensions.Trixition;

/**
 * @author hardwiwill
 * For static helper methods for converting between trisitions and 3d points.
 */
public class TrixelUtil {

	/**
	 * @param position
	 * @return the position
	 */
	public static Trixition positionToTrisition(Point3D position){
		return new Trixition((int)Math.floor(position.getX() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE));
	}
}
