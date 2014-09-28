package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;
import game.world.model.Exit;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Room;
import game.world.model.Table;
import game.world.util.Floor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import test.render.TestRenderer;

public class LevelMakerGUI extends JPanel{

	private Place place;
	private Vector3D rotateAmounts = new Vector3D(0,0,0);;
	private TrixelGrid grid;
	private List<TrixelFace> rotatedTrixels;

	public LevelMakerGUI(){
		WillMouseMotionListener listener = new WillMouseMotionListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		place = makeRoom();

		Polygon vertFloorPoly = Renderer.floorToVerticalPolygon(place.getFloor());
		Rectangle floorBounds = vertFloorPoly.getBounds();
		int placeHeight = 500;

		grid = new TrixelGrid(floorBounds.width, placeHeight, floorBounds.height);
		rotatedTrixels = new ArrayList<TrixelFace>();
	}

	/**
	 * makes a room given a floor
	 * @return
	 */
	private Room makeRoom() {
		int[] xpoints = new int[]{200,800,600,100};
		int[] ypoints = new int[]{200,600,400,400};

		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		List<Item> items = new ArrayList<Item>();
		items.add(new Table("Table1", new Point3D(250, 0, 250), new Rectangle3D(50, 50, 50)));
		return new Room(new ArrayList<Exit>(), items, p, "Room1");
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Renderer.renderPlace(g, place, rotateAmounts);
	}

	/**
	 * @param mouseDragX : how much to rotate in x direction in radians
	 * @param mouseDragY : ^ y direction...
	 */
	public Vector3D changeRotateAmount(int mouseDragX, int mouseDragY) {
		float rotateSpeed = 0.01f;
		return rotateAmounts.plus(
				new Vector3D(mouseDragX*rotateSpeed , mouseDragY*rotateSpeed , 0)
		);
	}

	/**
	 * @param mouseDragX
	 * @param mouseDragY
	 */
	private void onMouseDragged(int mouseDragX, int mouseDragY){

		rotateAmounts = changeRotateAmount(mouseDragX, mouseDragY);

		// reset rotated trixels
		Floor floor = place.getFloor();
		rotatedTrixels.clear();
		for (Trixel t : TrixelUtil.polygon2DToTrixels(
				Renderer.floorToVerticalPolygon(floor), -1)){
			//rotatedTrixels.add(TrixelUtil.makeTrixelFaces(t));
		}


		Point3D floorCentroid = Renderer.getFloorCentroid(floor);

		Vector3D viewTranslation = new Vector3D(0, 200, 0);
		Transform trans = Renderer.makeTransform(rotateAmounts, floorCentroid, viewTranslation);

		for (int i = 0; i < rotatedTrixels.size(); i++){

		}

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

			onMouseDragged(dx, dy);

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
