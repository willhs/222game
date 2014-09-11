package game.world.util;

public class Trixition {

	/**
	 * The position of a Trixel in the 3d Trixel grid, or "Trid".
	 * public because they are immutable
	 */
	public final int x, y, z;

	public Trixition(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
