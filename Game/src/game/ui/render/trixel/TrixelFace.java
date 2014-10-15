package game.ui.render.trixel;

import game.ui.render.util.DepthComparable;
import game.ui.render.util.LightSource;
import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author will	hardwick-smith 300285801
 *
 * Represents a face of a Trixel
 * 4 vertices, area = Trixel.SIZE
 */
public class TrixelFace implements DepthComparable, Transformable, Cloneable{

	private final Point3D[] vertices;

	/**
	 * The trixel which this face is partly representing.
	 */
	private Trixel parent;

	private Color shadedColour;
	/**
	 * PRE: must have 4 vertices
	 * PRE: must be ordered so that lines from each point in order (i to i+1)
	 * 		form a square (eg (0,0,0), (1,0,0), (1,1,0), (0,1,0))
	 * PRE: element 0 be must leftest, bottomest, farest point.
	 * @param vertices
	 * @param z
	 */
	public TrixelFace(Point3D[] vertices, Trixel parent){
		this.vertices = vertices;
		this.parent = parent;
		this.shadedColour = parent.getColor();
	}

	/* Gets center z position
	 * @see game.ui.render.ZComparable#getZ()
	 */
	public float getDepth(){
		float z = 0;
		for (Point3D vertex : vertices){
			z += vertex.getZ();
		}
		return z/vertices.length;
	}

	/**
	 * @return the colour of the trixel (without shading)
	 */
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
		Vector3D edge1 = vertices[0].distanceTo(vertices[1]);
		Vector3D edge2 = vertices[2].distanceTo(vertices[1]);

		return edge1.crossProduct(edge2);
	}

	/**
	 * @param lights
	 * @param ambientLight
	 * @return the colour given light sources and ambient light
	 */
	public void setShadedColour(Iterator<LightSource> lights, Color ambientLight){
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

		shadedColour = new Color((int)(getBaseColour().getRed()*reflectionR), (int)(getBaseColour().getGreen()*reflectionG), (int)(getBaseColour().getBlue()*reflectionB));
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

	/**
	 * @return center point of the face
	 */
	public Point3D findCenterPoint(){

		int x=0,y=0,z=0;

		for (Point3D vertex : vertices){
			x += vertex.x;
			y += vertex.y;
			z += vertex.z;
		}
		return new Point3D(x/vertices.length, y/vertices.length, z/vertices.length);
	}

	/**
	 * @return the gradient of the line between the two highest vertices.
	 */
	public Vector3D findTopLineGradient(){
		List<Point3D> highestVertices = new ArrayList<Point3D>(Arrays.asList(vertices));
		Collections.sort(highestVertices, new Comparator<Point3D>(){
			@Override
			public int compare(Point3D p1, Point3D p2){
				return -Float.compare(p1.y, p2.y);
			}
		});
		Point3D v1 = highestVertices.get(0);
		Point3D v2 = highestVertices.get(1);

		return (v1.x < v2.x ? v1.distanceTo(v2) : v2.distanceTo(v1)).unitVector();
	}

	public Color getShadedColour() {
		return shadedColour;
	}

	@Override
	public TrixelFace clone(){
		return new TrixelFace(vertices.clone(), parent);
	}

	public void setVertices(Point3D[] vertices){
		if (vertices.length != this.vertices.length)
			throw new IllegalArgumentException("Illegal number of vertices");
		for (int i = 0; i < this.vertices.length; i++){
			this.vertices[i] = vertices[i];
		}
	}

}