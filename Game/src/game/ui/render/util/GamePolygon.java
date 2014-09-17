package game.ui.render.util;

import java.awt.Color;
import java.awt.Polygon;

/**
 * @author hardwiwill
 * Wrapper class for a java.awt.Polygon so that i can assign z position (to determine order of drawing).
 *
 */
public class GamePolygon extends Polygon implements ZComparable{

	private float z;
	private Color colour;

	public GamePolygon(int[] xpoints, int[] ypoints, int npoints, float z, Color colour){
		super(xpoints, ypoints, npoints);
		this.z = z;
		this.colour = colour;
	}

	@Override
	public float getZ() {
		return z;
	}

	public Color getColour() {
		return colour;
	}

}
