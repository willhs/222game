package game.ui.render.util;

import java.awt.Color;

/**
 * @author hardwiwill
 * A 3D pixel/voxel/cube.
 */
public class Trixel {

	/**
	 * value for trixel width, height and depth
	 */
	public static final int SIZE = 30;
	/**
	 * The trixel's position in the trixel grid (trid)!
	 */
	private Trixition trixition;

	private Color colour;

	public Trixel (Trixition trisition, Color c){
		this.trixition = trisition;
		this.colour = c;
	}

	public Color getColor(){
		return colour;
	}

	public Trixition getTrixition(){
		return trixition;
	}
}
