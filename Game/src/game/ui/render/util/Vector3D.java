package game.ui.render.util;


/**
 * @author pondy & will. pondy wrote most of this class.
 * Represents a vector with 3d
 */
public class Vector3D implements Transformable {
	// I made a float version of this class so that vectors could be used in the
	// transform class and so it could
	// be faster for the 3d rendering

	private float x;
	private float y;
	private float z;
	private float mag;

	/**
	 * Construct a new vector, with the specified x, y, z components computes
	 * and caches the magnitude.
	 */
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.setZ(z);
		this.mag = (float) Math.sqrt(x * x + y * y + z * z);
	}

	/** A private constructor, used only within this class */
	private Vector3D(float x, float y, float z, float mag) {
		this.x = x;
		this.y = y;
		this.setZ(z);
		this.mag = mag;
	}

	/**
	 * Constructs and returns a unit vector in the same direction as this
	 * vector.
	 */

	public Vector3D unitVector() {
		if (getMag() <= 0.0)
			return new Vector3D(1.0f, 0.0f, 0.0f, 1.0f);
		else
			return new Vector3D(getX() / getMag(), getY() / getMag(), getZ()
					/ getMag(), 1.0f);
	}

	/** Returns the new vector that is this vector minus the other vector. */
	public Vector3D minus(Vector3D other) {
		return new Vector3D(getX() - other.getX(), getY() - other.getY(),
				getZ() - other.getZ());
	}

	/** Returns the new vector that is this vector plus the other vector. */
	public Vector3D plus(Vector3D other) {
		return new Vector3D(getX() + other.getX(), getY() + other.getY(),
				getZ() + other.getZ());
	}

	/**
	 * Returns the float that is the dot product of this vector and the other
	 * vector.
	 */
	public float dotProduct(Vector3D other) {
		return getX() * other.getX() + getY() * other.getY() + getZ()
				* other.getZ();
	}

	/**
	 * Returns the vector that is the cross product of this vector and the other
	 * vector. Note that the resulting vector is perpendicular to both this and
	 * the other vector.
	 */
	public Vector3D crossProduct(Vector3D other) {
		float x = this.getY() * other.getZ() - this.getZ() * other.getY();
		float y = this.getZ() * other.getX() - this.getX() * other.getZ();
		float z = this.getX() * other.getY() - this.getY() * other.getX();
		return new Vector3D(x, y, z);
	}

	/**
	 * Returns the cosine of the angle between this vector and the other vector.
	 */
	public float cosTheta(Vector3D other) {
		return (getX() * other.getX() + getY() * other.getY() + getZ()
				* other.getZ())
				/ getMag() / other.getMag();
	}

	public String toString() {
		StringBuilder ans = new StringBuilder("Vect:");
		ans.append('(').append(getX()).append(',').append(getY()).append(',')
				.append(getZ()).append(')');
		return ans.toString();
	}

	@Override
	public void transform(Transform transform) {
		float[][] values = transform.getValues();
		if (values == null || values[0] == null || values[1] == null
				|| values[2] == null) {
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

	public void setZ(float z) {
		this.z = z;
	}

	public float getMag() {
		return mag;
	}

}
