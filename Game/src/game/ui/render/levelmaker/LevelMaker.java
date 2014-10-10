package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.ImageStorage;
import game.ui.render.able.GamePolygon;
import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelFace;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.util.DepthComparator;
import game.ui.render.util.Transform;
import game.ui.window.GameWindow;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;
import game.world.model.Chest;
import game.world.model.Cube;
import game.world.model.Inventory;
import game.world.model.Place;
import game.world.model.Table;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

/**
 * @author hardwiwill
 *
 * Used for designing levels for the 222game.
 * Starts with a flat plane of trixels, made with a floor object.
 * This can be expanded on using any of the tools.
 * View of the trixels can be rotated.
 *
 * TODO: Save the level in a file for use in the game.
 */
public class LevelMaker{

	public static final String TREE_MODE = "Tree";
	public static final String DOOR_MODE = "Door";
	public static final String CHEST_MODE = "Chest";
	public static final String TRIXEL_MODE = "Trixel";
	public static final String[] MODES = {TREE_MODE, DOOR_MODE, CHEST_MODE, TRIXEL_MODE};

	public static final int MIN_COLOUR_DEVIATION = 0;
	public static final int MAX_COLOUR_DEVIATION = 100;
	public static final int START_COLOUR_DEVIATION = 20;
	public static final int DEFAULT_TRIXEL_SIZE = Trixel.DEFAULT_SIZE;
	private static final float VERSION_NUMBER = 1.0f;
	/**
	 * the amount in which to rotate the level/place (so that it can be updated)
	 */
	private Vector3D rotateAmounts = new Vector3D(0,0,0);
	/**
	 * all faces which have been rotated
	 */
	private List<TrixelFace> rotatedFaces;

	/**
	 * The last transform used to transform the level/place.
	 * It can be inverted to find the true location of something which has already been transformed.
	 */
	private Transform lastTransform;
	/**
	 * all trixels in the level except the floor :(
	 */
	private Set<Trixel> createdTrixels;
	/**
	 * all trixels which make up the floor.
	 */
	private Set<Trixel> floorTrixels;
	/**
	 * all non-trixel objects in the level.
	 */
	private Set<Drawable> drawables;
	/**
	 * the center of the floor
	 */
	private Point3D floorCentroid;
	/**
	 * the colour that will be used to make the next trixel
	 */
	private Color baseColour;

	private int flipY = GameWindow.FRAME_HEIGHT;
	/**
	 * the drawing mode. Can be TRIXEL_MODE, TREE_MODE, CHEST_MODE
	 */
	private String drawMode;
	/**
	 * how much to deviate from the base colour when making the next colour
	 */
	private int randomColourDeviation;
	/**
	 * size of trixels to be made
	 */
	private int trixelSize;

	/**
	 * Initialises LevelMaker's fields
	 */
	public LevelMaker(){

		// initialise trixels
		createdTrixels = new HashSet<Trixel>();
		floorTrixels = new HashSet<Trixel>();
		floorCentroid = new Point3D(0,0,0);
		trixelSize = DEFAULT_TRIXEL_SIZE;

		// intialise colour
		randomiseBaseColour();
		randomColourDeviation = START_COLOUR_DEVIATION;

		// initialise draw mode
		drawMode = TRIXEL_MODE;

		// initialise lastTransform with no rotation
		lastTransform = makeTransform(new Vector3D(0,0,0));

		//intilise rotatedFaces
		rotatedFaces = new ArrayList<TrixelFace>();
		updateRotation(0, 0);

		// initialise worldObjects
		drawables = new HashSet<Drawable>();
	}

	/**
	 * Loads a floor into this level maker
	 * The floor is used to generate a starting set of trixels,
	 * which is then expanded on using the tools of the level maker.
	 *
	 * @param floor
	 */
	public void loadFloor(Floor floor){
		clearLevel();

		// initilise trixels
		for (Trixel t : TrixelUtil.polygon2DToTrixels(
			Renderer.floorToVerticalPolygon(floor), trixelSize, -trixelSize)){
			t.setColour(getTrixelColour());
			floorTrixels.add(t);
		}
		floorCentroid = TrixelUtil.findTrixelsCentroid(floorTrixels.iterator(), trixelSize);
		updateFaces();
	}

	/**
	 * @param rotateX : how much to rotate in x direction in radians
	 * @param rotateY : ^ y direction...
	 */
	public Vector3D getNewRotateAmount(int rotateX, int rotateY) {
		float rotateSpeed = 0.01f;
		return rotateAmounts.plus(
				new Vector3D(rotateX*rotateSpeed, rotateY*rotateSpeed, 0)
		);
	}

	/**
	 * Rotates trixels
	 * @param rotateX
	 * @param rotateY
	 */
	public void updateRotation(int rotateX, int rotateY){

		rotateAmounts = getNewRotateAmount(rotateX, rotateY);
		Transform trans = makeTransform(rotateAmounts);

		lastTransform = trans;

		updateTrixelFaces();
	}


	/**
	 * Makes a combined transform matrix involving several factors including
	 * PRE: trixelCentroid, rotateAmounts, viewTranslation are not null.
	 * @return a transform made by renderer.
	 */
	private Transform makeTransform(Vector3D rotateAmounts) {

		Vector3D viewTranslation = Renderer.STANDARD_VIEW_TRANSLATION;
		return Renderer.makeTransform(rotateAmounts, floorCentroid, viewTranslation);
	}

	/**
	 * Searches whether x,y is within a trixel face.
	 * If x,y in a trixel face, make a new thing next to that face and add it to the level.
	 * The thing that is made is dependant on the drawMode (e.g. trixel, tree, table ...)
	 * @param x
	 * @param y
	 */
	public void makeSomethingAt(int x, int y) {

		y = flipY - y; // flip y value (so up is positive direction)

		TrixelFace face = getTrixelFaceAtViewPoint(x, y);
		if (face == null)	return;

		Trixel trixel = face.getParentTrixel();

		// make a new thing next to this trixel
		Point3D aboveTrixel = TrixelUtil.findTopCenterOfTrixel(trixel, trixelSize);
		//Point3D randomAboveTrixel = aboveTrixel.getTranslatedPoint(new Vector3D((float)Math.random()*Trixel.SIZE, 0, (float)Math.random()*Trixel.SIZE));

	if (drawMode == TRIXEL_MODE){
			createdTrixels.add(makeTrixelNextToFace(face, baseColour));
		}
		if (drawMode == TREE_MODE){
			// TODO: replace this table with tree once tree is drawable
			drawables.add(new Table("Tree", aboveTrixel, new Rectangle3D(40,40,40)));
		}
		if (drawMode == CHEST_MODE){
			drawables.add(new Chest("Chest", new Inventory(), aboveTrixel));
		}
		updateTrixelFaces();
	}

	/**
	 * Re-makes and orders a list of trixel faces from ordered by closest first
	 */
	public void updateTrixelFaces() {
		// reset rotated trixels
		rotatedFaces = new ArrayList<TrixelFace>();

		for (Iterator<Trixel> iter = getAllTrixels(); iter.hasNext();){
			Trixel trixel = iter.next();
			for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel, trixelSize)){
				face.transform(lastTransform);
				rotatedFaces.add(face);
			}
		}

		// sort in order of depth, farest first
		Collections.sort(rotatedFaces, new DepthComparator());
		// reverse so that closest trixels are first (so easy to obtain trixel clicked).
		Collections.reverse(rotatedFaces);
	}

	/**
	 * @param x
	 * @param y
	 * @return the closest trixel face at the x,y position in view space
	 */
	private TrixelFace getTrixelFaceAtViewPoint(int x, int y){
		for (TrixelFace face : rotatedFaces){
			GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(face);
			if (facePoly.contains(x, y)){
				return face;
			}
		}
		return null;
	}

	/**
	 * TODO: The trixel will be made directly in front of face
	 *
	 * @param face - the TrixelFace directly next to the trixel to be made
	 */
	private Trixel makeTrixelNextToFace(TrixelFace face, Color colour) {

		Trixel parentTrixel = face.getParentTrixel();
		/*
		// find the true (unrotated) normal vector of the face by reversing the transform that was applied
		Point3D trixelCenter = TrixelUtil.findTrixelCentroid(parentTrixel, trixelSize);
		Point3D faceCenter = face.findCenterPoint();
		Vector3D newTrixelDir = trixelCenter.distanceTo(faceCenter);*/

		Point3D overTrixel = TrixelUtil.findTopCenterOfTrixel(parentTrixel, trixelSize);

		return new Trixel(TrixelUtil.positionToTrixition(overTrixel, trixelSize), getTrixelColour());
	}

	public Transform getLastTransform() {
		return lastTransform;
	}


	private final static String SEPARATOR = "\t";

	@Override
	public String toString(){

		StringBuilder levelString = new StringBuilder();

		// WRITE EVERYTHING
		levelString.append("222game level");
		levelString.append(VERSION_NUMBER);
		levelString.append("\n");
		levelString.append("Floor trixels");
		levelString.append(floorTrixels.size());
		levelString.append("Trixition"+SEPARATOR+"Colour");
		levelString.append("\n");

		for (Trixel floorTrixel : floorTrixels){
			levelString.append(floorTrixel);
		}

		levelString.append("\n");
		levelString.append("Created trixels");
		levelString.append(createdTrixels.size());
		levelString.append("Trixition"+SEPARATOR+"Colour");
		levelString.append("\n");

		for (Trixel createdTrixel : createdTrixels){
			levelString.append(createdTrixel);
		}

		levelString.append("\n");
		levelString.append("Drawable objects");
		levelString.append(drawables.size());
		levelString.append("Classname"+SEPARATOR+"ImageName"+SEPARATOR+"Position"+SEPARATOR+"BoundingBox"+SEPARATOR+"SpecificInfo");
		levelString.append("\n");

		for (Drawable drawable : drawables){
			levelString.append(drawable); // TODO sort this part out
		}

		return levelString.toString();
	}

	/**
	 * Reads a place file and makes us
	 * @param placeFile
	 * @return
	 */
	public static Place makePlaceFromFile(File placeFile){
		String title = "222game level";
		String floorTrixelsHeader = "Floor trixels";

		Set<Cube> floorCubes = new HashSet<Cube>();
		Set<Cube> createdCubes = new HashSet<Cube>();
		Set<Drawable> drawables = new HashSet<Drawable>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(placeFile));

			ensureMatch(reader.readLine(), title); // skip title line
			float version = Float.parseFloat(reader.readLine()); // version number of level maker
			reader.readLine(); // blank line

			ensureMatch(reader.readLine(), floorTrixelsHeader); // declaration that next tokens = floor trixels
			int floorTrixelsNum = Integer.parseInt(reader.readLine()); // number of floor trixels
			reader.readLine(); // details next tokens
			reader.readLine(); // blank

			for (int f = 0; f < floorTrixelsNum; f++){
				String[] trixelInfo = reader.readLine().split(SEPARATOR);
				//floorCubes.add(new Cube())
			}

			reader.close();

		} catch (IOException e) {
			failParsing("Some IO problem");
			e.printStackTrace();
		}

		return null; // TODO finish

	}

	public static void failParsing(String reason){
		System.err.println("************\nError reading place file\n***************");
		System.err.println(reason);
	}

	public static void ensureMatch(String toMatch, String token){
		if (!toMatch.equals(token)){
			failParsing(toMatch + " didn't match: '"+token+"'");
		}
	}

	/**
	 * sets the colour of the next trixel
	 * @param colour
	 */
	public void setBaseColour(Color colour){
		this.baseColour = colour;
	}

	/**
	 * @return the next trixel colour
	 */
	public Color getTrixelColour(){
		return Renderer.makeRandomColor(baseColour, randomColourDeviation);
	}

	/**
	 * Sets the drawing mode of the level maker
	 * @param drawMode
	 */
	public void setDrawMode(String drawMode) {
		this.drawMode  = drawMode;

	}

	/**
	 * deletes a trixel at this x, y location
	 * @param x
	 * @param y
	 */
	public void deleteTrixelAt(int x, int y) {
		y = flipY - y; // flip y value (so up is positive direction)

		TrixelFace face = getTrixelFaceAtViewPoint(x, y);

		if (face == null)	return;

		Trixel trixel = face.getParentTrixel();
		createdTrixels.remove(trixel);
	}

	public void setColourDeviation(int deviation) {
		randomColourDeviation = deviation;
	}

	public Iterator<Drawable> getWorldObjects() {
		return drawables.iterator();
	}

	/**
	 * @return all trixels in level
	 */
	private Iterator<Trixel> getAllTrixels(){
		Set<Trixel> allTrixels = new HashSet<Trixel>(floorTrixels);
		allTrixels.addAll(createdTrixels);
		return allTrixels.iterator();
	}

	public Iterator<Trixel> getFloorTrixels() {
		return floorTrixels.iterator();
	}

	public Iterator<Trixel> getCreatedTrixels(){
		return createdTrixels.iterator();
	}

	public void setTrixelSize(int trixelSize) {
		this.trixelSize = trixelSize;

	}
	public int getTrixelSize() {
		return trixelSize;
	}

	/**
	 * Makes a floor with a random shape.
	 * @return a floor
	 */
	public Floor makeRandomFloor(){
		int minPoints = 4;
		int maxPoints = 10;
		int y = -trixelSize;
		int numPoints = (int)(Math.random()*(maxPoints-minPoints)+minPoints);
		int maxXValue = 1000;
		int maxYValue = 800;

		Point3D[] vertices = new Point3D[numPoints];

		for (int i = 0; i < numPoints; i++){
			int x = (int)(Math.random()*maxXValue);
			int z = (int)(Math.random()*maxYValue);
			vertices[i] = new Point3D(x, y, z);
		}
		return new Floor(vertices);
	}

	/**
	 * sets the base colour to be a totally random colour
	 */
	public void randomiseBaseColour() {
		setBaseColour(Renderer.makeRandomColour());
	}

	private void clearLevel() {
		floorTrixels.clear();
		createdTrixels.clear();
		drawables.clear();
	}

	/**
	 * updates trixel faces and the current rotation.
	 */
	private void updateFaces() {
		updateRotation(0,0);
	}
	
	private void generateRandomBackGround(){
		
	}

}
