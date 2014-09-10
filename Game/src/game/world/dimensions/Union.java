package game.world.dimensions;

/**
 * Union
 * This handle the union of to rectangles.
 * @author Shane Brewer
 *
 */
public class Union extends Rectangle implements Dimension {
	
	Dimension dimensionOne;
	Dimension dimensionTwo;

	public Union(Dimension dimensionOne, Dimension dimensionTwo) {
		super(Math.min(dimensionOne.getX(), dimensionTwo.getX()), Math.min(
				dimensionOne.getY(), dimensionTwo.getY()), Math.max(
				dimensionOne.getWidth(), dimensionTwo.getWidth()), Math.max(
				dimensionOne.getHeight(), dimensionTwo.getHeight()));
		
		this.dimensionOne = dimensionOne;
		this.dimensionTwo = dimensionTwo;
	}
	
	@Override
	public boolean contains(double x, double y){
		return dimensionOne.contains(x, y) || dimensionTwo.contains(x, y);
	}
	
	@Override
	public Rectangle getBoundingBox(){
		return this;
	}
	
}
