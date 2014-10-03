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
	public static final int SIZE = 20;
	/**
	 * The trixel's position in the trixel grid (trid)!
	 */
	private Trixition trixition;

	private Color colour;

	public Trixel (Trixition trixition, Color c){
		this.trixition = trixition;
		this.colour = c;
	}

	public Color getColor(){
		return colour;
	}

	public Trixition getTrixition(){
		return trixition;
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof Trixel))
			return false;

		Trixel other = (Trixel)o;

		return trixition.equals(other.getTrixition());
	}

	@Override
	public int hashCode(){
		return trixition.hashCode();
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	@Override
	public String toString(){
		return trixition + "\t" + colour;
	}
}
