package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.Res;
import game.world.dimensions.Point3D;
import game.world.util.Floor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class LevelMakerView extends JPanel{

	private LevelMaker levelMaker;

	public LevelMakerView(){

		Floor floor = null;
		try {
			floor = getFloorPolygon();
		} catch (NoFloorException e1) {
			System.exit(0);
		}

		// initialise GUI stuff
		WillMouseMotionListener listener = new WillMouseMotionListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		levelMaker = new LevelMaker(floor);

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		// --------- main control panel

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		add(buttonPanel, BorderLayout.NORTH);
		
		JPanel colourPanel = new JPanel();
		colourPanel.setLayout(new FlowLayout());
		buttonPanel.add(colourPanel);

		JButton chooseColourButton = new JButton("Colour");
		chooseColourButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				levelMaker.setColour(JColorChooser.showDialog(null, "choose colour", Color.WHITE));
			}
		});
		colourPanel.add(chooseColourButton);
		
		JButton setRandomColourButton = new JButton("Set colour random");
		setRandomColourButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				levelMaker.setColourToRandom();
			}
		});

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				levelMaker.writeTrixelsToFile();
			}
		});
		buttonPanel.add(saveButton);

		// -------- objects panel

		JPanel objectsPanel = new JPanel();
		objectsPanel.setLayout(new FlowLayout());
		add(objectsPanel, BorderLayout.SOUTH);

		ModeButtonListener modeButtonListener = new ModeButtonListener();

		JButton trixelButton = new JButton("Trixel");
		trixelButton.addActionListener(modeButtonListener);
		trixelButton.setActionCommand(LevelMaker.TRIXEL_MODE);

		JButton treeButton = new JButton(new ImageIcon(Res.getImageFromName("Tree")));
		treeButton.addActionListener(modeButtonListener);
		treeButton.setActionCommand(LevelMaker.TREE_MODE);

		JButton chestButton = new JButton(new ImageIcon(Res.getImageFromName("Chest")));
		chestButton.addActionListener(modeButtonListener);
		chestButton.setActionCommand(LevelMaker.CHEST_MODE);
	}

	/**
	 * @return gets a floor polygon for the level maker to use
	 * @throws NoFloorException
	 */
	private Floor getFloorPolygon() throws NoFloorException {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+File.separator+Res.FLOOR_PATH);
		final int USER_SELECTION = chooser.showOpenDialog(null);

		File floorFile;

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			floorFile =  chooser.getSelectedFile();
		}
		else throw new NoFloorException();

		return parseFloorFile(floorFile);
	}

	/**
	 * PRE: floorFile must be a valid floor file: 2 lines with equal number of integer tokens
	 * @param floorFile
	 * @return a polygon obtained from the floorFile
	 */
	private Floor parseFloorFile(File floorFile) {

		Scanner floorFileScanner = null;
		try {
			floorFileScanner = new Scanner(floorFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		floorFileScanner.nextLine(); // skip first line

		String[] xpointStrings = floorFileScanner.nextLine().split("\t");
		String[] ypointStrings = floorFileScanner.nextLine().split("\t");
		floorFileScanner.close();

		int numPoints = xpointStrings.length;

		Point3D[] points = new Point3D[numPoints];

		for (int i = 0; i < numPoints; i++){
			int x = Integer.parseInt(xpointStrings[i]);
			int z = Integer.parseInt(ypointStrings[i]);
			points[i] = new Point3D(x,0,z);
		}

		System.out.println(numPoints);

		return new Floor(points);
	}

	private void dealWithMouseDragged(int dx, int dy) {
		levelMaker.updateRotation(0, dx);
		repaint();
	}

	private void makeSomethingAt(int x, int y) {
		levelMaker.makeSomethingAt(x, y);
		repaint();
	}
	private void deleteSomethingAt(int x, int y) {
		levelMaker.deleteTrixelAt(x, y);

	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//long start = System.currentTimeMillis();
		Renderer.renderTrixels(g, levelMaker.getTrixels(), levelMaker.getLastTransform());
		/*System.out.printf("time taken to transform and draw trixels: %d\n",
				System.currentTimeMillis()-start);*/
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

			// if right-click, delete trixel at this point
			if (e.getButton() == MouseEvent.BUTTON3) {
	            deleteSomethingAt(e.getX(), e.getY());
	        }
			else { // else, draw something
				makeSomethingAt(e.getX(), e.getY());
			}
			repaint();
		}
	}

	/**
	 * Listens to buttons which change the mode of the level maker
	 *
	 * @author hardwiwill
	 */
	private class ModeButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			levelMaker.setDrawMode(e.getActionCommand());
		}
	}

}

