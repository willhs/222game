package game.ui.render;

import game.ui.render.util.GameImage;
import game.ui.render.util.GamePolygon;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Vector3D;
import game.ui.render.util.ZComparable;
import game.ui.render.util.ZComparator;
import game.world.dimensions.Point3D;
import game.world.model.Place;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Renderer {

	// TEMPORARY
	private static Point3D CENTER = new Point3D(200, 200, -200);
	private static Transform ISOMETRIC = Transform.newXRotation((float)(Math.PI/2)).compose(Transform.newYRotation((float)(Math.PI/2)));
	private static Transform AUTO = Transform.identity();
	private static Vector3D DEFAULT_VIEW_ANGLE = new Vector3D(0,0,1);

	/**
	 * Draws a place using Graphics parameter and viewer direction
	 * @param g
	 * @param place
	 */
	public static void renderPlace(Graphics g, Place place, Vector3D viewerDirection){
		// all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component

		float xAngle = DEFAULT_VIEW_ANGLE.xAngle(viewerDirection);
		float yAngle = DEFAULT_VIEW_ANGLE.yAngle(viewerDirection);
		Vector3D viewAngleChange = new Vector3D(xAngle, yAngle, 0);
		//Vector3D viewAngleChange = (viewerDirection);
		
		//System.out.println("view angle Change: "+viewAngleChange);
		
		
		Queue<ZComparable> toDraw = new PriorityQueue<ZComparable>(50, new ZComparator());

		for (Iterator<Drawable> iter = place.getDrawable(); iter.hasNext();){
			Drawable drawable = iter.next();
			if (isImage(drawable)){ // TODO: not be always true
				// drawable is an image
				Dimension dimension = new Dimension((int)drawable.getBoundingBox().getWidth(),
						(int)drawable.getBoundingBox().getHeight());
				GameImage image = new GameImage(Res.getImageFromName(drawable.getImageName()), drawable.getPosition(), dimension);
				//rotateImage(image, viewAngleChange);
				
						
				Transform translateToOrigin = Transform.newTranslation(-CENTER.getX(), -CENTER.getY(), -CENTER.getZ());
				Transform rotate = Transform.newXRotation(viewAngleChange.getX()).compose(
						Transform.newYRotation(viewAngleChange.getY())).compose(
								Transform.newZRotation(viewAngleChange.getZ()));
				Transform translateBack = Transform.newTranslation(CENTER.getX(), CENTER.getY(), CENTER.getZ());
				//System.out.println(viewerDirection);
				//System.out.println(point);
				image.transform(translateToOrigin);
				image.transform(rotate);
				image.transform(translateBack);
				
				toDraw.offer(image);
			}
			else {
				// drawable is trixel
				Trixel trixel = (Trixel) drawable;
				TrixelFace[] faces = TrixelUtil.getTrixelFaces(trixel);
				for (TrixelFace face : faces){
					rotateFace(face, viewAngleChange);
					if (face.isFacingViewer()){
						toDraw.offer(getGamePolygonFromTrixelFace(face));
					}
				}
			}
		}

		// convert floor into trixels and add those to toDraw
		// TODO: please rewrite/refactor this part when we can
		/*List<Trixel> floorTrixels = TrixelUtil.polygon2DToTrixels(floorToPolygon(place.getFloor()), 0);
		for (Trixel floorTrixel : floorTrixels){
			TrixelFace[] faces = TrixelUtil.getTrixelFaces(floorTrixel);
			for (TrixelFace face : faces){
				rotateFace(face, viewAngleChange);
				if (face.isFacingViewer()){
					toDraw.offer(getGamePolygonFromTrixelFace(face));
				}
			}
		}
*/
		//System.out.println("Drawing "+toDraw.size()+" shapes");

		// draw all gameObjects in correct order
		// all gameObjects are either trixel faces or images.
		while (!toDraw.isEmpty()){
			ZComparable gameObject = toDraw.poll();
			if (gameObject instanceof GameImage){
				GameImage image = (GameImage) gameObject;
				Point3D position = image.getPosition();
				Dimension dimension = image.getDimension();
				g.drawImage(image.getImage(), (int)position.getX()-dimension.width/2, (int)position.getY()-dimension.height/2, 
						dimension.width, dimension.height, null);
			}
			else if (gameObject instanceof GamePolygon){
				GamePolygon poly = (GamePolygon) gameObject;
				//System.out.println("poly box, "+poly.getBounds());
				g.setColor(poly.getColour());
				g.fillPolygon(poly);
			}
		}
		// draw center oval
		g.fillOval((int)(CENTER.x-10), (int)(CENTER.y-10), 20, 20);
	}

	/**
	 * @param face
	 * @return game polygon representing a trixel face
	 */
	private static GamePolygon getGamePolygonFromTrixelFace(TrixelFace face) {
		Point3D[] vertices = face.getVertices();
		int[] xpoints = new int[vertices.length];
		int[] ypoints = new int[vertices.length];
				
		float zTotal = 0;
		for (int i=0; i < vertices.length; i++){
			xpoints[i] = (int)vertices[i].getX();
			ypoints[i] = (int)vertices[i].getY();
			zTotal += (int)vertices[i].getZ();
		}
		return new GamePolygon(xpoints, ypoints, vertices.length, zTotal/vertices.length, face.getColour());
	}

	/**
	 * @param image
	 * @param viewerDirection
	 */
	private static void rotateImage(GameImage image, Vector3D viewerDirection) {
		Transform[] t = getRotateAroundPointTransforms(viewerDirection, CENTER);
		
	//	System.out.println("transform: "+Arrays.toString(t));
		
		image.transform(t[0]);
		image.transform(t[1]);
		image.transform(t[2]);
	}

	private static void rotateFace(TrixelFace face, Vector3D viewerDirection) {
		Transform[] t = getRotateAroundPointTransforms(viewerDirection, CENTER);
		
		face.transform(t[0]);
		face.transform(t[1]);
		face.transform(t[2]);
	}

	/**
	 * @param drawable
	 * @return whether a Drawable object should be represented as an image.
	 */
	private static boolean isImage(Drawable drawable) {
		return Res.isImage(drawable.getImageName());
	}
	
	/**
	 * @param dir
	 * @param point
	 * @return array of transforms necessary to perform rotation around the point
	 */
	public static Transform[] getRotateAroundPointTransforms(Vector3D dir, Point3D point){
		//System.out.println(dir);
		//System.out.println(point);
		Transform translateToOrigin = Transform.newTranslation(-point.getX(), -point.getY(), -point.getZ());
		Transform rotate = Transform.newYRotation(dir.getY()).compose(Transform.newXRotation(dir.getX())).compose(Transform.newZRotation(dir.getZ()));
		Transform translateBack = Transform.newTranslation(point.getX(), point.getY(), point.getZ());
		//System.out.println(dir);
		//System.out.println(point);
		return new Transform[]{ translateToOrigin, rotate, translateBack };
	}



	/**
	 * @return random colour
	 */
	public static Color getRandomColour(){
		int r = (int)(Math.random()*255);
		int g = (int)(Math.random()*255);
		int b = (int)(Math.random()*255);

		return new Color(r, g, b);
	}

	/**
	 * Temporary (hopefully)
	 * makes a java.awt.Polygon from a Floor object.
	 * @param floor
	 * @return a polygon representing the floor
	 */
	private static Polygon floorToPolygon(Floor floor){
		Point3D[] floorPoints = floor.getPoints();
		int[] xpoints = new int[floorPoints.length];
		int[] ypoints = new int[floorPoints.length];

		for ( int i = 0; i < floorPoints.length; i++){
			Point3D point = floorPoints[i];
			xpoints[i] = (int)point.getX();
			ypoints[i] = (int)point.getZ();
		}
		return new Polygon(xpoints, ypoints, floorPoints.length);
	}

}
