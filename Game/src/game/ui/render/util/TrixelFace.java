package game.ui.render.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import java.awt.Color;
import java.awt.Polygon;

/**
 * @author will
 * Represents a face of a trixel/voxel/cube
 */
public class TrixelFace implements ZComparable, Transformable{

	private final Point3D[] vertices;
	private final Color colour;

	/**
	 * PRE: must have 4 vertices
	 * PRE: must be ordered so that lines from each point in order (i to i+1) form a square (eg (0,0,0), (1,0,0), (1,1,0), (0,1,0))
	 * PRE: element 0 be must leftest, bottomest, farest point.
	 * @param vertices
	 * @param z
	 */
	public TrixelFace(Point3D[] vertices, Color colour){
		this.vertices = vertices;
		this.colour = colour;
	}

	/* Gets center z position
	 * @see game.ui.render.ZComparable#getZ()
	 */
	public float getZ(){
		float z = 0;
		for (Point3D vertex : vertices){
			z += vertex.getZ();
		}
		return z/vertices.length;
	}

	public Color getColour(){
		return colour;
	}

	/**
	 * Checks if the polygon is currently facing the viewer
	 * *** COULD BE OPTIMISED MORE***
	 * @return whether the polygon shuold be drawn
	 */
	public boolean isFacingViewer() {
		Vector3D edge1 = vertices[1].distanceTo(vertices[0]);
		Vector3D edge2 = vertices[1].distanceTo(vertices[2]);

		Vector3D normal = edge1.crossProduct(edge2);

		return normal.getZ() > 0;
	}

	@Override
	public void transform(Transform transform) {
		for (int i = 0; i < vertices.length; i++){		
			vertices[i] = transform.multiply(vertices[i]);
		}
	}

	public Point3D[] getVertices() {
		return vertices;
	}

}
