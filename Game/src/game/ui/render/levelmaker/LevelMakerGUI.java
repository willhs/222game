package game.ui.render.levelmaker;

import game.ui.render.Renderer;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class LevelMakerGUI extends JPanel{

	private LevelMaker levelMaker;

	public LevelMakerGUI(){

		// initialise GUI stuff
		WillMouseMotionListener listener = new WillMouseMotionListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		levelMaker = new LevelMaker();
	}

	private void dealWithMouseDragged(int dx, int dy) {
		levelMaker.updateRotation(dx, dy);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		long start = System.currentTimeMillis();
		Renderer.renderTrixels(g, levelMaker.getTrixels(), levelMaker.getLastTransform());
		System.out.printf("time taken to transform and draw trixels: %d\n",
				System.currentTimeMillis()-start);
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			dealWithMouseDragged(dx, dy);

			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseClicked(MouseEvent e){
			levelMaker.attemptCreateTrixel(e.getX(), e.getY());
			repaint();
		}
	}

}
