package game.world.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Trixel;

import java.util.List;

/**
 * @author hardwiwill
 *
 */
public interface Drawable3D extends Drawable{

	public List<Trixel> getTrixels();

}
