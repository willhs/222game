package game.ui.render.able;

import game.world.dimensions.Point3D;

/**
 * @author hardwiwill
 *
 * Used for drawing text at a position
 */
public class GameText implements Renderable{

	private Point3D position;
	private String text;

	public GameText(String text, Point3D position) {
		this.position = position;
		this.text = text;
	}

	@Override
	public float getDepth() {
		return position.z;
	}

	@Override
	public void flipAroundY(int top) {
		position = position.flipY(top);
	}

	public Point3D getPosition(){
		return position;
	}

	public String getText(){
		return text;
	}

}
