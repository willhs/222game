package game.ui.render;

import game.ui.render.util.GameImage;
import game.ui.render.util.GamePolygon;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Trixition;
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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Renderer {

	// TEMPORARY
	private static Point3D SCREEN_CENTER = new Point3D(300, 300, 300);

	/**
	 * Draws a place using Graphics parameter and viewer direction
	 * @param g
	 * @param place
	 */
	public static void renderPlace(Graphics g, Place place, Vector3D viewerDirection){
		// all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component

		Queue<ZComparable> toDraw = new PriorityQueue<ZComparable>(50, new ZComparator());



		for (Iterator<Drawable> iter = place.getDrawable(); iter.hasNext();){
			Drawable drawable = iter.next();
			System.out.println("next");
			if (isImage(drawable)){
				// drawable is an image
				Dimension dimension = new Dimension((int)drawable.getBoundingBox().getWidth(),
						(int)drawable.getBoundingBox().getHeight());
				GameImage image = new GameImage(Res.getImageFromName(drawable.getImageName()), drawable.getPosition(), dimension);
				toDraw.offer(image);
			}
			else {
				// drawable is trixel
				Trixel trixel = (Trixel) drawable;
				TrixelFace[] faces = TrixelUtil.getTrixelFaces(trixel);
				toDraw.addAll(getVisibleTrixelFaces(faces, viewerDirection));
			}
		}

		// convert floor into trixels and add those to toDraw
		// TODO: please rewrite/refactor this part when we can
		List<Trixel> floorTrixels = polygon2DToTrixels(floorToPolygon(place.getFloor()), 0);
		for (Trixel floorTrixel : floorTrixels){
			TrixelFace[] faces = TrixelUtil.getTrixelFaces(floorTrixel);
			toDraw.addAll(getVisibleTrixelFaces(faces, viewerDirection));
		}

		System.out.println("Drawing "+toDraw.size()+" shapes");

		// draw all gameObjects in correct order
		// all gameObjects are either trixel faces or images.
		while (!toDraw.isEmpty()){
			ZComparable gameObject = toDraw.poll();
			if (gameObject instanceof GameImage){
				GameImage image = (GameImage) gameObject;
				Point3D position = image.getPoint();
				Dimension dimension = image.getDimension();
				g.drawImage(image.getImage(), (int)position.getX(), (int)position.getY(), dimension.width, dimension.height, null);
			}
			else if (gameObject instanceof GamePolygon){
				GamePolygon poly = (GamePolygon) gameObject;
				g.setColor(poly.getColour());
				g.drawPolygon(poly);
			}
		}
	}

	/**
	 * @param drawable
	 * @return whether a Drawable object should be represented as an image.
	 */
	private static boolean isImage(Drawable drawable) {
		return Res.isImage(drawable.getImageName());
	}

	/**
	 * TODO: Optimise
	 * @param face
	 * @return a list of trixel faces visible to current viewing direction
	 */
	private static List<TrixelFace> getVisibleTrixelFaces(TrixelFace[] faces, Vector3D viewingDirection){
		List<TrixelFace> visible = new ArrayList<TrixelFace>();

		Transform translateToOrigin = Transform.newTranslation(-SCREEN_CENTER.getX(), -SCREEN_CENTER.getY(), -SCREEN_CENTER.getZ());
		Transform rotation = Transform.newYRotation(viewingDirection.getX()).compose(Transform.newXRotation(viewingDirection.getY())).compose(Transform.newZRotation(viewingDirection.getZ()));
		Transform translateBack = Transform.newTranslation(SCREEN_CENTER.getX(), SCREEN_CENTER.getY(), SCREEN_CENTER.getZ());

		for (TrixelFace face : faces){
			if (face.isFacingViewer(translateToOrigin, rotation, translateBack)){
				visible.add(face);
			}
		}
		return visible;
	}

	/**
	 * makes a list of trixels to represent a 2d polygon
	 * PRE: polygon vertices must all have same z value (like a with a floor).
	 * @param poly
	 * @return list of trixels which make up the polygon
	 */
	private static List<Trixel> polygon2DToTrixels(Polygon poly, float z){
		List<Trixel> trixels = new ArrayList<Trixel>();
		Rectangle polyBounds = poly.getBounds();
		for (int x = polyBounds.x; x < polyBounds.x + polyBounds.width; x += Trixel.SIZE){
			for (int y = polyBounds.y; poly.contains(x,y); y += Trixel.SIZE){
				Trixition trixition = TrixelUtil.positionToTrixition(new Point3D(x, y, z));
				trixels.add(new Trixel(trixition, getRandomColour()));
			}
		}
		return trixels;
	}

	/**
	 * @return random colour
	 */
	private static Color getRandomColour(){
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
			ypoints[i] = (int)point.getY();
		}
		return new Polygon(xpoints, ypoints, floorPoints.length);
	}

}
