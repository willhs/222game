package game.ui.render.util;

import game.ui.render.ZComparable;
import game.world.dimensions.Point3D;

import java.awt.image.BufferedImage;

/**
 * @author hardwiwill
 * Contains an (2d) image at a point
 */
public class GameImage implements ZComparable{

	private BufferedImage image;
	private Point3D point;

	public GameImage(BufferedImage image, Point3D point) {
		this.image = image;
		this.point = point;
	}

	@Override
	public float getZ() {
		return point.getZ();
	}
}
