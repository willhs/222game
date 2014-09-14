package game.world.dimensions;

import game.ui.render.Transformable;
import game.ui.render.util.Transform;

/**
 * @author pondy & will
 * A representation of a 3d point
 */
public class Point3D implements Transformable {
	// ---- PONDY'S COMMENTS:
	// I made a float version of this class so that vectors could be used in the
	// transform class and so it could
	// be faster for the 3d rendering

	private float x;
	private float y;
	private float z;

	/**
	 * Construct a new vector, with the specified x, y, z components computes
	 * and caches the magnitude.
	 */
	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}


	/** Returns the distance to another point. */
	public Point3D distanceTo(Point3D other) {
		return new Point3D(getX() - other.getX(), getY() - other.getY(), getZ()
				- other.getZ());
	}

	public String toString() {
		StringBuilder ans = new StringBuilder("Point:");
		ans.append('(').append(getX()).append(',').append(getY()).append(',')
				.append(getZ()).append(')');
		return ans.toString();
	}

	
	@Override
	public void transform(Transform transform) {
		float[][] values = transform.getValues();
		if (values == null || values[0] == null || values[1] == null || values[2] == null) {
			throw new IllegalStateException("Ill-formed transform");
		}
		float newX = values[0][3];
		float newY = values[1][3];
		float newZ = values[2][3];
		newX += values[0][0] * this.x + values[0][1] * this.y + values[0][2] * this.z;
		newY += values[1][0] * this.x + values[1][1] * this.y + values[1][2] * this.z;
		newZ += values[2][0] * this.x + values[2][1] * this.y + values[2][2] * this.z;
		
		x = newX;
		y = newY;
		z = newZ;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point3D other = (Point3D) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

}
