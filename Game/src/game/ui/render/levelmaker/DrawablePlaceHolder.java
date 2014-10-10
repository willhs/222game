package game.ui.render.levelmaker;

import game.ui.render.util.DepthComparable;
import game.ui.render.util.Transform;
import game.ui.render.util.Transformable;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Place;
import game.world.util.Drawable;

import java.awt.Polygon;
import java.util.Arrays;

/**
 * @author hardwiwill
 * Placeholder for a drawable.
 * Made for hacky reasons: need a transformable drawable with a link back to drawable
 */
public class DrawablePlaceHolder implements Drawable, DepthComparable, Transformable{

	private Drawable link;
	private Point3D position;

	public DrawablePlaceHolder(Drawable link, Point3D pos){
		this.link = link;
		this.position = pos;
	}

	@Override
	public String getName() {
		return link.getName();
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return link.getBoundingBox();
	}

	@Override
	public String getImageName() {
		return link.getImageName();
	}

	@Override
	public Point3D getPosition(Place place) {
		return link.getPosition(place);
	}

	@Override
	public float getDepth() {
		return link.getPosition().getZ();
	}

	public Drawable getDrawable(){
		return link;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if the point at x, y is inside the this shape
	 */
	public boolean pointIsIn(int x, int y){
		Point3D pos = getPosition();
		Rectangle3D box = getBoundingBox();

		// the following are the vertices of where game image will be drawn
		// copy-pasted from GameImage section of Renderer.drawThings
		// TODO redo this somehow in future

		int[] xpoints = new int[]{
				(int)(pos.x-box.width/2),
				(int)(pos.x+box.width/2),
				(int)(pos.x+box.width/2),
				(int)(pos.x-box.width/2)};
		int[] ypoints = new int[]{
				(int)(pos.y + box.length/2 - box.height),
				(int)(pos.y + box.length/2 - box.height),
				(int)(pos.y + box.length/2),
				(int)(pos.y + box.length/2)};

		System.out.println(Arrays.toString(xpoints)+"\n"+Arrays.toString(ypoints));

		return new Polygon(xpoints, ypoints, xpoints.length).contains(x, y);
	}

	@Override
	public String toString(){
		return link.toString();
	}

	@Override
	public void transform(Transform transform) {
		position = transform.multiply(position);
	}

}
