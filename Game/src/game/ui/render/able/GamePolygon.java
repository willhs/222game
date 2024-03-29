package game.ui.render.able;

import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;

/**
 * @author hardwiwill	300285801
 *
 * A java.awt.Polygon with a depth component.
 */
public class GamePolygon extends Polygon implements Renderable{

	private float z;
	private Color colour;

	public GamePolygon(int[] xpoints, int[] ypoints, int npoints, float z, Color colour){
		super(xpoints, ypoints, npoints);
		this.z = z;
		this.colour = colour;
	}

	@Override
	public float getDepth() {
		return z;
	}

	public Color getColour() {
		return colour;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("Poly:");
		for (int i=0; i < npoints; i++){
			builder.append("[ "+xpoints[i]);
			builder.append(", ");
			builder.append(ypoints[i]+" ] ");
		}
		return builder.toString();
	}

	@Override
	public void flipAroundY(int top) {
		for (int i = 0; i < ypoints.length; i++){
			ypoints[i] = top - ypoints[i];
		}

	}

	/**
	 * @return center point or centroid of polygon
	 */
	public Point3D getCentroid(){
		int xSum = 0;
		int ySum = 0;
		for (int i = 0; i < npoints; i++){
			xSum += xpoints[i];
			ySum += ypoints[i];
		}
		return new Point3D(xSum/npoints, ySum/npoints, z);
	}

	/**
	 * Translates the z component.
	 * @param translateZ
	 */
	public void translateZ(float translateZ) {
		z += translateZ;
	}

}
