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
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

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
	private static final int TEXT_FIELD_SIZE = 20;
	private JTabbedPane levelTabsPane;
	private int numTabs = 1;
	private JTextField roomNameField;
	private JTextField portalNameField;

	public LevelMakerView(){

		// initialise GUI stuff

		setLayout(new BorderLayout());

		// --------- main control panel

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		add(controlPanel, BorderLayout.NORTH);

		// --------- main controls
		JPanel mainControls = new JPanel();
		mainControls.setLayout(new FlowLayout());
		controlPanel.add(mainControls, BorderLayout.NORTH);

		final JTextField trixelSizeField = new JTextField(TEXT_FIELD_SIZE);
		trixelSizeField.setToolTipText("enter trixel size");
		trixelSizeField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getCurrentLevelMaker().setTrixelSize(parseTrixelSize(trixelSizeField.getText()));
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

				getCurrentLevelMaker().loadFloor(floor);
				getCurrentDrawing().repaint();
			}
		});
		mainControls.add(loadButton);

		JButton loadRandomPolygonButton = new JButton("Load random");
		loadRandomPolygonButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getCurrentLevelMaker().loadFloor(getCurrentLevelMaker().makeRandomFloor());
				getCurrentDrawing().repaint();
			}
		});
		mainControls.add(loadRandomPolygonButton);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				List<LevelPanel> lps = new ArrayList<LevelPanel>();
				for(int i = 0; i<numTabs; i++){
					lps.add((LevelPanel)levelTabsPane.getTabComponentAt(i));
				}
				writeToFile(lps);
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
				getCurrentLevelMaker().setBaseColour(JColorChooser.showDialog(null, "choose colour", Color.WHITE));
				getCurrentDrawing().repaint();
			}
		});
		colourPanel.add(chooseColourButton);

		final JSlider colourRandomLevel = new JSlider(LevelMaker.MIN_COLOUR_DEVIATION,
				LevelMaker.MAX_COLOUR_DEVIATION,
				LevelMaker.START_COLOUR_DEVIATION);
		colourRandomLevel.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				getCurrentLevelMaker().setColourDeviation(colourRandomLevel.getValue());
			}
		});
		colourPanel.add(colourRandomLevel);

		JButton randomiseBaseColourButton = new JButton("Randomise base colour");
		randomiseBaseColourButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getCurrentLevelMaker().randomiseBaseColour();
			}
		});
		colourPanel.add(randomiseBaseColourButton);

		// -------- drawables panel

		JPanel drawablesPanel = new JPanel();
		drawablesPanel.setLayout(new FlowLayout());
		add(drawablesPanel, BorderLayout.SOUTH);

		ModeButtonListener mbl = new ModeButtonListener();
		for(String mode : LevelMaker.MODES){
			drawablesPanel.add(makeDrawButton(mode, mbl));
		}

		levelTabsPane = new JTabbedPane();
		String name = "Room 1";
		levelTabsPane.addTab(name, null, makeNewDrawPanel(name), name);
		add(levelTabsPane, BorderLayout.CENTER);

		JButton newLevelButton = new JButton("New Level");
		newLevelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = "Room " + ++numTabs;
				levelTabsPane.addTab(name, null, makeNewDrawPanel(name), name);
				getCurrentDrawing().repaint();
			}
		});
		mainControls.add(newLevelButton);
	}

	/*
	 * Make a JButton with the given mode, its associated image as an icon, and the given listener linked.
	 * @return JButton for the given mode.
	 */
	private JButton makeDrawButton(String mode, ModeButtonListener mbl){
		JButton button = new JButton(new ImageIcon(
			MenuUtil.scale(ImageStorage.getImageFromName(mode), ICON_SIZE, ICON_SIZE)));
		button.addActionListener(mbl);
		button.setActionCommand(mode);
		return button;
	}

	/**
	 * Get the level maker of the currently selected tab.
	 * @return The LevelMaker object associated with the current tab.
	 */
	private LevelMaker getCurrentLevelMaker(){
		return ((LevelPanel)getCurrentDrawing()).getLevelMaker();
	}

	/**
	 * Get the JPanel object associated with the current tab
	 * @return The JPanel of the current tab
	 */
	private LevelPanel getCurrentDrawing(){
		return (LevelPanel)(levelTabsPane.getSelectedComponent());
	}

	/**
	 * Make a new JPanel with a LevelMaker for a new tab.
	 * @return the new JPanel ready to be added to a new tab.
	 */
	private LevelPanel makeNewDrawPanel(String name){
		LevelPanel draw = new LevelPanel(){
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				Renderer.renderLevel(g, levelMaker.getCreatedTrixels(), levelMaker.getFloorTrixels(),
						levelMaker.getTrixelSize(), levelMaker.getWorldObjects(), levelMaker.getLastTransform());

				// displays current colour
				g.setColor(levelMaker.getTrixelColour());
				g.fillRect(0, 0, 20, 20);
			}
		};

		WillMouseMotionListener listener = new WillMouseMotionListener();
		draw.addMouseListener(listener);
		draw.addMouseMotionListener(listener);
		draw.setBackground(Color.BLACK);
		draw.getLevelMaker().name = name;

		return draw;
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



	/**
	 * Writes a level to a file.
	 * @param levelMaker
	 */
	private void writeToFile(List<LevelPanel> lps) {
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

		for(LevelPanel lp : lps){
			//This is where we do literally everything
			//1. Make levels from the LevelPanels, not including the portals
			//2. Make Portal objects using the places and SimplePortals
			//3. Somehow add the portals to the new places
			//4. Write all places to file.
		}

		//writer.println(levelMaker);
		writer.close();
	}

	public class WillMouseMotionListener extends MouseAdapter{

		private int mouseX, mouseY;

		@Override
		public void mouseDragged(MouseEvent e) {

			int dx = e.getX()-mouseX;
			int dy = e.getY()-mouseY;

			getCurrentLevelMaker().updateRotation(0, dx);
			getCurrentDrawing().repaint();

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
			LevelMaker lm = getCurrentLevelMaker();
			LevelPanel lp = getCurrentDrawing();

			// if right-click, delete trixel at this point
			if (e.getButton() == MouseEvent.BUTTON3) {
	            lm.deleteSomethingAt(e.getX(), e.getY());
	        }
			else { // else, draw something
				lm.makeSomethingAt(e.getX(), e.getY());
			}

			lp.repaint();
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
			LevelMaker.setDrawMode(e.getActionCommand());
		}
	}

	class LevelPanel extends JPanel{
		protected LevelMaker levelMaker = new LevelMaker();

		public LevelMaker getLevelMaker(){
			return levelMaker;
		}
	}

}

