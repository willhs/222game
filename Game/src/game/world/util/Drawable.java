
package game.world.util;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Place;

/**
 * @author hardwiwill and Shane Brewer 300289850
 *
 */
public interface Drawable {
	public String getName();
	public Point3D getPosition();
	public Rectangle3D getBoundingBox();
	public String getImageName();
	public Point3D getPosition(Place place);
}
