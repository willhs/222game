package game.ui.render.util;

import game.world.dimensions.Point3D;

/**
 * @author hardwiwill
 * For helper methods for converting between trixitions and 3d points in the world.
 */
public class TrixelUtil {

	/**
	 * @param position
	 * @return the position in the trixel grid
	 */
	public static Trixition positionToTrixition(Point3D position){
		return new Trixition((int)Math.floor(position.getX() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE),
				(int)Math.floor(position.getY() / Trixel.SIZE));
	}

	/**
	 * @param trixition (position in the trixel grid)
	 * @return the position in the 3d world of the ** top, left, far ** point of the trixition
	 */
	public static Point3D trixitionToPosition(Trixition trixition){
		return new Point3D(trixition.x * Trixel.SIZE,
				trixition.y * Trixel.SIZE,
				trixition.z * Trixel.SIZE);
	}
	
	/**
	 * @return 6 faces of a trixel
	 */
	public TrixelFace[] getTrixelFaces(Trixel trixel){
		TrixelFace[] faces = new TrixelFace[6];
		Point3D trixelCenter = TrixelUtil.trixitionToPosition(trixel.getTrixition());
		
		float face1X[] = {trixelCenter.getX()-(Trixel.SIZE/2), trixelCenter.getX()-(Trixel.SIZE/2), trixelCenter.getX()-(Trixel.SIZE/2), trixelCenter.getX()-(Trixel.SIZE/2)};
		
		return faces;
	}
}
