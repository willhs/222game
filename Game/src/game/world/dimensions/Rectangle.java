package game.world.dimensions;

/**
 * Rectangle 
 * Used to define the dimensions of a room.
 * @author Shane Brewer
 *
 */
public class Rectangle implements Dimension{
	
	private final float x;
	private final float y;
	private final float width;
	private final float height;
	
	public Rectangle (float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean contains(float x, float y) {
		if (x < this.x || y < this.y || x > this.width || y > this.height){
			return false;
		}
		return true;
	}

	@Override
	public Rectangle getBoundingBox() {
		return this;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}
	
}
