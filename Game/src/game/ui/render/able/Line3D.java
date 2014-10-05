package game.ui.render.able;

import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;

/**
 * @author hardwiwill
 *
 * A line from p1, to p2.
 */
public class Line3D implements Renderable, Transformable{

	private Point3D p1, p2;

	public Line3D(Point3D p1, Point3D p2){
		this.p1 = p1;
		this.p2 = p2;
	}

	public Point3D getP1(){
		return p1;
	}
	public Point3D getP2(){
		return p2;
	}

	@Override
	public float getDepth() {
		return 0;
	}

	@Override
	public void flipAroundY(int top) {
		p1 = p1.flipY(top);
		p2 = p2.flipY(top);
	}
	@Override
	public void transform(Transform transform){
		p1 = transform.multiply(p1);
		p2 = transform.multiply(p2);
	}
}
