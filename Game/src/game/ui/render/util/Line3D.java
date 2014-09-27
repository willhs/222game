package game.ui.render.util;

import game.ui.render.util.Renderable;
import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;

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
	public float getZ() {
		return 0;
	}

	@Override
	public void flipY(int top) {
		p1 = p1.flipY(top);
		p2 = p2.flipY(top);
	}
	@Override
	public void transform(Transform transform){
		p1 = transform.multiply(p1);
		p2 = transform.multiply(p2);
	}
}
