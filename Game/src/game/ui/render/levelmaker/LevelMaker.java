package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.util.GamePolygon;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Trixition;
import game.ui.render.util.ZComparator;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;
import game.world.util.Floor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author hardwiwill
 * Used for designing levels for the 222game
 * Provides a GUI for building a collection of trixels to define the physical layout of a level.
 * TODO: Save the level in a file for use in the game.
 */
public class LevelMaker extends JPanel{

	/**
	 * the amount in which to rotate all trixels (so that it can be updated)
	 */
	private Vector3D rotateAmounts = new Vector3D(0,0,0);
	/**
	 * all faces which have been rotated
	 */
	private List<TrixelFace> rotatedFaces;

	/**
	 * The last transform used to transform trixels.
	 * It can be inverted to find the true location of something which has already been transformed.
	 */
	private Transform lastTransform;
	/**
	 * all trixels in the level
	 */
	private List<Trixel> trixels;
	/**
	 * the center of all trixels
	 */
	private Point3D trixelsCentroid;

	public LevelMaker(){

		//System.out.println(System.getProperty("java.class.path"));

		// initialise GUI stuff
		WillMouseMotionListener listener = new WillMouseMotionListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		// initialise trixels to make up a floor.
		trixels = new ArrayList<Trixel>();

		Floor floor = makeFloor();

		for (Trixel t : TrixelUtil.polygon2DToTrixels(
				Renderer.floorToVerticalPolygon(floor), -1)){
			trixels.add(t);
		}

		// initialise lastTransform with no rotation
		lastTransform = makeTransform(new Vector3D(0,0,0));

		//intilise rotatedFaces
		rotatedFaces = new ArrayList<TrixelFace>();
		updateRotation(0, 0);
	}

	/**
	 * makes a room for use
	 * @return
	 */
	private Floor makeFloor() {
		int[] x = new int[]{100,600,600,100};
		int[] z = new int[]{100,100,600,600};

		Point3D[] points = new Point3D[x.length];
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], 0, z[i]);
		}

		return new Floor(points);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Renderer.renderTrixels(g, trixels.iterator(), lastTransform);
	}

	/**
	 * @param mouseDragX : how much to rotate in x direction in radians
	 * @param mouseDragY : ^ y direction...
	 */
	public Vector3D changeRotateAmount(int mouseDragX, int mouseDragY) {
		float rotateSpeed = 0.01f;
		return rotateAmounts.plus(
				new Vector3D(mouseDragX*rotateSpeed, mouseDragY*rotateSpeed, 0)
		);
	}

	/**
	 * Rotates trixels
	 * @param rotateX
	 * @param rotateY
	 */
	private void updateRotation(int rotateX, int rotateY){

		rotateAmounts = changeRotateAmount(rotateX, rotateY);
		Transform trans = makeTransform(rotateAmounts);

		// reset rotated trixels
		rotatedFaces = new ArrayList<TrixelFace>();

		for (Trixel trixel : trixels){
			for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel)){
				face.transform(trans);
				rotatedFaces.add(face);
			}
		}

		// sort in order of closest trixels
		Collections.sort(rotatedFaces, new ZComparator());

		lastTransform = trans;
		repaint();
	}


	/**
	 * Makes a combined transform matrix involving several factors including
	 * @return
	 */
	private Transform makeTransform(Vector3D rotateAmounts) {

		trixelsCentroid  = TrixelUtil.findTrixelsCentroid(trixels.iterator());

		Vector3D viewTranslation = Renderer.STANDARD_VIEW_TRANSLATION;

		return Renderer.makeTransform(rotateAmounts, trixelsCentroid, viewTranslation);
	}

	/**
	 * Searches whether x,y is within a trixel face.
	 * If x,y in a trixel face, make a new trixel next to that face and add it to the level.
	 * @param x
	 * @param y
	 */
	private void attemptCreateTrixel(int x, int y) {
		for (TrixelFace face : rotatedFaces){
			GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(face);
			if (facePoly.contains(x, y)){
				trixels.add(makeTrixelNextToFace(face));
				break; // add only one trixel
			}
		}
	}

	/**
	 * The trixel will be made directly next to neighbourFace.
	 *
	 * @param neighbourFace - the TrixelFace directly next to the trixel to be made
	 */
	private Trixel makeTrixelNextToFace(TrixelFace neighbourFace) {

		GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(neighbourFace);

		// find the true (unrotated) normal vector of the face by reversing the transform that was applied
		neighbourFace.transform(lastTransform.inverse());
		Point3D centroid = facePoly.getCentroid();
		Point3D newTrixelPosition = centroid.getTranslatedPoint(neighbourFace.calculateNormal());
		System.out.println("new trixel position"+newTrixelPosition);
		Trixition newTrixition = TrixelUtil.positionToTrixition(newTrixelPosition);
		return new Trixel(newTrixition, Renderer.getTrixelColour());
	}

 	/**
 	 * For testing as a module on it's own
	 * @param args
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame("Level maker");
		Dimension SCREEN_SIZE = new Dimension(800,800);
		frame.setSize(SCREEN_SIZE );
		frame.add(new LevelMaker());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			updateRotation(dx, dy);

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
			attemptCreateTrixel(e.getX(), e.getY());
		}
	}
}
