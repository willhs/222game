package game.ui.render.util;

import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;
import java.util.Iterator;

/**
 * @author will
 * Represents a face of a trixel/voxel/cube
 */
public class TrixelFace implements ZComparable, Transformable{

	private final Point3D[] vertices;
	/**
	 * colour assuming fully lighted
	 */
	private final Color baseColour;
	/**
	 * the normal vector of this face as a plane.
	 */
	private Vector3D normal;

	/**
	 * PRE: must have 4 vertices
	 * PRE: must be ordered so that lines from each point in order (i to i+1) form a square (eg (0,0,0), (1,0,0), (1,1,0), (0,1,0))
	 * PRE: element 0 be must leftest, bottomest, farest point.
	 * @param vertices
	 * @param z
	 */
	public TrixelFace(Point3D[] vertices, Color colour){
		this.vertices = vertices;
		this.baseColour = colour;
		normal = this.calculateNormal();
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
		return baseColour;
	}

	/**
	 * Checks if the polygon is currently facing the viewer
	 * @return whether the polygon should be drawn
	 */
	public boolean isFacingViewer() {

		return calculateNormal().getZ() > 0;
	}

	private Vector3D calculateNormal(){
		Vector3D edge1 = vertices[1].distanceTo(vertices[0]);
		Vector3D edge2 = vertices[1].distanceTo(vertices[2]);

		return edge1.crossProduct(edge2);
	}

	/*public void getRealColour(Iterator<LightSource> lights){
		double reflection = 0;
		while(lights.hasNext()){
			LightSource light = lights.next();
			Vector3D lightDir = light.getDirection();
			float cosTheta = normal.cosTheta(lightDir);
			double angle = Math.acos(cosTheta);
		//	System.out.println("angle: "+angle);
			reflection += (angle / (Math.PI));
		}
		try{
			this.shadedColour = new Color((int)(initColour.getRed()*reflection), (int)(initColour.getGreen()*reflection), (int)(initColour.getBlue()*reflection));
		}catch(IllegalArgumentException e){ this.shadedColour = initColour; }
	}*/

	@Override
	public void transform(Transform transform) {
		for (int i = 0; i < vertices.length; i++){
			vertices[i] = transform.multiply(vertices[i]);
		}
		normal = transform.multiply(normal);
	}

	public Point3D[] getVertices() {
		return vertices;
	}

}
