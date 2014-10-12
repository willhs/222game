package game.ui.render.trixel;

import java.awt.Color;

/**
 * @author hardwiwill
 *
 * Trixel - a pixel in tri dimensions. Also known as Voxel. Naming inspired by it's usage in FEZ game.
 *
 * A 3D pixel/voxel/cube at a position in trixel space (could be thought of as a 3d array).
 * 6 faces, all with equal area (like a cube).
 */
public class Trixel {

	/**
	 * value for trixel width, height and depth
	 */
	public static final int DEFAULT_SIZE = 20;
	private static final Color DEFAULT_COLOUR = Color.BLUE;

	/**
	 * The trixel's position in the trixel grid (trid)!
	 */
	private Trixition trixition;

	private Color colour;

	public Trixel (Trixition trixition, Color c){
		this.trixition = trixition;
		this.colour = c;
	}

	public Trixel (Trixition trixition){
		this.trixition = trixition;
		this.colour = DEFAULT_COLOUR;
	}

	public Color getColor(){
		return colour;
	}

	public Trixition getTrixition(){
		return trixition;
	}

	public void setTrixition(Trixition trixition){
		this.trixition = trixition;
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
