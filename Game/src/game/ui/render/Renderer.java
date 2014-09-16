package game.ui.render;

import game.ui.render.util.GameImage;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Trixition;
import game.world.dimensions.Point3D;
import game.world.model.Place;
import game.world.util.Drawable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Renderer {

	/**
	 * @param g
	 * @param place
	 * Draws a place using Graphics parameter
	 */
	public static void renderPlace(Graphics g, Place place){
		// all objects to be drawn (either trixels or 2d images).
		Queue<ZComparable> placeObjects = new PriorityQueue<ZComparable>(50, new ZComparator());

		// convert floor into trixels
		//List<Trixel> floor = polygon2DToTrixels(place.getFloor().get, 0);

		// deal with rest of drawables
		Iterator<Drawable> drawables = place.getDrawable();

		for (Drawable drawable = drawables.next(); drawables.hasNext(); drawable = drawables.next()){
			String imageName = drawable.getImageName();
			Point3D z = drawable.getPosition();
			if (isImage(drawable)){
				GameImage image = new GameImage(Res.getImageFromName(imageName), z);
				placeObjects.offer(image);
			}
		}
	}

	private static boolean isImage(Drawable drawable) {
		return Res.isImage(drawable.getImageName());
	}

	private List<TrixelFace> getVisibleTrixelFaces(List<Trixel> trixels){
		List<TrixelFace> faces = new ArrayList<TrixelFace>();
		for (Trixel trixel : trixels){

		}
		return faces;
	}

	/**
	 * @param poly
	 * @return list of trixels which make up the polygon
	 * PRE: polygon must have equal z values (like a floor).
	 */
	private List<Trixel> polygon2DToTrixels(Polygon poly, float z){
		List<Trixel> trixels = new ArrayList<Trixel>();
		Rectangle polyBounds = poly.getBounds();
		for (int x = polyBounds.x; x < polyBounds.x + polyBounds.width; x += Trixel.SIZE){
			for (int y = polyBounds.y; poly.contains(x,y); y += Trixel.SIZE){
				Trixition trixition = TrixelUtil.positionToTrixition(new Point3D(x, y, z));
				trixels.add(new Trixel(trixition, getRandomColor()));
			}
		}
		return trixels;
	}

	public static Color getRandomColor(){
		int r = (int)(Math.random()*255);
		int g = (int)(Math.random()*255);
		int b = (int)(Math.random()*255);

		return new Color(r, g, b);
	}

}
