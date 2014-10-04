package game.world.model;

import java.awt.Rectangle;

import game.world.dimensions.*;

public class Cube extends Enviroment{

    public Cube(String name, Point3D poisition) {
        super(name, poisition, new Rectangle3D(20, 20, 20));
    }

}
