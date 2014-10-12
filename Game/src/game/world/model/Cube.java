package game.world.model;

import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.trixel.Trixition;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;

/**
 * @author Shane Brewer 300289850
 * 			& Will
 */
public class Cube extends Enviroment{

	/**
	 * So it's easier to convert between Cube and Trixel.
	 */
	private Trixition trixition;

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
    }

    public Trixition getTrixition(){
    	return trixition;
    }
}
