package game.ui.render.levelmaker;

import game.ui.render.util.Trixel;

/**
 * A 3D grid of trixel
 * @author hardwiwill
 */
public class TrixelGrid {

	private Trixel[][][] trixels;

	public TrixelGrid(int width, int height, int length){
		trixels = new Trixel[width][height][length];
	}

	public Trixel getTrixel(int x, int y, int z) {
		return trixels[x][y][z];
	}

	public void setTrixel(Trixel trixel, int x, int y, int z) {
		this.trixels[x][y][z] = trixel;
	}
}
