package game.ui.render.levelmaker;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Place;
import game.world.util.Drawable;

public class BackgroundObject implements Drawable{

	private static final int numImages = 20;

	private String imageName;
	private Point3D position;
	private Rectangle3D boundingBox;

	public BackgroundObject(Point3D position, Rectangle3D boundingBox){
		imageName = selectRandomImage();
		this.position = position;
		this.boundingBox = boundingBox;
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
