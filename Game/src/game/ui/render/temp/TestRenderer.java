package game.ui.render.temp;

import java.awt.Dimension;

import javax.swing.JFrame;

public class TestRenderer {

	 	/** For testing as a module on it's own
		 * @param args
		 */
		public static void main(String[] args){
			JFrame frame = new JFrame();
			Dimension SCREEN_SIZE = new Dimension(400,400);
			frame.setSize(SCREEN_SIZE );
			frame.add(new RotationTest());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}

	
	
}
