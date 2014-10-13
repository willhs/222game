package test.render;

import game.ui.render.Renderer;
import game.ui.render.Res;
import game.world.dimensions.Vector3D;
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
	private Vector3D rotateAmounts = new Vector3D(0,0,0);

	public TestRenderer(){
		 testPlace = new SingleRoomWorldTest().world.getPlaces().next();
		 WillMouseMotionListener listener = new WillMouseMotionListener();
		 addMouseListener(listener);
		 addMouseMotionListener(listener);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Renderer.renderPlace(g, testPlace, rotateAmounts, );
	}

	/**
	 * @param mouseDragX : how much to rotate in x direction in radians
	 * @param mouseDragY : ^ y direction...
	 */
	public void rotateWithMouse(float mouseDragX, float mouseDragY) {
		float rotateSpeed = 0.01f;
		rotateAmounts = rotateAmounts.plus(
				new Vector3D(mouseDragX*rotateSpeed, mouseDragY*rotateSpeed, 0)
		);

		repaint();
	}

 	/** For testing as a module on it's own
	 * @param args
	 */
	public static void main(String[] args){
		Res.readInAllCommonImages();
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

			rotateWithMouse(dx, dy);

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
