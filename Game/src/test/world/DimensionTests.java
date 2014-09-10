package test.world;

import static org.junit.Assert.*;
import game.world.dimensions.Complement;
import game.world.dimensions.Dimension;
import game.world.dimensions.Intersection;
import game.world.dimensions.Rectangle;
import game.world.dimensions.Union;

import org.junit.Test;

public class DimensionTests {

	// Union intersection complement
	public enum Type{U, I, C}
	
	@Test
	public void pointContainsTest1() {
		Dimension dimension = makeDimension(0, 0, 200, 200);
		if (!dimension.contains(100, 100)){
			fail("this dimension should contain this point");
		}
	}
	
	@Test
	public void pointContainsTest2() {
		Dimension dimension = makeDimension(0, 0, 200, 200);
		if (!dimension.contains(150, 100)){
			fail("this dimension should contain this point");
		}
	}
	
	@Test
	public void pointContainsTest3() {
		Dimension dimension = makeDimension(makeDimension(0, 0, 200, 200), makeDimension(0, 0, 30, 30), Type.C);
		if (!dimension.contains(100, 100)){
			fail("this dimension should contain this point");
		}
	}
	
	@Test
	public void pointNotContainsTest1() {
		Dimension dimension = makeDimension(makeDimension(0, 0, 200, 200), makeDimension(0, 0, 30, 30), Type.C);
		if (dimension.contains(20, 20)){
			fail("this dimension should not contain this point");
		}
	}
	
	@Test
	public void pointNotContainsTest2() {
		Dimension dimension = makeDimension(makeDimension(0, 0, 200, 200), makeDimension(0, 0, 30, 30), Type.U);
		if (dimension.contains(200, 220)){
			fail("this dimension should not contain this point");
		}
	}
	
	
	
	private Dimension makeDimension(int x, int y, int width, int height){
		return new Rectangle(x,y,width,height);
	}
	
	private Dimension makeDimension(Dimension dOne, Dimension dTwo, Type type){
		if (Type.U == type){
			return new Union(dOne, dTwo);
		}
		else if (Type.I == type){
			return new Intersection(dOne, dTwo);
		}
		else {
			return new Complement(dOne, dTwo);
		}
	}
	

}
