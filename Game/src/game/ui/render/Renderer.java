package game.ui.render;

import game.ui.render.util.GameImage;
import game.ui.render.util.GamePolygon;
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

	/**
	 * Draws a place using Graphics parameter
	 * @param g
	 * @param place
	 */
	public static void renderPlace(Graphics g, Place place){
		// all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component
		Queue<ZComparable> placeObjects = new PriorityQueue<ZComparable>(50, new ZComparator());

		// convert floor into trixels
		//List<Trixel> floor = polygon2DToTrixels(place.getFloor().get, 0);

		// deal with rest of drawables
		Iterator<Drawable> drawables = place.getDrawable();

		for (Drawable drawable = drawables.next(); drawables.hasNext(); drawable = drawables.next()){
			if (isImage(drawable)){
				Dimension dimension = new Dimension((int)drawable.getBoundingBox().getWidth(),
						(int)drawable.getBoundingBox().getHeight());
				GameImage image = new GameImage(Res.getImageFromName(drawable.getImageName()), drawable.getPosition(), dimension);
				placeObjects.offer(image);
			}
		}

		// draw all objects in correct order
		// all objects are either trixel faces or images.
		while (!placeObjects.isEmpty()){
			ZComparable gameObject = placeObjects.poll();
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
	 * TODO
	 * @param trixels
	 * @return a list of trixel faces visible to current viewing direction
	 */
	private List<TrixelFace> getVisibleTrixelFaces(List<Trixel> trixels, Vector3D viewerDirection){
		List<TrixelFace> visibleFaces = new ArrayList<TrixelFace>();

		//Transform rotationTransform =

		for (Trixel trixel : trixels){
			TrixelFace[] faces = TrixelUtil.getTrixelFaces(trixel);
			for (TrixelFace face : faces){
				//if (face.shouldBeDrawn(viewerDirection))
			}
		}
		return visibleFaces;
	}

	/**
	 * makes a list of trixels to represent a 2d polygon
	 * PRE: polygon vertices must all have same z value (like a with a floor).
	 * @param poly
	 * @return list of trixels which make up the polygon
	 */
	private List<Trixel> polygon2DToTrixels(Polygon poly, float z){
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
	public static Color getRandomColour(){
		int r = (int)(Math.random()*255);
		int g = (int)(Math.random()*255);
		int b = (int)(Math.random()*255);

		return new Color(r, g, b);
	}

}
