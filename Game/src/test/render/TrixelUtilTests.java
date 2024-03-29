package test.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelFace;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.trixel.Trixition;
import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;
import java.util.List;

import org.junit.Test;

public class TrixelUtilTests {

	@Test public void positionToTrixitionTest(){
		Point3D p1 = new Point3D(11,22,33); 
		Trixition t1 = TrixelUtil.positionToTrixition(p1, Trixel.DEFAULT_SIZE);
		
		assertEquals(t1.x, 11/Trixel.DEFAULT_SIZE);
		assertEquals(t1.y, 22/Trixel.DEFAULT_SIZE);
		assertEquals(t1.z, 33/Trixel.DEFAULT_SIZE);
		
		float x = 1084.5923f;
		float y = 480123.523f;
		float z = 907984.611112f;
		Point3D p2 = new Point3D(x,y,z); 
		Trixition t2 = TrixelUtil.positionToTrixition(p2, Trixel.DEFAULT_SIZE);
		
		assertEquals(t2.x, (int)(x/Trixel.DEFAULT_SIZE));
		assertEquals(t2.y, (int)(y/Trixel.DEFAULT_SIZE));
		assertEquals(t2.z, (int)(z/Trixel.DEFAULT_SIZE));
	}
	
	@Test public void trixitionToPositionTest(){
		Trixition t1 = new Trixition(4,5,6);
		Point3D p1 = TrixelUtil.trixitionToPosition(t1, Trixel.DEFAULT_SIZE);
		
		assertEquals((int)p1.getX(), 4*Trixel.DEFAULT_SIZE);
		assertEquals((int)p1.getY(), 5*Trixel.DEFAULT_SIZE);
		assertEquals((int)p1.getZ(), 6*Trixel.DEFAULT_SIZE);
	}
	
	/**
	 * getTrixelFaces should always return 6 faces
	 */
	@Test public void getTrixelFacesTest1(){
		int testsCount = 50;
		for (int i = 0; i < testsCount; i++){
			int x = (int)(Math.random()*1000);
			int y = (int)(Math.random()*1000);
			int z = (int)(Math.random()*1000);
			Trixel t = new Trixel(new Trixition(x,y,z), Color.BLACK);
			TrixelFace[] faces = TrixelUtil.makeTrixelFaces(t, Trixel.DEFAULT_SIZE);
			assertEquals(faces.length, 6);
		}
	}
	
	@Test public void getTrixelFacesCoordinateTest(){
		int numOfTests = 50;
		
		for (int t = 0; t < numOfTests; t++){
			// trixition coordinates
			int left = (int)Math.random()*100;
			int bottom = (int)Math.random()*100;
			int far = (int)Math.random()*100;
			Trixel trixel = new Trixel(new Trixition(left,bottom,far), Color.green);
			TrixelFace[] faces = TrixelUtil.makeTrixelFaces(trixel, Trixel.DEFAULT_SIZE);
			
			// LEFT
			TrixelFace leftFace = faces[0];
			Point3D[] leftFaceVertices = leftFace.getVertices();
			// 4 vertices
			assertEquals(leftFaceVertices.length, 4);
			// test if points are correct
			assertTrue(leftFaceVertices[0].getX() == left);
			assertTrue(leftFaceVertices[0].getY() == bottom);
			assertTrue(leftFaceVertices[0].getZ() == far);
			
			assertTrue(leftFaceVertices[1].getX() == left);
			assertTrue(leftFaceVertices[1].getY() == bottom);
			assertTrue(leftFaceVertices[1].getZ() == far+Trixel.DEFAULT_SIZE);
			
			assertTrue(leftFaceVertices[2].getX() == left);
			assertTrue(leftFaceVertices[2].getY() == bottom+Trixel.DEFAULT_SIZE);
			assertTrue(leftFaceVertices[2].getZ() == far+Trixel.DEFAULT_SIZE);
			
			assertTrue(leftFaceVertices[3].getX() == left);
			assertTrue(leftFaceVertices[3].getY() == bottom+Trixel.DEFAULT_SIZE);
			assertTrue(leftFaceVertices[3].getZ() == far);
			
			// BOTTOM
			TrixelFace bottomFace = faces[2];
			Point3D[] bottomFaceVertices = bottomFace.getVertices();
			// 4 vertices
			assertEquals(bottomFaceVertices.length, 4);
			// test if points are correct
			assertTrue(bottomFaceVertices[0].getX() == left);
			assertTrue(bottomFaceVertices[0].getY() == bottom);
			assertTrue(bottomFaceVertices[0].getZ() == far);
			
			assertTrue(bottomFaceVertices[1].getX() == left+Trixel.DEFAULT_SIZE);
			assertTrue(bottomFaceVertices[1].getY() == bottom);
			assertTrue(bottomFaceVertices[1].getZ() == far);
			
			assertTrue(bottomFaceVertices[2].getX() == left+Trixel.DEFAULT_SIZE);
			assertTrue(bottomFaceVertices[2].getY() == bottom);
			assertTrue(bottomFaceVertices[2].getZ() == far+Trixel.DEFAULT_SIZE);
			
			assertTrue(bottomFaceVertices[3].getX() == left);
			assertTrue(bottomFaceVertices[3].getY() == bottom);
			assertTrue(bottomFaceVertices[3].getZ() == far+Trixel.DEFAULT_SIZE);
			
			// FAR
			TrixelFace farFace = faces[4];
			Point3D[] farFaceVertices = farFace.getVertices();
			// 4 vertices
			assertEquals(farFaceVertices.length, 4);
			// test if points are correct
			assertTrue(farFaceVertices[0].getX() == left);
			assertTrue(farFaceVertices[0].getY() == bottom);
			assertTrue(farFaceVertices[0].getZ() == far);
			
			assertTrue(farFaceVertices[1].getX() == left+Trixel.DEFAULT_SIZE);
			assertTrue(farFaceVertices[1].getY() == bottom);
			assertTrue(farFaceVertices[1].getZ() == far);
			
			assertTrue(farFaceVertices[2].getX() == left+Trixel.DEFAULT_SIZE);
			assertTrue(farFaceVertices[2].getY() == bottom+Trixel.DEFAULT_SIZE);
			assertTrue(farFaceVertices[2].getZ() == far);
			
			assertTrue(farFaceVertices[3].getX() == left);
			assertTrue(farFaceVertices[3].getY() == bottom+Trixel.DEFAULT_SIZE);
			assertTrue(farFaceVertices[3].getZ() == far);
		}
	}
	
	/**
	 * should make one trixel at the correct position
	 */
	@Test public void polygon2DToTrixelstest1(){ 
		int top = 100;
		int left = 100;
		int z = 0;
		int[] xpoints = { left , left + Trixel.DEFAULT_SIZE, left + Trixel.DEFAULT_SIZE, left};
		int[] ypoints = { top , top,  top + Trixel.DEFAULT_SIZE, top + Trixel.DEFAULT_SIZE};
		Polygon poly = new Polygon(xpoints, ypoints, xpoints.length);
		List<Trixel> trixels = TrixelUtil.polygon2DToTrixels(poly, Trixel.DEFAULT_SIZE, z);
		
		assertEquals(1, trixels.size());
		
		Trixel t = trixels.get(0);
		Trixition trix = t.getTrixition();
		
		assertEquals(trix.x, (int)(top/Trixel.DEFAULT_SIZE));
		assertEquals(trix.y, (int)(left/Trixel.DEFAULT_SIZE));
		assertEquals(trix.z, (int)(z/Trixel.DEFAULT_SIZE));
	}
	
	@Test public void testTrixelCentroid(){
		
		int numTrials = 50;
		
		for (int trial = 0; trial < numTrials; trial++){
			int x = 3;
			int y = 3;
			int z = 3;
			
			Trixition trix = new Trixition(x,y,z);
			Trixel t = new Trixel(trix, Color.WHITE);
			Point3D centroid = TrixelUtil.findTrixelCentroid(t, Trixel.DEFAULT_SIZE);
			
			assertEquals(TrixelUtil.trixitionToPosition(trix, Trixel.DEFAULT_SIZE), centroid);
		}
	}
	
}
