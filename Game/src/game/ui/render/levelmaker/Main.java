package game.ui.render.levelmaker;

import game.ui.render.Res;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author hardwiwill
 *
 * For initialising the level maker.
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Res.readInAllCommonImages();
		JFrame frame = new JFrame("Level maker");
		Dimension SCREEN_SIZE = new Dimension(800,800);
		frame.setSize(SCREEN_SIZE );
		frame.add(new LevelMakerView());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
