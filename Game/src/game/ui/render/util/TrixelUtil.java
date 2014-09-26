package game.ui.render.util;

import game.ui.render.Renderer;
import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hardwiwill
 * For helper methods involving trixels,
 * especially for converting between trixitions and 3d points in the world.
 */
public class TrixelUtil {

	/**
	 * @param position
	 * @return the position in the trixel grid
	 */
	public static Trixition positionToTrixition(Point3D position){
		return new Trixition((int)(position.getX() / Trixel.SIZE),
				(int)(position.getY() / Trixel.SIZE),
				(int)(position.getZ() / Trixel.SIZE));
	}

	/**
	 * @param trixition (position in the trixel grid)
	 * @return the position in the 3d world of the ** top, left, far ** point of the trixition
	 */
	public static Point3D trixitionToPosition(Trixition trixition){
		return new Point3D(trixition.x * Trixel.SIZE,
				trixition.y * Trixel.SIZE,
				trixition.z * Trixel.SIZE);
	}

	/**
	 * Gets the faces of a trixel
	 * TODO: reuse code more
	 * POST: 6 TrixelFaces
	 * POST: Faces ordered: left, right, bottom, top, back, front (currently only for testing purposes).
	 * @return 6 faces of a trixel
	 */
	public static TrixelFace[] getTrixelFaces(Trixel trixel){
		TrixelFace[] faces = new TrixelFace[6];
		Color colour = trixel.getColor();
		final int FACE_SIZE = Trixel.SIZE;

		Point3D trixelOrigin = TrixelUtil.trixitionToPosition(trixel.getTrixition());

		// left face
		// corner = corner of current face
		Point3D corner = trixelOrigin;
		faces[0] = getXFace(corner, 0, colour);

		// right face
		corner = new Point3D(trixelOrigin.getX()+FACE_SIZE, trixelOrigin.getY(), trixelOrigin.getZ());
		faces[1] = getXFace(corner, 1, colour);

		// bottom face
		corner = trixelOrigin;
		faces[2] = getYFace(corner, 0, colour);

		// top face
		corner = new Point3D(trixelOrigin.getX(),trixelOrigin.getY()+FACE_SIZE,trixelOrigin.getZ());
		faces[3] = getYFace(corner, 1, colour);

		// back face
		corner = trixelOrigin;
		faces[4] = getZFace(corner, 0, colour);

		// front face
		corner = new Point3D(trixelOrigin.getX(),trixelOrigin.getY(),trixelOrigin.getZ()+FACE_SIZE);
		faces[5] = getZFace(corner, 1, colour);

		return faces;
	}

	// -------------- HELPER METHODS ----------------------
	/**
	 * @param c : far bottom corner of face
	 * @param colour: colour of face
	 * @return a trixel face in which the x value of the vertices is constant
	 */
	private static TrixelFace getXFace(Point3D c, int clockwise, Color colour){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX(), c.getY()+Trixel.SIZE*(1-clockwise), c.getZ()+Trixel.SIZE*clockwise);
		vertices[2] = new Point3D(c.getX(), c.getY()+Trixel.SIZE, c.getZ()+Trixel.SIZE);
		vertices[3] = new Point3D(c.getX(), c.getY()+Trixel.SIZE*clockwise, c.getZ()+Trixel.SIZE*(1-clockwise));

		return new TrixelFace(vertices, Renderer.getRandomColour());
	}

	/**
	 * @param c : far left corner of face
	 * @param colour: colour of face
	 * @return a trixel face in which the y value of the vertices is constant
	 */
	private static TrixelFace getYFace(Point3D c, int clockwise, Color colour){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX()+Trixel.SIZE*(clockwise), c.getY(), c.getZ()+Trixel.SIZE*(1-clockwise));
		vertices[2] = new Point3D(c.getX()+Trixel.SIZE, c.getY(), c.getZ()+Trixel.SIZE);
		vertices[3] = new Point3D(c.getX()+Trixel.SIZE*(1-clockwise), c.getY(), c.getZ()+Trixel.SIZE*(clockwise));

		return new TrixelFace(vertices, Renderer.getRandomColour());
	}

	/**
	 * @param c : bottom left corner of face
	 * @param colour: colour of face
	 * @param clockwise: 1 = make points in clockwise, 0 = make points in anti-clockwise
	 * @return a trixel face in which the z value of the vertices is constant
	 */
	private static TrixelFace getZFace(Point3D c, int clockwise, Color colour){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX()+(Trixel.SIZE*(1-clockwise)), c.getY()+(Trixel.SIZE*clockwise), c.getZ());
		vertices[2] = new Point3D(c.getX()+Trixel.SIZE, c.getY()+Trixel.SIZE, c.getZ());
		vertices[3] = new Point3D(c.getX()+(Trixel.SIZE*clockwise), c.getY()+(Trixel.SIZE*(1-clockwise)), c.getZ());

		return new TrixelFace(vertices, Renderer.getRandomColour());
	}
	// -----------------------------------------------------------

	/**
	 * makes a list of trixels to represent a flat 2d polygon
	 * PRE: polygon vertices must all have same y (height component) (like a with a floor).
	 * @param poly
	 * @return list of trixels which make up the polygon
	 */
	public static List<Trixel> polygon2DToTrixels(Polygon poly, float y){
		List<Trixel> trixels = new ArrayList<Trixel>();
		Rectangle polyBounds = poly.getBounds();
		for (int x = polyBounds.x; x < polyBounds.x + polyBounds.width; x += Trixel.SIZE){
			for (int z = polyBounds.y; z < polyBounds.y + polyBounds.height; z += Trixel.SIZE){
				if (poly.contains(x,z)){
					Trixition trixition = TrixelUtil.positionToTrixition(new Point3D(x, y, z));
					trixels.add(new Trixel(trixition, Renderer.getRandomColour()));
				}
			}
		}
		return trixels;
	}
}
