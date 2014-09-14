package game.ui.render.util;

import game.ui.render.ZComparable;
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
	 * @param vertices
	 * @param z
	 * PRE: must have 4 vertices
	 */
	public TrixelFace(Point3D[] vertices, Color colour){
		this.vertices = vertices;
		this.colour = colour;
	}
	
	public Polygon get2DFace(){
		return new Polygon();
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
	
}
