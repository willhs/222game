package game.ui.render.trixel;

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
