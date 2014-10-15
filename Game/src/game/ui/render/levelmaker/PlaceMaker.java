package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.able.GamePolygon;
import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelFace;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.util.DepthComparable;
import game.ui.render.util.DepthComparator;
import game.ui.render.util.Transform;
import game.ui.window.GameWindow;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;
import game.world.model.AirTank;
import game.world.model.Chest;
import game.world.model.Crystal;
import game.world.model.Cube;
import game.world.model.Enviroment;
import game.world.model.FinishPortal;
import game.world.model.Inventory;
import game.world.model.Item;
import game.world.model.LockedPortal;
import game.world.model.Place;
import game.world.model.Plant;
import game.world.model.Portal;
import game.world.model.Room;
import game.world.model.Tree;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Will Hardwick-Smith 300285801
 * @author Aaron Haslett 300306981
 *
 * Used for designing levels for the 222game.
 * Starts with a flat plane of trixels, made with a floor object.
 * This can be expanded on using any of the tools.
 * View of the trixels can be rotated.
 */
public class PlaceMaker{

	public static final String PLANT_MODE = "Plant";
	public static final String DOOR_MODE = "Door";
	public static final String AIR_TANK_MODE = "Air_tank";
	public static final String TREE_MODE = "Tree";
	public static final String TRIXEL_MODE = "Trixel";
	public static final String CHEST_MODE = "Chest";
	public static final String CRYSTAL_MODE = "Crystal";
	public static final String LOCKED_PORTAL_MODE = "LockedPortal";
	public static final String FINISH_PORTAL_MODE = "FinishPortal";
	/**
	 * All of the draw modes that the world maker can be in
	 */
	public static final String[] MODES = {CHEST_MODE, PLANT_MODE, TREE_MODE, DOOR_MODE, AIR_TANK_MODE, TRIXEL_MODE,
	                                      CRYSTAL_MODE, LOCKED_PORTAL_MODE, FINISH_PORTAL_MODE};

	public static final int MIN_COLOUR_DEVIATION = 0;
	public static final int MAX_COLOUR_DEVIATION = 100;
	public static final int START_COLOUR_DEVIATION = 20;
	public static final int DEFAULT_TRIXEL_SIZE = Trixel.DEFAULT_SIZE;

	//id for keeping item names unique
	private static int id = 0;
	public static FinishPortal finishPortal = null;
	public static Point3D finishPortalPoint = null;
	public static PlaceMaker finishPortalLM = null;

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

		DepthComparable thingClicked = findSomethingAtPoint(x,y);
		Trixel clickedTrixel = face.getParentTrixel();
		// make a new thing next to this trixel
		Point3D aboveTrixel = TrixelUtil.findTopCenterOfTrixel(clickedTrixel, trixelSize);
		Drawable thingToAdd = null;

		// Normal item/environment adds
		if (drawMode == PLANT_MODE){
			thingToAdd = new Plant("Plant" + (id++), aboveTrixel);
		}else if (drawMode == TREE_MODE){
			thingToAdd = new Tree("Tree" + (id++), aboveTrixel);
		}else if (drawMode == AIR_TANK_MODE){
			thingToAdd = new AirTank("Air tank" + (id++), aboveTrixel);
		}else if (drawMode == CHEST_MODE){
			thingToAdd = new Chest("Chest" + (id++), new Inventory(), aboveTrixel);
		}else if (drawMode == CRYSTAL_MODE){
			thingToAdd = new Crystal("Crystal" + (id++), aboveTrixel);
		}else if (drawMode == TRIXEL_MODE){//Trixels
			Trixel newTrixel = makeTrixelNextToFace(face, baseColour);
			createdTrixels.add(newTrixel);
			return;
		}else if (drawMode == DOOR_MODE || drawMode == LOCKED_PORTAL_MODE){//Portals
			boolean locked = drawMode == LOCKED_PORTAL_MODE;
			if(tempPortal == null){//If this is the first half of a portal link, set the tempPortal variable
				tempPortal = new SimplePortal(this, aboveTrixel, null, locked);
				drawables.add(tempPortal);
				getPortals().add(tempPortal);
				System.out.println("Made start portal at " + aboveTrixel + " in room " + getName());
			}else if(tempPortal.locked == locked){//Otherwise, if this is the second half and the portal type matches
				if(tempPortal.lm != this){//We can't set a portal link between two places in the same room.
					SimplePortal newSP = new SimplePortal(this, aboveTrixel, tempPortal, locked);
					tempPortal.toPortal = newSP;
					drawables.add(newSP);
					getPortals().add(newSP);
					tempPortal = null;
					System.out.println("Made portal link from " + aboveTrixel + " in " + getName() + " to " + newSP.toPortal.location + " in " + newSP.toPortal.lm.getName());
				}
			}
			return;
		}else if (drawMode == FINISH_PORTAL_MODE){//The finish portal is a special case.
			if(finishPortal != null){return;}
			//Remember its fields for WorldMaker to use
			finishPortal = new FinishPortal("FinishPortal" + (id++), null, aboveTrixel, 10);
			finishPortalPoint = aboveTrixel;
			finishPortalLM = this;
			thingToAdd = finishPortal;
		}

		//If we clicked on a chest, and we've selected an item, then add that item to the chest.
		if(thingClicked instanceof DrawablePlaceHolder){
			Drawable d = ((DrawablePlaceHolder)thingClicked).getDrawable();
			if(d instanceof Chest && thingToAdd instanceof Item){
				((Chest)d).getContents().addItem((Item)thingToAdd);
				System.out.println("Added thing to chest");
			}
		}
		else{//Add the selected thing to drawables so it'll be rendered
			drawables.add(thingToAdd);
		}

		updateTransformedObjects();
	}

	/**
	 * Deletes a trixel at this x, y location.
	 * Can't be a floor trixel.
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
			// could be either a floor or created trixel. Only delete floor trixel
			createdTrixels.remove(trixel);
		}
		else if (something instanceof DrawablePlaceHolder){
			DrawablePlaceHolder placeHolder = (DrawablePlaceHolder) something;
			Drawable drawable = placeHolder.getDrawable();
			if(drawable instanceof SimplePortal){
				removePortal((SimplePortal)drawable);
			}else{
				drawables.remove(drawable);
			}

			if(drawable instanceof FinishPortal){
				finishPortal = null;
				finishPortalPoint = null;
				finishPortalLM = null;
			}
		}

		updateTransformedObjects();
	}

	/**
	 * Remove a portal from this PlaceMaker, and any portals it links to in other PlaceMakers
	 * @param portal Portal to remove
	 */
	public void removePortal(SimplePortal portal){
		if(portal == null){return;}
		if(portal.lm == this){//Don't remove the portal if it's not in this PlaceMaker
			drawables.remove(portal);
			if(tempPortal == portal){//If the portal is the current tempPortal then clear that variable
				tempPortal = null;
			}
			if(portal.toPortal != null){//Remove the other half of the link if it is declared
				portal.toPortal.toPortal = null;
				portal.toPortal.lm.removePortal(portal.toPortal);
			}
		}
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

	/**
	 * Finds an object (either a drawable or a trixel) at the view-space point x,y.
	 * @param x
	 * @param y
	 * @return the object at this point.
	 */
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
				DrawablePlaceHolder drawable = (DrawablePlaceHolder) object;

				if (drawable.pointIsIn(x, y)){
					return drawable;
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
	 * Makes a place object using the information in the level.
	 * @return
	 */
	public Place toPlace(){

		List<Item> items = new ArrayList<Item>();
		List<Enviroment> environment = new ArrayList<Enviroment>();

		//Add all drawable objects to the correct list except for portals
		for (Drawable drawable : drawables){
			if (drawable instanceof Item){
				items.add((Item) drawable);
			}
			if (drawable instanceof Enviroment){
				environment.add((Enviroment) drawable);
			}
		}

		//Trixels are split into floorTrixels and createdTrixels in PlaceMaker, save them both
		//with a field in Cube to show the difference
		for (Trixel floorTrixel : floorTrixels){
			environment.add(new Cube(Cube.FLOOR, floorTrixel, trixelSize, false));
		}
		for (Trixel createdTrixel : createdTrixels){
			environment.add(new Cube(Cube.NON_FLOOR, createdTrixel, trixelSize, true));
		}

		//Get the floor polygon from the floor we generated earlier
		Polygon floorPolygon = Renderer.floorToVerticalPolygon(floor);

		return new Room(items, environment, floorPolygon, getName());
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

	/**
	 * Get the draw mode
	 * @return the current draw mode
	 */
	public String getDrawMode(){
		return drawMode;
	}

	/**
	 * Set the random colour deviation used when generating floors and trixels
	 */
	public void setColourDeviation(int deviation) {
		randomColourDeviation = deviation;
	}

	/**
	 * Get all objects in the world, stored as a drawables list in this class
	 * @return A list of all drawable objects in the world
	 */
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

	/**
	 * Get the list of trixels making up the floor
	 * @return All trixels making up the floor
	 */
	public Iterator<Trixel> getFloorTrixels() {
		return floorTrixels.iterator();
	}

	/**
	 * Get all trixels created by the user
	 * @return All user created trixels
	 */
	public Iterator<Trixel> getCreatedTrixels(){
		return createdTrixels.iterator();
	}

	/**
	 * Set the integer size used to draw pixels
	 * @param trixelSize Size of constructed trixels
	 */
	public void setTrixelSize(int trixelSize) {
		this.trixelSize = trixelSize;

	}

	/**
	 * Get the trixel size
	 * @return Size of trixels
	 */
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

		//Iterate through the randomly chosen range and construct points
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
		portals.clear();
		if(tempPortal != null){
			removePortal(tempPortal);
		}
	}

	/**
	 * Get the name of the place this PlaceMaker is making
	 * @return name of the place of this PlaceMaker
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the place this PlaceMaker is making
	 * @param name Name of the place of this PlaceMaker
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * A temporary portal class used to represent a real portal between saves/loads because
	 * when constructing a portal we don't have a second place until the second portal is placed.
	 */
	public class SimplePortal extends Portal{
		public PlaceMaker lm;
		public Point3D location;
		public SimplePortal toPortal;
		public boolean locked;

		public SimplePortal(PlaceMaker lm, Point3D location, SimplePortal toPortal, boolean locked){
			super((locked?"LockedPortal":"Portal") + id, null, location, null, null);
			id++;
			this.lm = lm;
			this.location = location;
			this.toPortal = toPortal;
			this.locked = locked;
		}
		@Override
		public Point3D getPosition(Place place){
			return getPosition();
		}
		@Override
		public String getImageName() {
			return locked ? "teleport_off":"teleporter_on";
		}
	}

	/**
	 * Add a portal to this PlaceMaker
	 * @param sp SimplePortal to add to Place being made
	 */
	public void addPortal(SimplePortal sp){
		portals.add(sp);
		drawables.add(sp);
	}

	/**
	 * Load the given place into this PlaceMaker
	 * @param place Place to load into this PlaceMaker
	 */
	public void loadPlace(Place place){
		createdTrixels.clear();
		floorTrixels.clear();
		drawables.clear();
		portals.clear();
		tempPortal = null;

		//Add every non-portal drawable to the drawables
		for (Iterator<Drawable> placeDrawables = place.getDrawable(); placeDrawables.hasNext();){
			Drawable d = placeDrawables.next();
			if(!(d instanceof Portal) && !(d instanceof LockedPortal)){
				drawables.add(d);
			}
		}
		//Add the environment
		for (Iterator<Enviroment> placeEnvironments = place.getEnviroment(); placeEnvironments.hasNext();){
			Enviroment env = placeEnvironments.next();
			if(env instanceof Cube){
				Cube c = (Cube)env;
				Trixel t = new Trixel(c.getTrixition(), c.getColor());
				if(c.getName().equals("floor")){
					floorTrixels.add(t);
				}else if(c.getName().equals("non-floor")){
					createdTrixels.add(t);
				}
			}
		}
		name = place.getName();
		floorCentroid = TrixelUtil.findTrixelsCentroid(floorTrixels.iterator(), trixelSize);
		floor = place.getFloor();
		updateFaces();
	}

	/**
	 * Get all portals across all PlaceMakers
	 * @return All portals defined across all PlaceMakers
	 */
	public static Set<SimplePortal> getPortals() {
		return portals;
	}

	/**
	 * Set a new list of portals
	 * @param portals Set of SimplePortal objects
	 */
	public static void setPortals(Set<SimplePortal> portals) {
		PlaceMaker.portals = portals;
	}
}
