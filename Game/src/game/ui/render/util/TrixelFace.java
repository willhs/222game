package game.ui.render.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author will
 * Represents a face of a trixel/voxel/cube
 */
public class TrixelFace implements ZComparable, Transformable{

	private final Point3D[] vertices;

	private Trixel parent;
	/**
	 * PRE: must have 4 vertices
	 * PRE: must be ordered so that lines from each point in order (i to i+1) form a square (eg (0,0,0), (1,0,0), (1,1,0), (0,1,0))
	 * PRE: element 0 be must leftest, bottomest, farest point.
	 * @param vertices
	 * @param z
	 */
	public TrixelFace(Point3D[] vertices, Trixel parent){
		this.vertices = vertices;
		this.parent = parent;
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

	public Color getBaseColour(){
		return parent.getColor();
	}

	/**
	 * Checks if the polygon is currently facing the viewer
	 * @return whether the polygon should be drawn
	 */
	public boolean isFacingViewer() {
		return calculateNormal().getZ() > 0;
	}

	/**
	 * @return the normal (perpendicular) vector of this face
	 */
	public Vector3D calculateNormal(){
		Vector3D edge1 = vertices[1].distanceTo(vertices[0]);
		Vector3D edge2 = vertices[1].distanceTo(vertices[2]);

		return edge1.crossProduct(edge2);
	}

	/**
	 * @param lights
	 * @param ambientLight
	 * @return the colour given light sources and ambient light
	 */
	public Color makeShadedColour(Iterator<LightSource> lights, Color ambientLight){
		float reflectionR = 0, reflectionG = 0, reflectionB = 0;
		Vector3D normal = calculateNormal();

		while (lights.hasNext()){

			LightSource light = lights.next();
			float cosTheta = normal.cosTheta(light.getDirection());
			double angle = Math.acos(cosTheta);
			float reflectance = (float)(angle / (Math.PI)) * light.getIntensity();

			Color lightColour = light.getColour();
			float redScale = (float)lightColour.getRed() / 255;
			float greenScale = (float)lightColour.getGreen() / 255;
			float blueScale = (float)lightColour.getBlue() / 255;


			reflectionR += reflectance * redScale;
			reflectionG += reflectance * greenScale;
			reflectionB += reflectance * blueScale;
		}

		reflectionR += (float)ambientLight.getRed() / 255;
		reflectionG += (float)ambientLight.getGreen() / 255;
		reflectionB += (float)ambientLight.getBlue() / 255;

		reflectionR = Math.min(reflectionR, 1);
		reflectionG = Math.min(reflectionG, 1);
		reflectionB = Math.min(reflectionB, 1);

		return new Color((int)(getBaseColour().getRed()*reflectionR), (int)(getBaseColour().getGreen()*reflectionG), (int)(getBaseColour().getBlue()*reflectionB));
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

	public Trixel getParentTrixel() {
		return parent;
	}

	public String toString(){
		return Arrays.toString(vertices);
	}

}
