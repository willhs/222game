package game.world.util;

import game.world.dimensions.*;

/**
 * @author hardwiwill & Shane Brewer
 *
 */
public interface Drawable {
	public String getName();
	public Point3D getPosition();
	public Rectangle3D getBoundingBox();
}
