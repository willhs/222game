package game.world.util;

import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;

import java.util.Arrays;


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
		for (int i = 0; i <points.length; i++){
			points[i] = transform.multiply(points[i]);
		}
	}

	public String toString(){
		return Arrays.toString(points);
	}

}
