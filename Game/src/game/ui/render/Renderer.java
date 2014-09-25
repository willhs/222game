package game.ui.render;

import game.ui.render.util.GameImage;
import game.ui.render.util.GamePolygon;
import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Vector3D;
import game.ui.render.util.Renderable;
import game.ui.render.util.ZComparator;
import game.world.dimensions.Point3D;
import game.world.model.Place;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Renderer {

	// TEMPORARY
	private static Point3D CENTER = new Point3D(300, 300, 0);
	private static Transform ISOMETRIC_ROTATION = Transform.newXRotation((float)(Math.PI/4)).compose(Transform.newYRotation((float)(Math.PI/4)));
	private static Transform AUTO = Transform.identity();
	private static Vector3D DEFAULT_VIEW_ANGLE = new Vector3D(0,0,1);

	private static final int FRAME_TOP = 600;

	/**
	 * Draws a place using Graphics parameter and viewer direction
	 * @param g
	 * @param place
	 */
	public static void renderPlace(Graphics g, Place place){

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component

		Queue<Renderable> toDraw = new PriorityQueue<Renderable>(50, new ZComparator());

		for (Iterator<Drawable> iter = place.getDrawable(); iter.hasNext();){
			Drawable drawable = iter.next();
			if (isImage(drawable)){ // TODO: not be always true
				// drawable is an image
				Dimension dimension = new Dimension((int)drawable.getBoundingBox().getWidth(),
						(int)drawable.getBoundingBox().getHeight());
				GameImage image = new GameImage(Res.getImageFromName(drawable.getImageName()), drawable.getPosition(), dimension);
				rotate(image, ISOMETRIC_ROTATION);

				toDraw.offer(image);
			}
			else {
				// drawable is made of trixels or is a trixel
				Trixel trixel = (Trixel) drawable;
				TrixelFace[] faces = TrixelUtil.getTrixelFaces(trixel);
				for (TrixelFace face : faces){
					rotate(face, ISOMETRIC_ROTATION);
					if (face.isFacingViewer()){
						toDraw.offer(getGamePolygonFromTrixelFace(face));
					}
				}
			}
		}

		// convert floor into trixels and add those to toDraw
		// TODO: please rewrite/refactor this part when we can
		Polygon floorPolygon = floorToPolygon(place.getFloor());

		List<Trixel> floorTrixels = TrixelUtil.polygon2DToTrixels(floorPolygon, 0);
		for (Trixel floorTrixel : floorTrixels){
			TrixelFace[] faces = TrixelUtil.getTrixelFaces(floorTrixel);
			for (TrixelFace face : faces){
				//translate(face, new Vector3D(200, -100, 0));
				// rotate the floor so that it's on the ground (rather than on the wall)
				rotate(face, Transform.newXRotation((float)(Math.PI/2)));
				rotate(face, ISOMETRIC_ROTATION);
				if (face.isFacingViewer()){
					toDraw.offer(getGamePolygonFromTrixelFace(face));
				}
			}
		}

		// ------- FLIP Y VALUES OF ALL THINGS
		for (Renderable shape : toDraw){
			shape.flipY(FRAME_TOP);
		}

		//System.out.println("Drawing "+toDraw.size()+" shapes");

		//  ----- DRAW ALL THE THINGS
		//  ...in correct order
		// all gameObjects are either trixel faces or images.
		while (!toDraw.isEmpty()){
			Renderable renderObject = toDraw.poll();
			if (renderObject instanceof GameImage){
				GameImage image = (GameImage) renderObject;
				Point3D position = image.getPosition();
				Dimension dimension = image.getDimension();
				g2.drawImage(image.getImage(), (int)position.getX()-dimension.width/2, (int)position.getY()-dimension.height/2,
						dimension.width, dimension.height, null);
			}
			else if (renderObject instanceof GamePolygon){
				GamePolygon poly = (GamePolygon) renderObject;
				g2.setColor(poly.getColour());
				g2.fillPolygon(poly);
			}
		}
		// draw center oval
		g2.fillOval((int)(CENTER.x-10), (int)(CENTER.y-10), 20, 20);
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
	 * rotates a transformable object around a point given a viewer direction
	 * @param object
	 * @param viewerDirection
	 */
	private static void rotate(Transformable object, Transform rotate) {
		Transform[] t = getTranslationAroundPointTransforms(CENTER);

		object.transform(t[0]);
		object.transform(rotate);
		object.transform(t[1]);
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
		Transform translateToOrigin = Transform.newTranslation(-point.getX(), -point.getY(), -point.getZ());
		Transform rotate = Transform.newYRotation(dir.getY()).compose(Transform.newXRotation(dir.getX())).compose(Transform.newZRotation(dir.getZ()));
		Transform translateBack = Transform.newTranslation(point.getX(), point.getY(), point.getZ());
		return new Transform[]{ translateToOrigin, rotate, translateBack };
	}

	/**
	 * @param point
	 * @return two transforms, one to translate away from point, then one to translate back
	 */
	private static Transform[] getTranslationAroundPointTransforms(Point3D point){
		Transform translateToOrigin = Transform.newTranslation(-point.getX(), -point.getY(), -point.getZ());
		Transform translateBack = Transform.newTranslation(point.getX(), point.getY(), point.getZ());
		return new Transform[]{ translateToOrigin, translateBack };
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
			System.out.println(point);
		}
		return new Polygon(xpoints, ypoints, floorPoints.length);
	}

}