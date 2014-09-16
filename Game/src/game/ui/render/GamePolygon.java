package game.ui.render;

import java.awt.Polygon;

/**
 * @author hardwiwill
 * Wrapper class for a java.awt.Polygon so that i can assign z position (to determine order of drawing).
 * TODO
 */
public class GamePolygon extends Polygon implements ZComparable{

	private float z;

	public GamePolygon(int[] xpoints, int[] ypoints, int npoints, float z){
		super(xpoints, ypoints, npoints);
		this.z = z;
	}

	@Override
	public float getZ() {
		return z;
	}

}
