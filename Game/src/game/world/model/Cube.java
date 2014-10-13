package game.world.model;

import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.trixel.Trixition;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

import java.awt.Color;

/**
 * @author Shane Brewer 300289850
 * 			& Will
 */
public class Cube extends Enviroment{

	public static final String FLOOR = "floor";
	public static final String NON_FLOOR = "non_floor";
	/**
	 * So it's easier to convert between Cube and Trixel.
	 */
	private Trixition trixition;
	private Color colour;

    public Cube(String name, Point3D position) {
        super(name, position, new Rectangle3D(20, 20, 20));
    }

    /**
     * The LevelMaker will use this constructor
     * so it's easier to convert between Cube and Trixel.
     *
     * @param name
     * @param position
     * @param size
     * @param trixition
     */
    public Cube(String name, Trixel trixel, int size) {
        super(name, TrixelUtil.trixitionToPosition(trixel.getTrixition(), size), new Rectangle3D(size, size, size));
        this.trixition = trixel.getTrixition();
        this.colour = trixel.getColor();
    }

    public Trixition getTrixition(){
    	return trixition;
    }

	@Override
	public String getImageName() {
		return "Cube";
	}

	@Override
	public Point3D getPosition(Place place) {
		return getPosition();
	}

	public Color getColor(){
		return colour;
	}
}
