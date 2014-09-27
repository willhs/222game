package test.render;

import static org.junit.Assert.*;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Trixition;
import game.world.dimensions.Point3D;

import java.awt.Color;
import java.awt.Polygon;
import java.util.List;

import org.junit.Test;

public class TrixelUtilTests {

	@Test public void positionToTrixitionTest(){
		Point3D p1 = new Point3D(11,22,33); 
		Trixition t1 = TrixelUtil.positionToTrixition(p1);
		
		assertEquals(t1.x, 11/Trixel.SIZE);
		assertEquals(t1.y, 22/Trixel.SIZE);
		assertEquals(t1.z, 33/Trixel.SIZE);
		
		float x = 1084.5923f;
		float y = 480123.523f;
		float z = 907984.611112f;
		Point3D p2 = new Point3D(x,y,z); 
		Trixition t2 = TrixelUtil.positionToTrixition(p2);
		
		assertEquals(t2.x, (int)(x/Trixel.SIZE));
		assertEquals(t2.y, (int)(y/Trixel.SIZE));
		assertEquals(t2.z, (int)(z/Trixel.SIZE));
	}
	
	@Test public void trixitionToPositionTest(){
		Trixition t1 = new Trixition(4,5,6);
		Point3D p1 = TrixelUtil.trixitionToPosition(t1);
		
		assertEquals((int)p1.getX(), 4*Trixel.SIZE);
		assertEquals((int)p1.getY(), 5*Trixel.SIZE);
		assertEquals((int)p1.getZ(), 6*Trixel.SIZE);
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
			TrixelFace[] faces = TrixelUtil.makeTrixelFaces(t);
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
			TrixelFace[] faces = TrixelUtil.makeTrixelFaces(trixel);
			
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
			assertTrue(leftFaceVertices[1].getZ() == far+Trixel.SIZE);
			
			assertTrue(leftFaceVertices[2].getX() == left);
			assertTrue(leftFaceVertices[2].getY() == bottom+Trixel.SIZE);
			assertTrue(leftFaceVertices[2].getZ() == far+Trixel.SIZE);
			
			assertTrue(leftFaceVertices[3].getX() == left);
			assertTrue(leftFaceVertices[3].getY() == bottom+Trixel.SIZE);
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
			
			assertTrue(bottomFaceVertices[1].getX() == left+Trixel.SIZE);
			assertTrue(bottomFaceVertices[1].getY() == bottom);
			assertTrue(bottomFaceVertices[1].getZ() == far);
			
			assertTrue(bottomFaceVertices[2].getX() == left+Trixel.SIZE);
			assertTrue(bottomFaceVertices[2].getY() == bottom);
			assertTrue(bottomFaceVertices[2].getZ() == far+Trixel.SIZE);
			
			assertTrue(bottomFaceVertices[3].getX() == left);
			assertTrue(bottomFaceVertices[3].getY() == bottom);
			assertTrue(bottomFaceVertices[3].getZ() == far+Trixel.SIZE);
			
			// FAR
			TrixelFace farFace = faces[4];
			Point3D[] farFaceVertices = farFace.getVertices();
			// 4 vertices
			assertEquals(farFaceVertices.length, 4);
			// test if points are correct
			assertTrue(farFaceVertices[0].getX() == left);
			assertTrue(farFaceVertices[0].getY() == bottom);
			assertTrue(farFaceVertices[0].getZ() == far);
			
			assertTrue(farFaceVertices[1].getX() == left+Trixel.SIZE);
			assertTrue(farFaceVertices[1].getY() == bottom);
			assertTrue(farFaceVertices[1].getZ() == far);
			
			assertTrue(farFaceVertices[2].getX() == left+Trixel.SIZE);
			assertTrue(farFaceVertices[2].getY() == bottom+Trixel.SIZE);
			assertTrue(farFaceVertices[2].getZ() == far);
			
			assertTrue(farFaceVertices[3].getX() == left);
			assertTrue(farFaceVertices[3].getY() == bottom+Trixel.SIZE);
			assertTrue(farFaceVertices[3].getZ() == far);
		}
	}
	
	/**
	 * should make one polygon with at the correct position
	 */
	@Test public void polygon2DToTrixelstest1(){ 
		int top = 100;
		int left = 100;
		int z = 0;
		int[] xpoints = { left , left + Trixel.SIZE, left + Trixel.SIZE, left};
		int[] ypoints = { top , top,  top + Trixel.SIZE, top + Trixel.SIZE};
		Polygon poly = new Polygon(xpoints, ypoints, xpoints.length);
		List<Trixel> trixels = TrixelUtil.polygon2DToTrixels(poly, z);
		
		assertEquals(1, trixels.size());
		
		Trixel t = trixels.get(0);
		Trixition trix = t.getTrixition();
		
		assertEquals(trix.x, (int)(top/Trixel.SIZE));
		assertEquals(trix.y, (int)(left/Trixel.SIZE));
		assertEquals(trix.z, (int)(z/Trixel.SIZE));
	}
	
}
