package game.ui.render.trixel;

/**
 * @author hardwiwill
 *
 * The position of a Trixel in the trixel space.
 * Can be converted into real space, using TrixelUtil.trixitionToPosition
 */
public class Trixition {

	public final int x, y, z;

	public Trixition(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof Trixition))
			return false;

		Trixition t = (Trixition) o;

		return x == t.x && y == t.y && z == t.z;
	}

	@Override
	public int hashCode(){
		return x * y * z;
	}

	@Override
	public String toString(){
		return String.format("(%d, %d, %d)", x, y, z);
	}

}
