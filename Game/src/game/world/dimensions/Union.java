package game.world.dimensions;

import java.util.List;

/**
 * Union This handle the union of to rectangles.
 * 
 * @author Shane Brewer
 * 
 */
public class Union extends Rectangle implements Dimension {

	Dimension dimensionOne;
	Dimension dimensionTwo;

	public Union(Dimension dimensionOne, Dimension dimensionTwo) {
		super(Math.min(dimensionOne.getX(), dimensionTwo.getX()), Math.min(
				dimensionOne.getY(), dimensionTwo.getY()), Math.max(
				dimensionOne.getWidth(), dimensionTwo.getWidth()), Math.max(
				dimensionOne.getHeight(), dimensionTwo.getHeight()));

		this.dimensionOne = dimensionOne;
		this.dimensionTwo = dimensionTwo;
	}

	@Override
	public boolean contains(float x, float y) {
		return dimensionOne.contains(x, y) || dimensionTwo.contains(x, y);
	}

	@Override
	public Rectangle getBoundingBox() {
		return this;
	}

	public List<Point3D> getCorners() {
		List<Point3D> pointList = dimensionOne.getCorners();
		List<Point3D> toRemove = dimensionTwo.getCorners();
		for (int i = 0; i < toRemove.size(); i++) {
			if (pointList.contains(toRemove.get(i))) {
				pointList.remove(toRemove.get(i));
			}
		}

		for (float x = dimensionTwo.getX(); x < dimensionTwo.getX()
				+ dimensionTwo.getWidth(); x++) {
			if (dimensionOne.contains(x, dimensionTwo.getY())
					&& dimensionOne.contains(x - 1, dimensionTwo.getY())) {
				pointList.add(new Point3D(x, 0, dimensionTwo.getY()));
			}
			if (dimensionOne.contains(x, dimensionTwo.getY())
					&& dimensionOne.contains(x + 1, dimensionTwo.getY())) {
				pointList.add(new Point3D(x, 0, dimensionTwo.getY()));
			}
			if (dimensionOne.contains(x,
					dimensionTwo.getY() + dimensionTwo.getWidth())
					&& dimensionOne.contains(x - 1, dimensionTwo.getY()
							+ dimensionTwo.getWidth())) {
				pointList.add(new Point3D(x, 0, dimensionTwo.getY()
						+ dimensionTwo.getWidth()));
			}
			if (dimensionOne.contains(x,
					dimensionTwo.getY() + dimensionTwo.getWidth())
					&& dimensionOne.contains(x + 1, dimensionTwo.getY()
							+ dimensionTwo.getWidth())) {
				pointList.add(new Point3D(x, 0, dimensionTwo.getY()
						+ dimensionTwo.getWidth()));
			}
		}
		for (float y = dimensionTwo.getY(); y < dimensionTwo.getY()
				+ dimensionTwo.getHeight(); y++) {
			if (dimensionOne.contains(dimensionTwo.getX(), y)
					&& dimensionOne.contains(dimensionTwo.getX(), y - 1)) {
				pointList.add(new Point3D(dimensionTwo.getX(), 0, dimensionTwo
						.getY()));
			}
			if (dimensionOne.contains(dimensionTwo.getX(), y)
					&& dimensionOne.contains(dimensionTwo.getX(), y + 1)) {
				pointList.add(new Point3D(dimensionTwo.getX(), 0, y));
			}
			if (dimensionOne.contains(dimensionTwo.getX(), y)
					&& dimensionOne.contains(
							dimensionTwo.getX() + dimensionTwo.getHeight(),
							y - 1)) {
				pointList.add(new Point3D(dimensionTwo.getX(), 0, y));
			}
			if (dimensionOne.contains(dimensionTwo.getX(), y)
					&& dimensionOne.contains(
							dimensionTwo.getX() + dimensionTwo.getHeight(),
							y + 1)) {
				pointList.add(new Point3D(dimensionTwo.getX(), 0, y));
			}
		}
		return pointList;
	}

}
