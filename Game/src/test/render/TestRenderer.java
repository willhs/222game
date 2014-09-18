package test.render;

import game.ui.render.Renderer;
import game.ui.render.util.Vector3D;
import game.world.model.Place;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import test.world.util.SingleRoomWorldTest;

public class TestRenderer extends JPanel{

	private Place testPlace;
	private Vector3D viewerDirection;

	public TestRenderer(){
		 testPlace = new SingleRoomWorldTest().world.getPlaces().next();
		 viewerDirection = new Vector3D(0,0,1);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Renderer.renderPlace(g, testPlace, viewerDirection);
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
}
