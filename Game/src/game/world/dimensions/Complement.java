package game.world.dimensions;

import java.util.List;

public class Complement extends Rectangle implements Dimension{
	Dimension dimensionOne;
	Dimension dimensionTwo;

	public Complement(Dimension dimensionOne, Dimension dimensionTwo) {
		super(Math.min(dimensionOne.getX(), dimensionTwo.getX()), Math.min(
				dimensionOne.getY(), dimensionTwo.getY()), Math.max(
				dimensionOne.getWidth(), dimensionTwo.getWidth()), Math.max(
				dimensionOne.getHeight(), dimensionTwo.getHeight()));
		
		this.dimensionOne = dimensionOne;
		this.dimensionTwo = dimensionTwo;
	}
	
	@Override
	public boolean contains(float x, float y){
		return dimensionOne.contains(x, y) && !dimensionTwo.contains(x, y);
	}
	
	@Override
	public Rectangle getBoundingBox(){
		return this;
	}
	
	@Override
	public List<Point3D> getCorners(){
		List<Point3D> pointList = dimensionOne.getCorners();
		List<Point3D> toRemove = dimensionTwo.getCorners();
		for (int i =0; i < toRemove.size(); i++){
			if (pointList.contains(toRemove.get(i))){
				pointList.remove(toRemove.get(i));
			}
			else {
				pointList.add(toRemove.get(i));
			}
		}
		return pointList;
	}
}
