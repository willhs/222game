package game.ui.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author hardwiwill
 *
 * For testing rendering stuff!
 */
public class GraphicsPanel extends JPanel{

	Vector3D point = new Vector3D(20, 20, 20);
	Vector3D centre = new Vector3D(50, 50, 20);
	Vector3D viewerDir = new Vector3D(0,0,1);
	Vector3D viewerPos = new Vector3D(200, 200, 100); // half screen height and width
	
	int objSize = 50;

	public GraphicsPanel(){
		WillMouseMotionListener mouseListener = new WillMouseMotionListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Point point2d = get2DFrom3D(point, viewerDir, centre, viewerPos);
		g.fillRect(point2d.x, point2d.y ,objSize, objSize);
	}

	public void rotate(int x, int y){
		
	}

	public Point get2DFrom3D(Vector3D point, Vector3D pan, Vector3D centre, Vector3D position){
		float x = point.x + position.x;
		float y = point.y + position.y;
		float z = point.z + position.z;
		
		Vector3D temp = new Vector3D( (float) (x*Math.cos(pan.x) - z*Math.sin(pan.x)), 
				(float) (y*Math.cos(pan.y) - z*Math.sin(pan.y)), 
				(float) (x*Math.sin(pan.x) + z*Math.cos(pan.x)));
		
		
		z = (float) (temp.y*Math.cos(pan.y) - temp.z*Math.sin(pan.y));
		x = (float) (temp.x*Math.cos(pan.z) - temp.y*Math.sin(pan.z));
		y = (float) (temp.x*Math.sin(pan.z) + temp.y*Math.cos(pan.z));
		 
		
		int zoom = 1;
		//if (z > 0){
		return new Point((int) (x / z * zoom + centre.x), (int) (y / z * zoom + centre.y));
	}
	

	/**
	 * For testing as a module on it's own
	 * @param args
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame();
		Dimension SCREEN_SIZE = new Dimension(400,400);
		frame.setSize(SCREEN_SIZE );
		frame.add(new GraphicsPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public class WillMouseMotionListener implements MouseListener, MouseMotionListener {

		int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {
			
			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;
			
			rotate(dx, dy);
			
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
