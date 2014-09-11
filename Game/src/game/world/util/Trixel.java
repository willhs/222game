package game.world.util;

import java.awt.Color;

/**
 * @author hardwiwill
 * Like a 3D pixel, or a cube.
 *
 */
public class Trixel {

	/**
	 * trixel width, height and depth
	 */
	public static final int SIZE = 10;
	/**
	 * The trixel's position in the trixel grid (trid)!
	 */
	private Trixition trixition;
	/**
	 * TODO Will need to be one of these for each face of the
	 */
	private Color c;

	public Trixel (Trixition trisition, Color c){
		this.trixition = trisition;
		this.c = c;
	}

}
