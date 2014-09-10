package game.world.dimensions;

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
	public boolean contains(double x, double y){
		return dimensionOne.contains(x, y) && !dimensionTwo.contains(x, y);
	}
	
	@Override
	public Rectangle getBoundingBox(){
		return this;
	}
}
