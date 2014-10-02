package game.world.dimensions;



/**
 * @author pondy & will. pondy wrote most of this class.
 * Represents NOW IMMUTABLE a vector with 3d
 */
public class Vector3D{
	// I made a float version of this class so that vectors could be used in the
	// transform class and so it could
	// be faster for the 3d rendering

	public final float x;
	public final float y;
	public final float z;
	public final float mag;

	/**
	 * Construct a new vector, with the specified x, y, z components computes
	 * and caches the magnitude.
	 */
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mag = (float) Math.sqrt(x * x + y * y + z * z);
	}

	/** A private constructor, used only within this class */
	private Vector3D(float x, float y, float z, float mag) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mag = mag;
	}

	/**
	 * makes a vector using the components from a point
	 * @param point
	 */
	public Vector3D(Point3D point){
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
		this.mag = (float) Math.sqrt(x * x + y * y + z * z);
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

	public float xAngle(Vector3D other){
		return (float)Math.acos((getY() * other.getY() + getZ()	* other.getZ())
				/ (getMag() * other.getMag()));
	}
	public float yAngle(Vector3D other){
		return (float)Math.acos((getX() * other.getX() + getZ()	* other.getZ())
				/ (getMag() * other.getMag()));
	}

	/**
	 * Make a scaled version of this vector
	 * @param scale
	 * @return a new vector which is a scaled version of this vector
	 */
	public Vector3D makeScaled(float scale){
		return new Vector3D(x*scale, y*scale, z*scale);
	}

	public String toString() {
		StringBuilder ans = new StringBuilder("Vect:");
		ans.append('(').append(String.format("%4.2f", getX())).append(',').append(String.format("%4.2f", getY())).append(',')
				.append(String.format("%4.2f", getZ())).append(')');
		return ans.toString();
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

	public float getMag() {
		return mag;
	}

}
