
package test.render;

import game.ui.render.Renderer;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.world.dimensions.Vector3D;
import game.world.model.Exit;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Room;
import game.world.util.Floor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestRenderSingleTrixel extends JPanel{

	private Place testPlace;
	private Vector3D viewerDirection;

	public TestRenderSingleTrixel(){
		int top = 100;
		int left = 100;
		int[] xpoints = { left , left + Trixel.SIZE, left + Trixel.SIZE, left};
		int[] ypoints = { top , top,  top + Trixel.SIZE, top + Trixel.SIZE};
		//testPlace = new Room(new ArrayList<Exit>(), new ArrayList<Item>(), new Polygon(xpoints, ypoints, xpoints.length), "testRoom");
		viewerDirection = new Vector3D(0,0,1);
		WillMouseMotionListener l = new WillMouseMotionListener();
		addMouseListener(l);
		addMouseMotionListener(l);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawString("Not currently set up properly", 100, 100);
		//Renderer.renderPlace(g, testPlace);
	}

	public void rotate(int dx, int dy, int i) {
		float scalar = 100;
		Transform rotation = Transform.newYRotation(dx/scalar).compose(Transform.newXRotation(dy/scalar));
		viewerDirection = rotation.multiply(viewerDirection);
		//System.out.println("viewing direction: "+viewerDirection);
		repaint();
	}

 	/** For testing as a module on it's own
	 * @param args
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame();
		Dimension SCREEN_SIZE = new Dimension(800,800);
		frame.setSize(SCREEN_SIZE );
		frame.add(new TestRenderSingleTrixel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			rotate(dx, dy, 0);

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
