package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.able.GamePolygon;
import game.ui.render.texture.Vine;
import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelFace;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.util.DepthComparable;
import game.ui.render.util.DepthComparator;
import game.ui.render.util.Transform;
import game.ui.window.GameWindow;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;
import game.world.model.AirTank;
import game.world.model.Chest;
import game.world.model.Cube;
import game.world.model.Enviroment;
import game.world.model.Inventory;
import game.world.model.Item;
import game.world.model.Place;
import game.world.model.Plant;
import game.world.model.Portal;
import game.world.model.Room;
import game.world.model.Table;
import game.world.model.Tree;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.omg.CORBA.Environment;

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
public class PlaceMaker{

	public static final String PLANT_MODE = "Plant";
	public static final String DOOR_MODE = "Door";
	public static final String AIR_TANK_MODE = "Air_tank";
	public static final String TREE_MODE = "Tree";
	public static final String TRIXEL_MODE = "Trixel";
	public static final String CHEST_MODE = "Chest";
	public static final String[] MODES = {PLANT_MODE, TREE_MODE, DOOR_MODE, AIR_TANK_MODE, TRIXEL_MODE};

	public static final int MIN_COLOUR_DEVIATION = 0;
	public static final int MAX_COLOUR_DEVIATION = 100;
	public static final int START_COLOUR_DEVIATION = 20;
	public static final int DEFAULT_TRIXEL_SIZE = Trixel.DEFAULT_SIZE;
	/**
	 * the amount in which to rotate the level/place (so that it can be updated)
	 */
	private Vector3D rotateAmounts = new Vector3D(0,0,0);
	/**
	 * all objects which have been rotated
	 */
	private List<DepthComparable> rotatedObjects;

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
	 * portals
	 */
	private static Set<SimplePortal> portals;
	/**
	 * a temporary variable for when portals are being placed
	 */
	private static SimplePortal tempPortal;
	/**
	 * name of the level
	 */
	public String name;
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
	private static String drawMode;
	/**
	 * how much to deviate from the base colour when making the next colour
	 */
	private int randomColourDeviation;
	/**
	 * size of trixels to be made
	 */
	private int trixelSize = DEFAULT_TRIXEL_SIZE;

	/**
	 * For use when making a Place object for the level
	 */
	private Floor floor;

	/**
	 * Initialises LevelMaker's fields
	 */
	public PlaceMaker(){

		// initialise trixels
		createdTrixels = new HashSet<Trixel>();
		floorTrixels = new HashSet<Trixel>();
		floorCentroid = new Point3D(0,0,0);

		// initialise drawables, portals
		drawables = new HashSet<Drawable>();
		setPortals(new HashSet<SimplePortal>());

		// intialise colour
		randomiseBaseColour();
		randomColourDeviation = START_COLOUR_DEVIATION;

		// initialise draw mode
		drawMode = TRIXEL_MODE;

		// initialise lastTransform with no rotation
		lastTransform = makeTransform(new Vector3D(0,0,0));

		//intilise rotatedFaces
		rotatedObjects = new ArrayList<DepthComparable>();
		updateRotation(0, 0);
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

		// move level to the center of the screen
		floorCentroid = Renderer.getFloorCentroid(floor);
		Point3D idealCentroid = new Point3D(GameWindow.FRAME_WIDTH/2, floorCentroid.y, GameWindow.FRAME_HEIGHT/2);
		Vector3D distToScreenCenter = floorCentroid.distanceTo(idealCentroid);
		floor.transform(Transform.newTranslation(distToScreenCenter));
		// re-calculate floor centroid.
		floorCentroid = Renderer.getFloorCentroid(floor);

		// initialise trixels
		for (Trixel t : TrixelUtil.polygon2DToTrixels(
			Renderer.floorToVerticalPolygon(floor), trixelSize, -trixelSize)){
			t.setColour(getTrixelColour());
			floorTrixels.add(t);
		}

		this.floor = floor;

		updateFaces();
	}


	/**
	 * updates trixel faces and the current rotation.
	 */
	private void updateFaces() {
		updateRotation(0,0);
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

		updateTransformedObjects();
		// update light direction
		Renderer.resetLightDir();
		//Renderer.lightDir = Transform.newYRotation(rotateAmounts.y).multiply(Renderer.lightDir);
	}

	/**
	 * Re-makes and orders a list of trixel faces and drawables from ordered by closest first
	 */
	public void updateTransformedObjects() {
		// reset rotated trixels
		rotatedObjects = new ArrayList<DepthComparable>();

		for (Iterator<Trixel> iter = getAllTrixels(); iter.hasNext();){
			Trixel trixel = iter.next();
			for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel, trixelSize)){
				face.transform(lastTransform);
				rotatedObjects.add(face);
			}
		}

		for (Drawable drawable : drawables){
			DrawablePlaceHolder placeHolder = new DrawablePlaceHolder(drawable, drawable.getPosition());
			placeHolder.transform(lastTransform);
			rotatedObjects.add(placeHolder);
		}

		// sort in order of depth, farest first
		Collections.sort(rotatedObjects, new DepthComparator());
		// reverse so that closest trixels are first (so easy to obtain what is clicked).
		Collections.reverse(rotatedObjects);
	}

	/**
	 * Makes a combined transform matrix involving several factors including
	 * PRE: trixelCentroid, rotateAmounts, viewTranslation are not null.
	 * @return a transform made by renderer.
	 */
	private Transform makeTransform(Vector3D rotateAmounts) {

		Vector3D viewTranslation = new Vector3D(0,Renderer.STANDARD_VIEW_TRANSLATION.y, 0);//new Vector3D(0,0,0);
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

		TrixelFace face = getTrixelFaceAt(x, y);
		if (face == null)	return;

		Trixel clickedTrixel = face.getParentTrixel();

		// make a new thing next to this trixel
		Point3D aboveTrixel = TrixelUtil.findTopCenterOfTrixel(clickedTrixel, trixelSize);
		//Point3D randomAboveTrixel = aboveTrixel.getTranslatedPoint(new Vector3D((float)Math.random()*Trixel.SIZE, 0, (float)Math.random()*Trixel.SIZE));

		if (drawMode == TRIXEL_MODE){
			Trixel newTrixel = makeTrixelNextToFace(face, baseColour);
			createdTrixels.add(newTrixel);
		}
		if (drawMode == PLANT_MODE){
			drawables.add(new Plant("Plant", aboveTrixel));
		}
		if (drawMode == TREE_MODE){
			drawables.add(new Tree("Tree", aboveTrixel));
		}
		if (drawMode == AIR_TANK_MODE){
			drawables.add(new AirTank("Air tank", aboveTrixel));
		}
		if (drawMode == CHEST_MODE){
			drawables.add(new Chest("Chest", new Inventory(), aboveTrixel));
		}
		if (drawMode == DOOR_MODE){
			if(tempPortal == null){
				tempPortal = new SimplePortal(this, aboveTrixel, null);
				drawables.add(tempPortal);
				getPortals().add(tempPortal);
				System.out.println("Made start portal at " + aboveTrixel + " in room " + getName());
			}else{
				if(tempPortal.lm != this){
					SimplePortal newSP = new SimplePortal(this, aboveTrixel, tempPortal);
					tempPortal.toPortal = newSP;
					drawables.add(newSP);
					getPortals().add(newSP);
					tempPortal = null;
					System.out.println("Made portal link from " + aboveTrixel + " in " + getName() + " to " + newSP.toPortal.location + " in " + newSP.toPortal.lm.getName());
				}
			}
		}
		updateTransformedObjects();
	}

	/**
	 * deletes a trixel at this x, y location
	 * @param x
	 * @param y
	 */
	public void deleteSomethingAt(int x, int y) {
		y = flipY - y; // flip y value (so up is positive direction)

		DepthComparable something = findSomethingAtPoint(x,y);

		System.out.println(something);
		if (something == null)	return;

		if (something instanceof TrixelFace){
			TrixelFace face = (TrixelFace) something;
			Trixel trixel = face.getParentTrixel();
			// could be either a floor or created trixel.
			createdTrixels.remove(trixel);
			floorTrixels.remove(trixel);
		}
		else if (something instanceof DrawablePlaceHolder){
			DrawablePlaceHolder placeHolder = (DrawablePlaceHolder) something;
			Drawable drawable = placeHolder.getDrawable();
			drawables.remove(drawable);
		}

		updateTransformedObjects();
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
	 * @param x
	 * @param y
	 * @return the closest trixel face at the x,y position in view space
	 */
	private TrixelFace getTrixelFaceAt(int x, int y){
		for (DepthComparable object : rotatedObjects){
			if (!(object instanceof TrixelFace)) continue;
			TrixelFace face = (TrixelFace) object;
			GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(face);
			if (facePoly.contains(x, y)){
				return face;
			}
		}
		return null;
	}

	private DepthComparable findSomethingAtPoint(int x, int y){
		for (DepthComparable object : rotatedObjects){
			if (object instanceof TrixelFace){
				TrixelFace face = (TrixelFace) object;
				GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(face);
				if (facePoly.contains(x, y)){
					return face;
				}
			}
			else if (object instanceof DrawablePlaceHolder){
				DrawablePlaceHolder temp = (DrawablePlaceHolder) object;

				if (temp.pointIsIn(x, y)){
					return temp;
				}
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

	/**
	 * Makes a place object using the information in
	 * @return
	 */
	public Place toPlace(){

		List<Item> items = new ArrayList<Item>();
		List<Enviroment> environment = new ArrayList<Enviroment>();

		for (Drawable drawable : drawables){
			if (drawable instanceof Item){
				items.add((Item) drawable);
			}
			if (drawable instanceof Enviroment){
				environment.add((Enviroment) drawable);
			}
		}

		for (Trixel floorTrixel : floorTrixels){
			environment.add(new Cube(Cube.FLOOR, floorTrixel, trixelSize, false));
		}
		for (Trixel createdTrixel : createdTrixels){
			environment.add(new Cube(Cube.NON_FLOOR, createdTrixel, trixelSize, true));
		}

		Polygon floorPolygon = Renderer.floorToVerticalPolygon(floor);

		return new Room(items, environment, floorPolygon, getName());
	}

	private List<Vine> makeVinesAroundTrixel(Trixel newTrixel) {
		float minHeight = trixelSize/2;
		float maxHeight = trixelSize;
		TrixelFace[] faces = TrixelUtil.makeTrixelFaces(newTrixel, trixelSize);
		// the faces around the side of the trixel (all but top and bottom).
		TrixelFace[] vineFaces = new TrixelFace[] { faces[0], faces[1], faces[4], faces[5] };
		List<Vine> vines = new ArrayList<Vine>();

		for (TrixelFace vineFace : vineFaces){

			int numVines = (int)(Math.random()*3);
			Vector3D topLineGradient = vineFace.findTopLineGradient();

			for (int v = 0; v < numVines; v++){
				Point3D center = vineFace.findCenterPoint();

				float dist = (float)(Math.random()*trixelSize - (trixelSize/2)); // travel to random x,z pos within trixel face
				// translation from center to random top point
				Vector3D translateFromCenter = new Vector3D((dist*topLineGradient.x),
						trixelSize/2,
						dist*topLineGradient.z);
				Point3D randomTopPoint = center.getTranslatedPoint(translateFromCenter);
				float height = (float)((Math.random()*(maxHeight-minHeight))+minHeight);
				Point3D vinePosition = randomTopPoint.getTranslatedPoint(new Vector3D(0,-height, 0));

				vines.add(new Vine(vinePosition, height));

				/*System.out.println("center:\t" + center);
				System.out.println("top line gradient:\t"+topLineGradient);
				System.out.println("dist:\t"+dist);
				System.out.println("trans from center:\t"+translateFromCenter);
				System.out.println("randomTop:\t"+randomTopPoint);
				System.out.println("height:\t"+height);
				System.out.println("vinePosition:\t"+vinePosition);*/
			}
		}
		return vines;
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
	public static void setDrawMode(String drawMode) {
		PlaceMaker.drawMode  = drawMode;
	}

	public String getDrawMode(){
		return drawMode;
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

	/**
	 * Resets level to its default state
	 */
	private void clearLevel() {
		floorTrixels.clear();
		createdTrixels.clear();
		drawables.clear();
	}



	/**
	 * Test method for generating random background objects.
	 *
	 */
	private List<BackgroundObject> generateRandomBackgroundObjects(){
		int maxObjectsCount = 20;
		int minObjectsCount = 10;
		int minSize = 10;
		int maxSize = 100;
		int minYDist = -1000;
		int maxYDist = 1000;
		int minXZ = 100;// the min x or z value.
		int maxXZ = 1000;

		Random r = new Random();

		int objectsCount = r.nextInt(maxObjectsCount-minObjectsCount) + minObjectsCount;
		List<BackgroundObject> background = new ArrayList<BackgroundObject>();

		for (int i = 0; i < objectsCount; i++){
			int size = r.nextInt(maxSize-minSize) + minSize;

			int xPos = r.nextInt(maxXZ-minXZ) + (minXZ * (r.nextInt(2) == 0 ? 1 : -1)); // 50/50 of being pos or neg
			// y is more constrained, because the game is currently seen from a constant y-axis angle
			int yPos = (int) (floorCentroid.y + r.nextInt(maxYDist-minYDist) + minYDist);
			int zPos = r.nextInt(maxXZ-minXZ) + (minXZ * (r.nextInt(2) == 0 ? 1 : -1));

			//System.out.println("background object: "+xPos + " " + yPos + " " + zPos);

			Point3D pos = new Point3D(xPos, yPos, zPos);

			// rotate
			pos = Renderer.makeReverseTransform(rotateAmounts, floorCentroid, Renderer.STANDARD_VIEW_TRANSLATION).multiply(pos);

			background.add(new BackgroundObject(pos, size));
		}

		return background;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public class SimplePortal extends Portal{
		public PlaceMaker lm;
		public Point3D location;
		public SimplePortal toPortal;

		public SimplePortal(PlaceMaker lm, Point3D location, SimplePortal toPortal){
			super("Portal", null, location, null, null);
			this.lm = lm;
			this.location = location;
			this.toPortal = toPortal;
		}
		public Point3D getPosition(Place place){
			return getPosition();
		}
	}

	public void addPortal(SimplePortal sp){
		portals.add(sp);
		drawables.add(sp);
	}

	public void loadPlace(Place place){
		createdTrixels.clear();
		floorTrixels.clear();
		drawables.clear();
		portals.clear();
		tempPortal = null;

		for (Iterator<Drawable> placeDrawables = place.getDrawable(); placeDrawables.hasNext();){
			Drawable d = placeDrawables.next();
			if(!(d instanceof Portal)){
				drawables.add(d);
			}
		}
		for (Iterator<Enviroment> placeEnvironments = place.getEnviroment(); placeEnvironments.hasNext();){
			Enviroment env = placeEnvironments.next();
			if(env instanceof Cube){
				Cube c = (Cube)env;
				Trixel t = new Trixel(c.getTrixition());
				if(c.getName().equals("floor")){
					floorTrixels.add(t);
				}else if(c.getName().equals("non-floor")){
					createdTrixels.add(t);
				}
			}
		}
		name = place.getName();
		floorCentroid = TrixelUtil.findTrixelsCentroid(floorTrixels.iterator(), trixelSize);
		updateFaces();
	}

	public static Set<SimplePortal> getPortals() {
		return portals;
	}

	public static void setPortals(Set<SimplePortal> portals) {
		PlaceMaker.portals = portals;
	}
}
