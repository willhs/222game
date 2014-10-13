package game.ui.render.texture;

import java.awt.image.BufferedImage;

import game.ui.render.Res;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Place;
import game.world.util.Drawable;

/**
 * @author hardwiwill
 *
 * Represents image of a vine
 */
public class Vine implements Drawable{

	private Point3D position;
	private Rectangle3D box;
	private String imageName;
	private final String name = "alien_vine";

	public Vine(Point3D position, float height){
		this.position = position;
		this.imageName = generateRandomVineImageName();
		this.box = generateBoxGivenHeight(height);
	}

	/**
	 * Computes bounding box for the image scaled by height.
	 * @param newHeight
	 * @return
	 */
	private Rectangle3D generateBoxGivenHeight(float newHeight) {
		BufferedImage image = Res.getImageFromName(imageName);
		float scalar = newHeight / image.getHeight();
		float newWidth = scalar * image.getWidth();
		return new Rectangle3D(newWidth, newHeight, 1);
	}

	private String generateRandomVineImageName(){
		int numOfImages = 11;
		return name + "_" + (int)(Math.random()*numOfImages);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return box;
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	@Override
	public Point3D getPosition(Place place) {
		return position;
	}

}
