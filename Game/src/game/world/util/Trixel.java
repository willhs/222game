package game.world.util;

import game.world.dimensions.Point3D;

import java.awt.Color;

/**
 * @author hardwiwill
 * Like a 3D pixel, a cube
 *
 */
public class Trixel {

	/**
	 * The center of the trixel!
	 */
	private Point3D point;
	/**
	 * TODO Will need to be one of these for each face of the
	 */
	private Color c;

	public Trixel (Point3D point, Color c){
		this.point = point;
		this.c = c;
	}

}
