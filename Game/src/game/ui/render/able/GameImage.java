package game.ui.render.able;

import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.awt.image.BufferedImage;

/**
 * @author hardwiwill	300285801
 *
 * An image at a point
 */
public class GameImage implements Renderable, Transformable{

	private BufferedImage image;
	private Point3D position;
	private Rectangle3D boundingBox;

	public GameImage(BufferedImage image, Point3D point, Rectangle3D boundingBox) {
		this.image = image;
		this.position = point;
		this.boundingBox= boundingBox;
	}

	@Override
	public float getDepth() { // TODO: make this be relative to the position instead of the length:
		return position.getZ() + boundingBox.length/4;
	}

	public Rectangle3D getBoundingBox(){
		return boundingBox;
	}

	public Point3D getPosition(){
		return position;
	}

	public BufferedImage getImage(){
		return image;
	}

	@Override
	public void transform(Transform transform) {
		position = transform.multiply(position);
	}

	@Override
	public void flipAroundY(int top) {
		position = position.flipY(top);
	}
}
