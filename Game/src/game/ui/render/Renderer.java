package game.ui.render;

import game.world.util.GameImage;
import game.world.util.Trixel;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.List;

public class Renderer {

	private List<GameImage> images;
	private List<Trixel> trixels;

	public void setGraphics(List<GameImage> images, List<Trixel> trixels){
		this.images = images;
		this.trixels = trixels;
	}

/*	public BufferedImage renderWorld(Graphics g){

	}

	private List<Polygon> getPolysFromTrixels(List<Trixel> trixels){

	}*/

}
