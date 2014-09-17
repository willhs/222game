package game.ui.render.util;

import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;

/**
 * @author will
 * Represents a face of a trixel/voxel/cube
 */
public class TrixelFace implements ZComparable{

	private final Point3D[] vertices;
	private final Color colour;

	/**
	 * PRE: must have 4 vertices
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
	 * @return whether the polygon shuold be drawn
	 */
	public boolean shouldBeDrawn(Transform viewerDirectionTransform){
		Vector3D edge1 = vertices[1].distanceTo(vertices[0]);
		Vector3D edge2 = vertices[1].distanceTo(vertices[2]);

		Vector3D normal = edge1.crossProduct(edge2);
		normal.transform(viewerDirectionTransform);

		return normal.getZ() > 0;
	}

}
