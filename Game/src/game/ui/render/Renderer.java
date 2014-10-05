package game.ui.render;

import game.ui.render.able.GameImage;
import game.ui.render.able.GamePolygon;
import game.ui.render.able.GameText;
import game.ui.render.able.Line3D;
import game.ui.render.able.Renderable;
import game.ui.render.trixel.Trixel;
import game.ui.render.trixel.TrixelFace;
import game.ui.render.trixel.TrixelUtil;
import game.ui.render.util.LightSource;
import game.ui.render.util.Transform;
import game.ui.render.util.ZComparator;
import game.ui.window.GameWindow;
import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.dimensions.Vector3D;
import game.world.model.Place;
import game.world.model.Player;
import game.world.util.Drawable;
import game.world.util.Floor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Renderer {

	// TEMPORARY

	private static final Transform ISOMETRIC_ROTATION = Transform.newXRotation((float)(Math.PI/4)).compose(Transform.newYRotation((float)(Math.PI/4)));
	public static final Vector3D STANDARD_VIEW_TRANSLATION = new Vector3D(0,500,0);

	private static final int FRAME_TOP = GameWindow.FRAME_HEIGHT;

	public static final long RANDOM_SEED = 15274910874912L;
	private static final Color DEFAULT_AMBIENT_LIGHT = new Color(100, 100, 100);
	public static Random randomGen;

	/**
	 * Draws a place using Graphics object, viewer direction and place
	 * Floor from the place is converted into trixels which are then drawn
	 * All drawable objects from the place are drawn as images
	 * @param g
	 * @param place
	 */
	public static void renderPlace(Graphics g, Place place, Vector3D rotateAmount){
		resetColour(); //  TODO: replace this random colour implementation

		Graphics2D g2 = (Graphics2D) g;
		// enable anti-aliasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component
		Queue<Renderable> renderables = new PriorityQueue<Renderable>(50, new ZComparator());

		// convert floor into trixels and add those to toDraw
		Floor floor = place.getFloor();
		Polygon floorPolygon = floorToVerticalPolygon(floor);
		Point3D floorCentroid = getFloorCentroid(floor);

		Vector3D viewTranslation = STANDARD_VIEW_TRANSLATION;

		// all rotations and translations composed into one affine transform
		Transform transform = makeTransform(
				rotateAmount,
				floorCentroid,
				viewTranslation
			);


		List<Trixel> floorTrixels = TrixelUtil.polygon2DToTrixels(floorPolygon, -Trixel.SIZE);

		// get everything ready to render
		renderables.addAll(trixelsToRenderables(floorTrixels.iterator(), transform));
		renderables.addAll(drawablesToRenderables(place.getDrawable(), transform, place));

		flipYAxis(renderables);

		// draw everything
		drawThings(renderables, g2);

	}

	/**
	 * Draws trixels and drawables using a graphics object
	 * Currently used for the level maker
	 *
	 * @param g
	 * @param trixels
	 * @param iterator
	 * @param transform
	 */
	public static void renderLevel(Graphics g, Iterator<Trixel> trixels, Iterator<Drawable> drawables, Transform transform){
		Graphics2D g2 = (Graphics2D) g;
		// enable anti-aliasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// all objects to be drawn (either trixels or 2d images) sorted in order of z (depth) component
		Queue<Renderable> toDraw = new PriorityQueue<Renderable>(50, new ZComparator());

		toDraw.addAll(trixelsToRenderables(trixels, transform));
		toDraw.addAll(drawablesToRenderables(drawables, transform, null));

		flipYAxis(toDraw);

		drawThings(toDraw, g2);
	}

	/**
	 * Makes a list of transformed renderable objects
	 * @param drawables
	 * @param place
	 * @return
	 */
	private static List<Renderable> drawablesToRenderables(Iterator<Drawable> drawables, Transform transform, Place place) {
		List<Renderable> renderables = new ArrayList<Renderable>();
		while (drawables.hasNext()){
			Drawable drawable = drawables.next();
			// drawable is an image
			GameImage image = new GameImage(Res.getImageFromName(drawable.getImageName()),
					drawable.getPosition(place),
					drawable.getBoundingBox());

			image.transform(transform);
			renderables.add(image);

			// if it's player, put name above head
			if (drawable instanceof Player){
				GameText text = new GameText(drawable.getName(),
						image.getPosition().getTranslatedPoint(
								new Vector3D(-drawable.getBoundingBox().width/2, 10, 0)));

				renderables.add(text);
			}
		}
		return renderables;
	}

	/**
	 * Converts trixels into GamePolygons and then transforms them all
	 * @param trixels
	 * @param transform
	 * @return transform renderable trixels
	 */
	private static List<Renderable> trixelsToRenderables(Iterator<Trixel> trixels, Transform transform) {
		List<Renderable> renderables = new ArrayList<Renderable>();
		// temporary solution to having floor behind everything.
		Transform floorBehindDrawables = Transform.newTranslation(0,0,-Trixel.SIZE);

		while (trixels.hasNext()){
			Trixel trixel = trixels.next();
			for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel)){
				face.transform(transform);
				face.transform(floorBehindDrawables);
				renderables.add(Renderer.makeGamePolygonFromTrixelFace(face));
			}
		}
		return renderables;
	}

	/**
	 * FLIP Y VALUES OF ALL THINGS
	 * So that y increases upwards.
	 * @param toDraw
	 */
	private static void flipYAxis(Queue<Renderable> toDraw) {
		for (Renderable shape : toDraw){
			shape.flipAroundY(FRAME_TOP);
		}
	}

	/**
	 * DRAW ALL THE THINGS  ...in correct order
	 * @param renderables
	 * @param g2
	 */
	private static void drawThings(Queue<Renderable> renderables, Graphics2D g2){
		while (!renderables.isEmpty()){
			Renderable renderObject = renderables.poll();
			if (renderObject instanceof GameImage){
				GameImage image = (GameImage) renderObject;
				Point3D position = image.getPosition();
				Rectangle3D boundingBox = image.getBoundingBox();
				// the following drawimage changes y position based on length of gameimage. TODO: find better solution
				g2.drawImage(image.getImage(),
						(int)(position.x - boundingBox.width/2),
						(int)(position.y + boundingBox.length/2 - boundingBox.height),
						(int)boundingBox.width, (int)boundingBox.height, null);
			}
			else if (renderObject instanceof GameText){
				GameText text = (GameText) renderObject;
				Point3D position = text.getPosition();
				g2.setColor(Color.RED);
				g2.drawString(text.getText(), position.x, position.y);
			}
			else if (renderObject instanceof GamePolygon){
				GamePolygon poly = (GamePolygon) renderObject;
				g2.setColor(poly.getColour());
				g2.fillPolygon(poly);
			}
			else if (renderObject instanceof Line3D){
				Line3D line = (Line3D) renderObject;
				Point3D p1 = line.getP1();
				Point3D p2 = line.getP2();
				g2.setColor(Color.orange);
				g2.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
			}
		}
	}

		// -------------- HELPER METHODS -----------------------
		//
		/**
		 * @return array of lines which are along the x,y,z axis
		 */
		private static Line3D[] makeAxisLines() {

			final int LINE_LENGTH = 1000;
			Line3D xLine = new Line3D(new Point3D(0,0,0), new Point3D(LINE_LENGTH,0,			0));
			Line3D yLine = new Line3D(new Point3D(0,0,0), new Point3D(0,			LINE_LENGTH,0));
			Line3D zLine = new Line3D(new Point3D(0,0,0), new Point3D(0,			0,			LINE_LENGTH));
			return new Line3D[]{xLine, yLine, zLine};
		}
		/**
		 * @param face
		 * @return game polygon representing a trixel face
		 */
		public static GamePolygon makeGamePolygonFromTrixelFace(TrixelFace face) {
			Point3D[] vertices = face.getVertices();
			int[] xpoints = new int[vertices.length];
			int[] ypoints = new int[vertices.length];

			float zTotal = 0;
			for (int i=0; i < vertices.length; i++){
				xpoints[i] = (int)vertices[i].getX();
				ypoints[i] = (int)vertices[i].getY();
				zTotal += (int)vertices[i].getZ();
			}
			float zAverage = zTotal/vertices.length;
			Color shadedColour = face.makeShadedColour(getTestLightSources(), DEFAULT_AMBIENT_LIGHT);
			return new GamePolygon(xpoints, ypoints, vertices.length, zAverage, shadedColour);
		}

		private static Iterator<LightSource> getTestLightSources() {
			List<LightSource> lights = new ArrayList<LightSource>();
			Vector3D dir = new Vector3D(0.39056706f, -0.13019001f, -0.9113221f);
			lights.add(new LightSource(0.8f, dir, new Color(150, 150, 150)));
			return lights.iterator();
		}
		/**
		 * rotates a transformable object around a point given a viewer direction
		 * @param object
		 * @param viewerDirection
		 */
		public static Transform makeTransform(Vector3D rotateAmount, Point3D pivotPoint, Vector3D viewSpaceTranslateDist) {

			Transform translateToOrigin = Transform.newTranslation(new Vector3D(pivotPoint.negate()));
			Transform translateBack = Transform.newTranslation(new Vector3D(pivotPoint));

			Transform rotate =
					Transform.newZRotation(rotateAmount.z).compose(
					Transform.newYRotation(rotateAmount.y).compose(
					Transform.newXRotation(rotateAmount.x)
			));

			Transform viewSpaceTranslation =
					Transform.newTranslation(viewSpaceTranslateDist);

			return 	viewSpaceTranslation.compose(
					ISOMETRIC_ROTATION.compose(
					translateBack.compose(
					rotate.compose(
					translateToOrigin
			))));
		}

		/**
		 * @param drawable
		 * @return whether a Drawable object should be represented as an image.
		 */
		private static boolean isImage(Drawable drawable) {
			return Res.isImage(drawable.getImageName());
		}


		/**
		 * @param dir
		 * @param point
		 * @return array of transforms necessary to perform rotation around the point
		 */
		public static Transform[] getRotateAroundPointTransforms(Vector3D dir, Point3D point){
			Transform translateToOrigin = Transform.newTranslation(-point.getX(), -point.getY(), -point.getZ());
			Transform rotate = Transform.newYRotation(dir.getY()).compose(Transform.newXRotation(dir.getX())).compose(Transform.newZRotation(dir.getZ()));
			Transform translateBack = Transform.newTranslation(point.getX(), point.getY(), point.getZ());
			return new Transform[]{ translateToOrigin, rotate, translateBack };
		}

		/**
		 * @return random colour
		 */
		public static Color defaultMakeRandomColour(){

			if (randomGen == null) randomGen = new Random();
			int r = 100 + randomGen.nextInt(100);
			int g = 100 + randomGen.nextInt(100);
			int b = 200;//

			// nicky's color code
//			int col = randomColor.nextInt(254);
//			int r = col;
//			int g = col;
//			int b = col;//
//
//
//			int r = 100 + randomColor.nextInt(100);
//			int g = 100 + randomColor.nextInt(100);
//			int b = 200;//

			return new Color(r, g, b);
		}

		/**
		 * @param random
		 * @return a colour based on input random generator
		 */
		public static Color makeRandomColour(){
			Random random = new Random();
			int b = random.nextInt(255);
			int g = random.nextInt(255);
			int r = random.nextInt(255);

			return new Color(r,g,b);
		}

		/**
		 * @param base
		 * @param random
		 * @return a random colour that deviates
		 */
		public static Color makeRandomColor(Color base, int maxDeviation){
			Random random = new Random();
			int deviation = (int)(random.nextInt(maxDeviation*2) - maxDeviation);
			int r = Math.max(Math.min(base.getRed() + deviation, 255), 0);

			deviation = (int)(random.nextInt(maxDeviation*2) - maxDeviation);
			int g = Math.max(Math.min(base.getGreen() + deviation, 255), 0);

			deviation = (int)(random.nextInt(maxDeviation*2) - maxDeviation);
			int b = Math.max(Math.min(base.getBlue() + deviation, 255), 0);

			return new Color(r,g,b);
		}

		/**
		 * Temporary (hopefully)
		 * makes a java.awt.Polygon from a Floor object.
		 * @param floor
		 * @return a polygon representing the floor
		 */
		public static Polygon floorToVerticalPolygon(Floor floor){
			Point3D[] floorPoints = floor.getPoints();
			int[] xpoints = new int[floorPoints.length];
			int[] ypoints = new int[floorPoints.length];

			for ( int i = 0; i < floorPoints.length; i++){
				Point3D point = floorPoints[i];
				xpoints[i] = (int)point.getX();
				ypoints[i] = (int)point.getZ();
			}
			return new Polygon(xpoints, ypoints, floorPoints.length);
		}

		/**
		 * @param floor
		 * @return the center point or centroid of the floor
		 */
		public static Point3D getFloorCentroid(Floor floor){
			float xSum = 0;
			float ySum = 0;
			float zSum = 0;
			Point3D[] vertices = floor.getPoints();
			for (Point3D vertex : vertices){
				xSum += vertex.x;
				ySum += vertex.y;
				zSum += vertex.z;
			}
			return new Point3D(xSum/vertices.length, ySum/vertices.length, zSum/vertices.length);
		}

		/**
		 * Testing making many stars
		 * @return
		 */
		private static List<GameImage> makeStars(){
			List<GameImage> stars = new ArrayList<GameImage>();

			Random ran = new Random(RANDOM_SEED);

			int maxX = 500;
			int maxY = 500;
			int maxZ = 500;

			int minSize = 1;
			int maxSize = 20;

			int starCount = 1000;
			for (int starNum = 0; starNum < starCount; starNum++){
				float x = ran.nextInt(maxX*2)-maxZ;
				float y = ran.nextInt(maxY*2)-maxZ;
				float z = ran.nextInt(maxZ*2)-maxZ;

				int size = randomGen.nextInt(maxSize-minSize)+minSize;

				stars.add(new GameImage(Res.getImageFromName("Star1"), new Point3D(x,y,z), new Rectangle3D(size, size, size)));
			}
			return stars;
		}

		public static void resetColour(){
			randomGen = new Random(RANDOM_SEED);
		}

}
