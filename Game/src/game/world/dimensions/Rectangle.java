package game.world.dimensions;

import java.util.*;

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

	@Override
	public List<Point3D> getCorners() {
		List<Point3D> pointList = new ArrayList<Point3D>();
		pointList.add(new Point3D(x,0,y));
		pointList.add(new Point3D(x+width,0,y));
		pointList.add(new Point3D(x,0,y+height));
		pointList.add(new Point3D(x+width,0,y+height));
		return pointList;
	}
	
}
