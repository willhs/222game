package game.ui.render.levelmaker;

import game.ui.render.ImageStorage;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Place;
import game.world.util.Drawable;

import java.awt.image.BufferedImage;

public class BackgroundObject implements Drawable{

	private static final int numImages = 20;

	private String imageName;
	private Point3D position;
	private Rectangle3D boundingBox;

	public BackgroundObject(Point3D position, int size){
		imageName = selectRandomImage();
		this.position = position;
		BufferedImage image = ImageStorage.getImageFromName(imageName);
		float scale = size/Math.max(image.getWidth(), image.getHeight());
		this.boundingBox = new Rectangle3D(image.getWidth()*scale, image.getHeight()*scale, 1);
	}

	/**
	 * @return an random background of
	 */
	private String selectRandomImage() {
		return "background_" + (int)(Math.random()*numImages);
	}

	@Override
	public String getName() {
		return "backgroundObject";
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
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
