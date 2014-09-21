package test.render;

import static org.junit.Assert.*;
import game.ui.render.Renderer;
import game.ui.render.util.Transform;
import game.ui.render.util.Vector3D;
import game.world.dimensions.Point3D;

import org.junit.Test;

public class RendererTests {

	@Test public void getRotateAroundPointTransforms(){
		Point3D p = new Point3D(4,5,6);
		Point3D center = new Point3D(10,11,12);
		
		Transform[] transforms = Renderer.getRotateAroundPointTransforms(
				new Vector3D((float)(Math.PI*2), (float)(Math.PI*2), (float)(Math.PI*2)), center);
		
		p = transforms[0].multiply(p);
		p = transforms[1].multiply(p);
		p = transforms[2].multiply(p);
		
		System.out.println(p);
		
		//the minimum amount of difference allowed between outcome and expected outcome
		float minDiff = 0.00001f;
		
		assertTrue(Math.abs(p.x - 4) < minDiff);
		assertTrue(Math.abs(p.y - 5) < minDiff);
		assertTrue(Math.abs(p.z - 6) < minDiff);
	}
	
}
