package game.world.util;

import game.world.dimensions.Point3D;

import java.awt.image.BufferedImage;

/**
 * @author hardwiwill
 * Contains an image at a point
 */
public class GameImage {

	private BufferedImage image;
	private Point3D point;

	public GameImage(BufferedImage image, Point3D point) {
		this.image = image;
		this.point = point;
	}
}
