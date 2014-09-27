
package game.world.dimensions;

/**
 * Used for defining bounding boxes for items and characters.
 * @author Shane Brewer
 *
 */
public class Rectangle3D {
	private final float x;
	private final float y;
	private final float z;
	private final float width;
	private final float length;
	private final float height;

	/**
	 * Constructer for the Rectangle3D class
	 * Only takes width height and length.
	 * x y and z are set to zero.
	 * @param width - the width of the rectangle.
	 * @param height - height of the rectangle.
	 * @param length - the length of the rectangle.
	 */
	public Rectangle3D(float width, float height, float length) {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.width = width;
		this.length = length;
		this.height = height;
	}

	/**
	 * Private constrcuter to make rectangles form other rectangles.
	 * @param rectangle3d - takes a rectangle and a point
	 * @param point - point 3d that is used to give the rectangle its x z and y.
	 */
	private Rectangle3D(Rectangle3D rectangle3d, Point3D point) {
		this.x = rectangle3d.x + point.getX() - rectangle3d.width * 0.5f;
		this.y = rectangle3d.y + point.getY() - rectangle3d.height * 0.5f;
		this.z = rectangle3d.z + point.getZ() - rectangle3d.length * 0.5f;
		this.width = rectangle3d.width;
		this.length = rectangle3d.length;
		this.height = rectangle3d.height;
	}

	/**
	 * 3D colistion detection becasue aprently that was needed.
	 * @param thisPoint - takes a point for this rectangle.
	 * @param otherRectangle - takes the other rectangle.
	 * @param otherPoint - takes the point for the other rectangle.
	 * @return - should only return true if the "things" touch.
	 */
	public boolean collisionDetection(Point3D thisPoint,
			Rectangle3D otherRectangle, Point3D otherPoint) {
		Rectangle3D thisRectangle = this.apply3Dpoint(thisPoint);
		otherRectangle = otherRectangle.apply3Dpoint(otherPoint);
		// checks if rectangle1's x y z values are within the other x y z values.
		if (compareValues(thisRectangle.x, thisRectangle.width,
				otherRectangle.x, otherRectangle.width)
				&& compareValues(thisRectangle.y, thisRectangle.height,
						otherRectangle.y, otherRectangle.height)
				&& compareValues(thisRectangle.z, thisRectangle.length,
						otherRectangle.z, otherRectangle.length)) {
			return true;
		}
		// checks if rectangle2's x y z values are within the other x y z values.
		if (compareValues(otherRectangle.x, otherRectangle.width,
				thisRectangle.x, thisRectangle.width)
				&& compareValues(otherRectangle.y, otherRectangle.height,
						thisRectangle.y, thisRectangle.height)
				&& compareValues(otherRectangle.z, otherRectangle.length,
						thisRectangle.z, thisRectangle.length)) {
			return true;
		}
		// if they are not the return false.
		return false;
	}

	/**
	 * Applys a point to a rectangle and returns a new rectangle.
	 * @param point - point to apply.
	 * @return - returns a new rectangle.
	 */
	public Rectangle3D apply3Dpoint(Point3D point) {
		return new Rectangle3D(this, point);
	}

	/**
	 * Private methods for comparing values so the if statments dident get to big.
	 * @param point1 - float to be comapered
	 * @param modeifier1 - the modifyer can be 0
	 * @param point2 - other float to be comapred
	 * @param modeifier2 - used to modify point 2
	 * @return - returns true if the point2 is withing point1 and its modifyer.
	 */
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

	//======================Getters================================//
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getWidth() {
		return width;
	}

	public float getLength() {
		return length;
	}

	public float getHeight() {
		return height;
	}
	//====================Getters End================================//
}
