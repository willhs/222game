package test.render;

import static org.junit.Assert.assertTrue;
import game.ui.render.Renderer;
import game.ui.render.util.Transform;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;

import org.junit.Test;

public class RendererTests {

	@Test public void getRotateAroundPointTransforms1(){

		int max = 10000;
		int maxTrials = 100;

		for (int trial = 0; trial < maxTrials; trial++){
			int x = (int)Math.random()*max;
			int y = (int)Math.random()*max;
			int z = (int)Math.random()*max;
			Point3D p = new Point3D(x,y,z);
			Point3D center = new Point3D((int)Math.random()*max,
					(int)Math.random()*max,
					(int)Math.random()*max);

			Transform[] transforms = Renderer.getRotateAroundPointTransforms(
					new Vector3D((float)(Math.PI*2), (float)(Math.PI*2), (float)(Math.PI*2)), center);

			p = transforms[0].multiply(p);
			p = transforms[1].multiply(p);
			p = transforms[2].multiply(p);

			//the minimum amount of difference allowed between outcome and expected outcome
			float minDiff = 0.00001f;

			assertTrue(Math.abs(p.x - x) < minDiff);
			assertTrue(Math.abs(p.y - y) < minDiff);
			assertTrue(Math.abs(p.z - z) < minDiff);
		}
	}

	@Test public void getRotateAroundPointTransforms2(){

		int max = 10000;
		int maxTrials = 100;

		for (int trial = 0; trial < maxTrials; trial++){
			int x = (int)Math.random()*max;
			int y = (int)Math.random()*max;
			int z = (int)Math.random()*max;
			Point3D p = new Point3D(x,y,z);
			Point3D center = new Point3D(10,11,12);

			Transform[] transforms = Renderer.getRotateAroundPointTransforms(
					new Vector3D((float)(Math.PI*2), (float)(Math.PI*2), (float)(Math.PI*2)), center);

			p = transforms[0].multiply(p);
			p = transforms[1].multiply(p);
			p = transforms[2].multiply(p);

			//the minimum amount of difference allowed between outcome and expected outcome
			float minDiff = 0.00001f;

			assertTrue(Math.abs(p.x - x) < minDiff);
			assertTrue(Math.abs(p.y - y) < minDiff);
			assertTrue(Math.abs(p.z - z) < minDiff);
		}
	}

}
