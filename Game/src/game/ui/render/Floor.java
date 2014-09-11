package game.ui.render;

import game.world.dimensions.Point3D;


public class Floor implements Transformable{

	private final Point3D[] points;

	public Floor(Point3D[] points){
		this.points = points;
	}

	public Point3D[] getPoints(){
		return points;
	}
	
	@Override
	public void transform(Transform transform){
		for (Point3D p : points){
			p.transform(transform);
		}
	}

}
