package game.world.model;

import java.awt.Rectangle;

import game.world.dimensions.*;

/**
 * @author Shane Brewer 300289850
 */
public class Cube extends Enviroment{

    public Cube(String name, Point3D poisition) {
        super(name, poisition, new Rectangle3D(20, 20, 20));
    }

    public Cube(String name, Point3D poisition, float size) {
        super(name, poisition, new Rectangle3D(size, size, size));
    }
}
