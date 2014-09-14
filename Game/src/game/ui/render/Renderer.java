package game.ui.render;

import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.world.dimensions.Point3D;
import game.world.model.Place;
import game.world.util.Drawable;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Renderer {

	/**
	 * @param g
	 * @param place
	 * Draws a place using Graphics parameter
	 */
	public static void renderPlace(Graphics g, Place place){
		Iterator<Drawable> drawables = place.getDrawable();
		for (Drawable drawable = drawables.next(); drawables.hasNext(); drawable = drawables.next()){
			//drawable.
		}
	}

	private List<TrixelFace> getVisibleTrixelFaces(List<Trixel> trixels){
		List<TrixelFace> faces = new ArrayList<TrixelFace>();
		for (Trixel trixel : trixels){
			
			
		}
		return faces;
	}

}
