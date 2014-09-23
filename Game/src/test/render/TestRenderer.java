package test.render;

import game.ui.render.Renderer;
import game.ui.render.util.Transform;
import game.ui.render.util.Vector3D;
import game.world.model.Place;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import test.world.util.SingleRoomWorldTest;

public class TestRenderer extends JPanel{

	private Place testPlace;
	private Vector3D viewerDirection;

	public TestRenderer(){
		 testPlace = new SingleRoomWorldTest().world.getPlaces().next();
		 viewerDirection = new Vector3D(0,0,1);
		 WillMouseMotionListener listener = new WillMouseMotionListener();
		 addMouseListener(listener);
		 addMouseMotionListener(listener);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		System.out.println("viewerDirection: "+viewerDirection);
		
		Renderer.renderPlace(g, testPlace, viewerDirection);
	}

	public void rotate(int dx, int dy) {
		float scalar = 50;
		Transform rotation = Transform.newYRotation(dx/scalar);//.compose(Transform.newXRotation(dy/scalar).compose(Transform.newZRotation(0)));
		viewerDirection = rotation.multiply(viewerDirection);
		repaint();
	}

 	/** For testing as a module on it's own
	 * @param args
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame();
		Dimension SCREEN_SIZE = new Dimension(600,600);
		frame.setSize(SCREEN_SIZE );
		frame.add(new TestRenderer());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			rotate(dx, dy);

			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}
}
