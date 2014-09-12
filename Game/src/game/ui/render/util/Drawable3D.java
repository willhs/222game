package game.ui.render.util;

import game.world.dimensions.Trixel;
import game.world.util.Drawable;

import java.util.List;

/**
 * @author hardwiwill
 *
 */
public interface Drawable3D extends Drawable{

	public List<Trixel> getTrixels();

}
