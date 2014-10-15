package test.world;

import static org.junit.Assert.fail;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import org.junit.Test;

/**
 *
 * @author Shane Brewer 300289850
 *
 */
public class CollisionDetectionTests {

	@Test
	public void collistionHappensTest1() {
		Rectangle3D one = new Rectangle3D(20,20,20);
		Rectangle3D two = new Rectangle3D(20, 20, 20);
		Point3D pointOne = new Point3D(0, 0, 0);
		if (!one.collisionDetection(pointOne, two, pointOne)){
			fail("They should collied");
		}
	}

	@Test
	public void collistionHappensTest2() {
		Rectangle3D one = new Rectangle3D(20,20,20);
		Rectangle3D two = new Rectangle3D(20, 20, 20);
		Point3D pointOne = new Point3D(0, 0, 0);
		Point3D pointTwo = new Point3D(20, 20, 20);
		if (!one.collisionDetection(pointOne, two, pointOne)){
			fail("They should collied the tip of the box is inside the other box.");
		}
	}

	@Test
	public void collistionDosentHappenTest1() {
		Rectangle3D one = new Rectangle3D(20,20,20);
		Rectangle3D two = new Rectangle3D(20, 20, 20);
		Point3D pointOne = new Point3D(0, 0, 0);
		Point3D pointTwo = new Point3D(30, 30, 30);
		if (one.collisionDetection(pointOne, two, pointTwo)){
			fail("They should not have collided");
		}
	}

	@Test
	public void collistionDosentHappenTest2() {
		Rectangle3D one = new Rectangle3D(20,20,20);
		Rectangle3D two = new Rectangle3D(20, 20, 20);
		Point3D pointOne = new Point3D(0, 0, 0);
		Point3D pointTwo = new Point3D(21, 0, 20);
		if (one.collisionDetection(pointOne, two, pointTwo)){
			fail("They should not have collided");
		}
	}
}
