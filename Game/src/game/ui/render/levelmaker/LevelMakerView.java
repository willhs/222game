package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.ImageStorage;
import game.ui.window.menus.MenuUtil;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author hardwiwill
 *
 * The view/controller element of the Level maker.
 * Deals with user-input like clicking, dragging, interacting with buttons and other GUI elements.
 * Triggers methods in the levelMaker with this input.
 * Draws the Level when it's updated.
 *
 */
public class LevelMakerView extends JPanel{

	private static final int ICON_SIZE = 30;
	private LevelMaker levelMaker;

	public LevelMakerView(){

		// initialise GUI stuff
		WillMouseMotionListener listener = new WillMouseMotionListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		// --------- main control panel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		add(controlPanel, BorderLayout.NORTH);

		// --------- main controls
		JPanel mainControls = new JPanel();
		mainControls.setLayout(new FlowLayout());
		controlPanel.add(mainControls, BorderLayout.NORTH);

		final JTextField trixelSizeField = new JTextField(LevelMaker.DEFAULT_TRIXEL_SIZE);
		trixelSizeField.setToolTipText("enter trixel size");
		trixelSizeField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				levelMaker.setTrixelSize(parseTrixelSize(trixelSizeField.getText()));
			}
		});
		mainControls.add(trixelSizeField);

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// initilise level maker
				Floor floor = null;
				try {
					floor = getFloorPolygon();
				} catch (NoFloorChosenException e1) {
					return;
				}

				levelMaker.loadFloor(floor);
				repaint();
			}
		});
		mainControls.add(loadButton);

		JButton loadRandomPolygonButton = new JButton("Load random");
		loadRandomPolygonButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				levelMaker.loadFloor(levelMaker.makeRandomFloor());
				repaint();
			}
		});
		mainControls.add(loadRandomPolygonButton);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				writeToFile(levelMaker);
			}
		});
		mainControls.add(saveButton);

		// ---------- colour panel

		JPanel colourPanel = new JPanel();
		colourPanel.setLayout(new FlowLayout());
		controlPanel.add(colourPanel, BorderLayout.SOUTH);

		JButton chooseColourButton = new JButton("Colour");
		chooseColourButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				levelMaker.setBaseColour(JColorChooser.showDialog(null, "choose colour", Color.WHITE));
				repaint();
			}
		});
		colourPanel.add(chooseColourButton);

		final JSlider colourRandomLevel = new JSlider(LevelMaker.MIN_COLOUR_DEVIATION,
				LevelMaker.MAX_COLOUR_DEVIATION,
				LevelMaker.START_COLOUR_DEVIATION);
		colourRandomLevel.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				levelMaker.setColourDeviation(colourRandomLevel.getValue());
			}
		});
		colourPanel.add(colourRandomLevel);

		JButton randomiseBaseColourButton = new JButton("Randomise base colour");
		randomiseBaseColourButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				levelMaker.randomiseBaseColour();
			}
		});
		colourPanel.add(randomiseBaseColourButton);

		// -------- drawables panel

		JPanel drawablesPanel = new JPanel();
		drawablesPanel.setLayout(new FlowLayout());
		add(drawablesPanel, BorderLayout.SOUTH);

		ModeButtonListener modeButtonListener = new ModeButtonListener();

		JButton trixelButton = new JButton("Trixel");
		trixelButton.addActionListener(modeButtonListener);
		trixelButton.setActionCommand(LevelMaker.TRIXEL_MODE);
		drawablesPanel.add(trixelButton);

		JButton treeButton = new JButton(new ImageIcon(
				MenuUtil.scale(ImageStorage.getImageFromName("Tree"), ICON_SIZE, ICON_SIZE)));
		treeButton.addActionListener(modeButtonListener);
		treeButton.setActionCommand(LevelMaker.TREE_MODE);
		drawablesPanel.add(treeButton);

		JButton chestButton = new JButton(new ImageIcon(
				MenuUtil.scale(ImageStorage.getImageFromName("Chest"), ICON_SIZE, ICON_SIZE)));
		chestButton.addActionListener(modeButtonListener);
		chestButton.setActionCommand(LevelMaker.CHEST_MODE);
		drawablesPanel.add(chestButton);

		// initialise level maker
		levelMaker = new LevelMaker();
	}

	/**
	 * @return gets a floor polygon for the level maker to use
	 * @throws NoFloorChosenException
	 */
	private Floor getFloorPolygon() throws NoFloorChosenException {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+File.separator+ImageStorage.FLOOR_PATH);
		final int USER_SELECTION = chooser.showOpenDialog(null);

		File floorFile;

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			floorFile =  chooser.getSelectedFile();
		}
		else throw new NoFloorChosenException();

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

		return new Floor(points);
	}

	/**
	 * Parses text for trixel size.
	 * If not valid trixel size, return default trixel size.
	 *
	 * @param text
	 * @return a trixel size parsed from text
	 */
	private int parseTrixelSize(String text) {
		int size = 0;
		try{
			size = Integer.parseInt(text);
		} catch (NumberFormatException e){
			size = LevelMaker.DEFAULT_TRIXEL_SIZE;
		}
		return size;
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Renderer.renderLevel(g, levelMaker.getCreatedTrixels(), levelMaker.getFloorTrixels(),
				levelMaker.getTrixelSize(), levelMaker.getWorldObjects(), levelMaker.getLastTransform());

		// displays current colour
		g.setColor(levelMaker.getTrixelColour());
		g.fillRect(0, 50, 20, 20);

	}

	/**
	 * Writes a level to a file.
	 * @param levelMaker
	 */
	private void writeToFile(LevelMaker levelMaker) {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+ImageStorage.LEVELS_PATH);
		final int USER_SELECTION = chooser.showSaveDialog(null);

		File fileToSave;

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			fileToSave =  chooser.getSelectedFile();
		} else return;

		// Set up file writer
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileToSave, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		writer.println(levelMaker);
		writer.close();
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			levelMaker.updateRotation(0, dx);
			repaint();

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
	            levelMaker.deleteTrixelAt(e.getX(), e.getY());
	        }
			else { // else, draw something
				levelMaker.makeSomethingAt(e.getX(), e.getY());
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

