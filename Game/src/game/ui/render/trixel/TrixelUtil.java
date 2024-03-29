package game.ui.render.trixel;

import game.ui.render.Renderer;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hardwiwill	300285801
 *
 * For helper methods involving trixels,
 * Functions include converting between trixitions and 3d points in the world,
 * converting between 2d polygons, making trixel faces in real space given a trixel,
 * finding the centroid of a trixel in real space, finding points in real space relative to a trixel.
 */
public class TrixelUtil {

	/**
	 * @param position
	 * @param trixelSize TODO
	 * @return the position in the trixel grid
	 */
	public static Trixition positionToTrixition(Point3D position, int trixelSize){
		return new Trixition((int)(position.getX() / trixelSize),
				(int)(position.getY() / trixelSize),
				(int)(position.getZ() / trixelSize));
	}

	/**
	 * @param trixition (position in the trixel grid)
	 * @param trixelSize TODO
	 * @return the position in the 3d world of the ** top, left, far ** point of the trixition
	 */
	public static Point3D trixitionToPosition(Trixition trixition, int trixelSize){
		return new Point3D(trixition.x * trixelSize,
				trixition.y * trixelSize,
				trixition.z * trixelSize);
	}

	/**
	 * Gets the faces of a trixel
	 * TODO: reuse code more
	 * POST: 6 TrixelFaces
	 * POST: Faces ordered: left, right, bottom, top, back, front (currently only for testing purposes).
	 * @return 6 faces of a trixel
	 */
	public static TrixelFace[] makeTrixelFaces(Trixel trixel, int trixelSize){
		TrixelFace[] faces = new TrixelFace[6];

		Point3D trixelOrigin = TrixelUtil.trixitionToPosition(trixel.getTrixition(), trixelSize);

		// left face
		// corner = corner of current face
		Point3D corner = trixelOrigin;
		faces[0] = getXFace(corner, 0, trixel, trixelSize);

		// right face
		corner = new Point3D(trixelOrigin.getX()+trixelSize, trixelOrigin.getY(), trixelOrigin.getZ());
		faces[1] = getXFace(corner, 1, trixel, trixelSize);

		// bottom face
		corner = trixelOrigin;
		faces[2] = getYFace(corner, 0, trixel, trixelSize);

		// top face
		corner = new Point3D(trixelOrigin.getX(),trixelOrigin.getY()+trixelSize,trixelOrigin.getZ());
		faces[3] = getYFace(corner, 1, trixel, trixelSize);

		// back face
		corner = trixelOrigin;
		faces[4] = getZFace(corner, 0, trixel, trixelSize);

		// front face
		corner = new Point3D(trixelOrigin.getX(),trixelOrigin.getY(),trixelOrigin.getZ()+trixelSize);
		faces[5] = getZFace(corner, 1, trixel, trixelSize);

		return faces;
	}

	// -------------- HELPER METHODS ----------------------
	/**
	 * @param c : far bottom corner of face
	 * @param colour: colour of face
	 * @return a trixel face in which the x value of the vertices is constant
	 */
	private static TrixelFace getXFace(Point3D c, int clockwise, Trixel trixel, int trixelSize){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX(), c.getY()+trixelSize*(1-clockwise), c.getZ()+trixelSize*clockwise);
		vertices[2] = new Point3D(c.getX(), c.getY()+trixelSize, c.getZ()+trixelSize);
		vertices[3] = new Point3D(c.getX(), c.getY()+trixelSize*clockwise, c.getZ()+trixelSize*(1-clockwise));

		return new TrixelFace(vertices, trixel);
	}

	/**
	 * @param c : far left corner of face
	 * @param colour: colour of face
	 * @return a trixel face in which the y value of the vertices is constant
	 */
	private static TrixelFace getYFace(Point3D c, int clockwise, Trixel trixel, int trixelSize){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX()+trixelSize*(clockwise), c.getY(), c.getZ()+trixelSize*(1-clockwise));
		vertices[2] = new Point3D(c.getX()+trixelSize, c.getY(), c.getZ()+trixelSize);
		vertices[3] = new Point3D(c.getX()+trixelSize*(1-clockwise), c.getY(), c.getZ()+trixelSize*(clockwise));

		return new TrixelFace(vertices, trixel);
	}

	/**
	 * @param c : bottom left corner of face
	 * @param colour: colour of face
	 * @param clockwise: 1 = make points in clockwise, 0 = make points in anti-clockwise
	 * @return a trixel face in which the z value of the vertices is constant
	 */
	private static TrixelFace getZFace(Point3D c, int clockwise, Trixel trixel, int trixelSize){
		Point3D[] vertices = new Point3D[4];
		vertices[0] = c;
		vertices[1] = new Point3D(c.getX()+(trixelSize*(1-clockwise)), c.getY()+(trixelSize*clockwise), c.getZ());
		vertices[2] = new Point3D(c.getX()+trixelSize, c.getY()+trixelSize, c.getZ());
		vertices[3] = new Point3D(c.getX()+(trixelSize*clockwise), c.getY()+(trixelSize*(1-clockwise)), c.getZ());

		return new TrixelFace(vertices, trixel);
	}
	// -----------------------------------------------------------

	/**
	 * makes a list of trixels to represent a flat 2d polygon
	 * POST: trixels which make up the polygon when it is vertically flipped 90deg.
	 * @param poly
	 * @param trixelSize TODO
	 * @return list of trixels which make up the polygon
	 */
	public static List<Trixel> polygon2DToTrixels(Polygon poly, int trixelSize, float y){
		List<Trixel> trixels = new ArrayList<Trixel>();
		Rectangle polyBounds = poly.getBounds();
		for (int x = polyBounds.x; x < polyBounds.x + polyBounds.width; x += trixelSize){
			for (int z = polyBounds.y; z < polyBounds.y + polyBounds.height; z += trixelSize){
				if (poly.contains(x,z)){
					Trixition trixition = TrixelUtil.positionToTrixition(new Point3D(x, y, z), trixelSize);
					trixels.add(new Trixel(trixition, Renderer.defaultMakeRandomColour()));
				}
			}
		}
		return trixels;
	}

	/**
	 * Finds the center point of all input trixels
	 * @param trixels
	 * @return the center point of many trixels
	 */
	public static Point3D findTrixelsCentroid(Iterator<Trixel> trixels, int trixelSize){

		int vertexCount = 0;
		float xSum = 0, ySum = 0, zSum = 0;

		while (trixels.hasNext()){
			Trixel trixel = trixels.next();
			Point3D trixelCentroid = findTrixelCentroid(trixel, trixelSize);
			xSum += trixelCentroid.x;
			ySum += trixelCentroid.y;
			zSum += trixelCentroid.z;

			vertexCount ++;
		}

		if (vertexCount == 0){ // to prevent division by 0
			return new Point3D(0,0,0);
		}
		return new Point3D (xSum/vertexCount, ySum/vertexCount, zSum/vertexCount);
	}

	/**
	 * @param trixel
	 * @return the center point of a trixel
	 */
	public static Point3D findTrixelCentroid(Trixel trixel, int trixelSize){

		int vertexCount = 0;
		float xSum = 0, ySum = 0, zSum = 0;

		for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel, trixelSize)){
			for (Point3D vertex : face.getVertices()){
				xSum += vertex.x;
				ySum += vertex.y;
				zSum += vertex.z;

				vertexCount ++;
			}
		}

		return new Point3D (xSum/vertexCount, ySum/vertexCount, zSum/vertexCount);
	}

	/**
	 * Get's the top, center position of the trixel
	 * @param trixel
	 * @return point in the center of a trixel's top face
	 */
	public static Point3D findTopCenterOfTrixel(Trixel trixel, int trixelSize){
		Vector3D translation = new Vector3D(0,0.5f,0).makeScaled(trixelSize);
		Point3D faceRealPosition = TrixelUtil.findTrixelCentroid(trixel, trixelSize);
		return faceRealPosition.getTranslatedPoint(translation);
	}
}
