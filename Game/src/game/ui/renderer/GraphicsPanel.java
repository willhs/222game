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

	private Vector3D point = new Vector3D(100, 100, 20);
	private Vector3D centre = new Vector3D(200, 200, -100);
	private Vector3D viewerDir = new Vector3D(0,0,1);
	
	private int objSize = 50;
	private int cetreSize = 10;

	public GraphicsPanel(){
		WillMouseMotionListener mouseListener = new WillMouseMotionListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// z component used for size, not display
		g.fillRect((int)point.x-(objSize/2), (int)point.y-(objSize/2) ,objSize, objSize);
		g.fillOval((int)centre.x-(cetreSize/2), (int)centre.y-(cetreSize/2), cetreSize, cetreSize);
		
		//System.out.println("Screen\nx = " + point.x + "\ny = " + point.y +"\nz = " + point.z);
	}

	
	public void rotate(int x, int y){
		float scalar = 10;
		
		Transform translate1 = Transform.newTranslation(-centre.x, -centre.y, -centre.z);
		Transform rotation = Transform.newYRotation(x/scalar).compose(Transform.newXRotation(y/scalar));
		Transform translate2 = Transform.newTranslation(centre.x, centre.y, centre.z);
		
		point = translate1.multiply(point);
		point = rotation.multiply(point);
		point = translate2.multiply(point);
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
			repaint();
			
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
