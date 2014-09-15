package game.world.dimensions;

public class Rectangle3D {
	private final float x;
	private final float y;
	private final float z;
	private final float width;
	private final float length;
	private final float height;

	public Rectangle3D(float width, float height, float length) {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.width = width;
		this.length = length;
		this.height = height;
	}

	private Rectangle3D(Rectangle3D rectangle3d, Point3D point) {
		this.x = rectangle3d.x + point.getX() - rectangle3d.width * 0.5f;
		this.y = rectangle3d.y + point.getY() - rectangle3d.height * 0.5f;
		this.z = rectangle3d.z + point.getZ() - rectangle3d.length * 0.5f;
		this.width = rectangle3d.width;
		this.length = rectangle3d.length;
		this.height = rectangle3d.height;
	}

	public boolean collisionDetection(Point3D thisPoint,
			Rectangle3D otherRectangle, Point3D otherPoint) {
		Rectangle3D thisRectangle = this.apply3Dpoint(thisPoint);
		otherRectangle = otherRectangle.apply3Dpoint(otherPoint);
		if (compareValues(thisRectangle.x, thisRectangle.width,
				otherRectangle.x, otherRectangle.width)
				&& compareValues(thisRectangle.y, thisRectangle.height,
						otherRectangle.y, otherRectangle.height)
				&& compareValues(thisRectangle.z, thisRectangle.length,
						otherRectangle.z, otherRectangle.length)) {
			return true;
		}
		if (compareValues(otherRectangle.x, otherRectangle.width,
				thisRectangle.x, thisRectangle.width)
				&& compareValues(otherRectangle.y, otherRectangle.height,
						thisRectangle.y, thisRectangle.height)
				&& compareValues(otherRectangle.z, otherRectangle.length,
						thisRectangle.z, thisRectangle.length)) {
			return true;
		}
		return false;
	}

	private Rectangle3D apply3Dpoint(Point3D point) {
		return new Rectangle3D(this, point);
	}

	private boolean compareValues(float point1, float modeifier1, float point2,
			float modeifier2) {
		if (point1 <= point2 && point1 + modeifier1 >= point2) {
			return true;
		}
		if (point1 <= point2 + modeifier2
				&& point1 + modeifier1 >= point2 + modeifier2) {
			return true;
		}
		return false;
	}

}
