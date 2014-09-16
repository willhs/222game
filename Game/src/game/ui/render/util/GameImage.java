package game.ui.render.util;

import game.ui.render.ZComparable;
import game.world.dimensions.Point3D;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * @author hardwiwill
 * Contains an (2d) image at a point
 */
public class GameImage implements ZComparable{

	private BufferedImage image;
	private Point3D position;
	private Dimension dimension;

	public GameImage(BufferedImage image, Point3D point, Dimension dimension) {
		this.image = image;
		this.position = point;
		this.dimension = dimension;
	}

	@Override
	public float getZ() {
		return position.getZ();
	}
	
	public Dimension getDimension(){
		return dimension;
	}
	
	public Point3D getPoint(){
		return position;
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
