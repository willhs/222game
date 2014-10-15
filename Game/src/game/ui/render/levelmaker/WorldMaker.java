package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.Res;
import game.ui.window.menus.MenuUtil;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;
import game.world.model.Exit;
import game.world.model.Place;
import game.world.model.Portal;
import game.world.model.LockedPortal;
import game.world.model.World;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author hardwiwill
 *
 * The view/controller element of the World maker.
 * Deals with user-input like clicking, dragging, interacting with buttons and other GUI elements.
 * Triggers methods in the levelMaker with this input.
 * Draws the Level when it's updated.
 * Allows user to create new places.
 *
 */
public class WorldMaker extends JPanel{

	private static final int ICON_SIZE = 30;
	private static final int TEXT_FIELD_SIZE = 20;
	private JTabbedPane levelTabsPane;
	private int numTabs = 1;

	/**
	 *	Initialises GUI elements.
	 */
	public WorldMaker(){

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
//		loadButton.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				World world = null;
//				try {
//					//world = browseForWorld();
//				} catch (NoFileChosenException e1) {
//					return;
//				}
//
//				loadWorld(world);
//			}
//		});
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
					lps.add((LevelPanel)levelTabsPane.getComponentAt(i));
					System.out.println(levelTabsPane.getComponentAt(i));
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

		final JSlider colourRandomLevel = new JSlider(PlaceMaker.MIN_COLOUR_DEVIATION,
				PlaceMaker.MAX_COLOUR_DEVIATION,
				PlaceMaker.START_COLOUR_DEVIATION);
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
				repaint();
			}
		});
		colourPanel.add(randomiseBaseColourButton);

		// -------- drawables panel

		JPanel drawablesPanel = new JPanel();
		drawablesPanel.setLayout(new FlowLayout());
		add(drawablesPanel, BorderLayout.SOUTH);

		ModeButtonListener mbl = new ModeButtonListener();
		for(String mode : PlaceMaker.MODES){
			drawablesPanel.add(makeDrawButton(mode, mbl));
		}

		levelTabsPane = new JTabbedPane();
		String name = "Room1";
		levelTabsPane.addTab(name, null, makeNewDrawPanel(name), name);
		add(levelTabsPane, BorderLayout.CENTER);

		JButton newLevelButton = new JButton("New Level");
		newLevelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = "Room" + ++numTabs;
				levelTabsPane.addTab(name, null, makeNewDrawPanel(name), name);
				getCurrentDrawing().repaint();
			}
		});
		mainControls.add(newLevelButton);
	}

	/**
	 * Make a JButton with the given mode, its associated image as an icon, and the given listener linked.
	 * @return JButton for the given mode.
	 */
	private JButton makeDrawButton(String mode, ModeButtonListener mbl){
		System.out.println("mode: "+mode);
		JButton button = new JButton(new ImageIcon(
			MenuUtil.scale(Res.getImageFromName(mode), ICON_SIZE, ICON_SIZE)));
		button.addActionListener(mbl);
		button.setActionCommand(mode);
		return button;
	}

	/**
	 * Get the level maker of the currently selected tab.
	 * @return The LevelMaker object associated with the current tab.
	 */
	private PlaceMaker getCurrentLevelMaker(){
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
				Renderer.renderPlace(g, levelMaker.getCreatedTrixels(), levelMaker.getFloorTrixels(),
						levelMaker.getTrixelSize(), levelMaker.getWorldObjects(), levelMaker.getLastTransform());

				// displays current colour
				g.setColor(levelMaker.getTrixelColour());
				g.fillRect(0, 0, 20, 20);
			}
		};

		MakerMouseMotionListener listener = new MakerMouseMotionListener();
		draw.addMouseListener(listener);
		draw.addMouseMotionListener(listener);
		draw.setBackground(Color.BLACK);
		draw.getLevelMaker().setName(name);

		return draw;
	}

	private World browseForWorld() throws NoFileChosenException {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+File.separator+Res.FLOOR_PATH);
		final int USER_SELECTION = chooser.showOpenDialog(null);

		File worldFile;

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			worldFile =  chooser.getSelectedFile();
		}
		else throw new NoFileChosenException();

		return parseWorld(worldFile);
	}
	/**
	 * Parses a world file a makes a World object
	 * @param worldFile
	 * @return
	 */

	public static World parseWorld(File worldFile){
		World world = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(worldFile)));
			world = (World)ois.readObject();
		}catch(ClassNotFoundException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}
		return world;
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
			size = PlaceMaker.DEFAULT_TRIXEL_SIZE;
		}
		return size;
	}



	/**
	 * Writes a level to a file.
	 * @param levelMaker
	 */
	private void writeToFile(List<LevelPanel> lps) {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+File.separator+Res.WORLDS_PATH);
		final int USER_SELECTION = chooser.showSaveDialog(null);

		File fileToSave;
		List<Exit> portals = new ArrayList<Exit>();

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			fileToSave =  chooser.getSelectedFile();
		} else return;

		// Set up file writer
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fileToSave));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, Place> places = new HashMap<String, Place>();
		for(LevelPanel lp : lps){
			places.put(lp.levelMaker.name, lp.levelMaker.toPlace());
		}
		for(Map.Entry<String, Place> m : places.entrySet()){
			System.out.println(m.getKey() + " ::: " + m.getValue());
		}

		for(PlaceMaker.SimplePortal sp : PlaceMaker.getPortals()){
			Place p = places.get(sp.lm.name);
			Exit portal;
			if(sp.locked){
				portal = new LockedPortal(sp.getName(), places.get(sp.lm.name), sp.location,
                                                 places.get(sp.toPortal.lm.name), sp.toPortal.location);
			}else{
				portal = new Portal(sp.getName(), places.get(sp.lm.name), sp.location,
                                                 places.get(sp.toPortal.lm.name), sp.toPortal.location);
			}
			p.addExit(portal);
			portals.add(portal);
		}

		List<Place> placesList = new ArrayList<Place>();
		for(Map.Entry<String, Place> m : places.entrySet()){
			placesList.add(m.getValue());
		}

		World newWorld = new World(placesList);
		for(Exit p : portals){
			newWorld.addExit(p);
		}

		try{
			oos.writeObject(newWorld);
			oos.close();
		}catch(IOException e){
			System.err.println("Writing failed.  Exception was : " + e);
		}
	}

	/**
	 * Loads a world into the WorldMaker.
	 * @param world
	 */
	private void loadWorld(World world){
		levelTabsPane.removeAll();
		numTabs = 0;
		Place place;
		List<Exit> portals = new ArrayList<Exit>();
		HashMap<String, PlaceMaker> placeMakers = new HashMap<String, PlaceMaker>();

		for(Iterator<Place> places = world.getPlaces(); places.hasNext();){
			numTabs++;
			place = places.next();

			String name = place.getName();
			LevelPanel lp = makeNewDrawPanel(name);
			levelTabsPane.addTab(name, null, lp, name);
			lp.levelMaker.loadPlace(place);

			placeMakers.put(name, lp.levelMaker);
			for(Iterator<Exit> exits = place.getExits(); exits.hasNext();){
				portals.add(exits.next());
			}
		}

		for(Exit portal : portals){
			Place from = portal.getConnectedPlaces().get(0).place;
			Place to = portal.getConnectedPlaces().get(1).place;

			Point3D fromPos = portal.getConnectedPlaces().get(0).position;
			Point3D toPos = portal.getConnectedPlaces().get(1).position;

			PlaceMaker fromLp = placeMakers.get(from.getName());
			PlaceMaker toLp = placeMakers.get(to.getName());

			boolean locked = portal instanceof LockedPortal;
			PlaceMaker.SimplePortal fromPortal = fromLp.new SimplePortal(fromLp, fromPos, null, locked);
			PlaceMaker.SimplePortal toPortal = toLp.new SimplePortal(toLp, toPos, fromPortal, locked);
			fromPortal.toPortal = toPortal;

			fromLp.addPortal(fromPortal);
			toLp.addPortal(toPortal);
		}
	}

	/**
	 * @author hardwiwill
	 * Listens for mouse events for the WorldMaker.
	 */
	public class MakerMouseMotionListener extends MouseAdapter{

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
			PlaceMaker lm = getCurrentLevelMaker();
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
			PlaceMaker.setDrawMode(e.getActionCommand());
		}
	}

	class LevelPanel extends JPanel{
		protected PlaceMaker levelMaker = new PlaceMaker();

		public PlaceMaker getLevelMaker(){
			return levelMaker;
		}
	}

}

