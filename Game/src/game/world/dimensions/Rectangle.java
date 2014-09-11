package game.world.dimensions;

/**
 * Rectangle 
 * Used to define the dimensions of a room.
 * @author Shane Brewer
 *
 */
public class Rectangle implements Dimension{
	
	private final double x;
	private final double y;
	private final double width;
	private final double height;
	
	public Rectangle (double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean contains(double x, double y) {
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
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}
	
}
