package game.ui.render.levelmaker;

import game.ui.render.Renderer;
import game.ui.render.util.GamePolygon;
import game.ui.render.util.Transform;
import game.ui.render.util.Trixel;
import game.ui.render.util.TrixelFace;
import game.ui.render.util.TrixelUtil;
import game.ui.render.util.Trixition;
import game.ui.render.util.ZComparator;
import game.world.dimensions.Point3D;
import game.world.dimensions.Vector3D;
import game.world.util.Floor;

import java.awt.Color;
import java.io.File;
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
 * Used for designing levels for the 222game
 * Provides a GUI for building a collection of trixels to define the physical layout of a level.
 * TODO: Save the level in a file for use in the game.
 */
public class LevelMaker{

	/**
	 * the amount in which to rotate all trixels (so that it can be updated)
	 */
	private Vector3D rotateAmounts = new Vector3D(0,0,0);
	/**
	 * all faces which have been rotated
	 */
	private List<TrixelFace> rotatedFaces;

	/**
	 * The last transform used to transform trixels.
	 * It can be inverted to find the true location of something which has already been transformed.
	 */
	private Transform lastTransform;
	/**
	 * all trixels in the level
	 */
	private Set<Trixel> trixels;
	/**
	 * the center of all trixels
	 */
	private Point3D trixelsCentroid;
	/**
	 * the colour that new trixels will be made in
	 */
	private Color colour;
	/**
	 * is eraser mode on or off
	 */
	private boolean eraser;

	private int flipY = 600;

	public LevelMaker(){

		// initialise trixels to make up a floor.
		trixels = new HashSet<Trixel>();

		Renderer.resetColour();
		Floor floor = makeFloor();

		for (Trixel t : TrixelUtil.polygon2DToTrixels(
				Renderer.floorToVerticalPolygon(floor), -1)){
			trixels.add(t);
		}

		// initialise colour
		colour = Color.WHITE;

		// initialise lastTransform with no rotation
		lastTransform = makeTransform(new Vector3D(0,0,0));

		//intilise rotatedFaces
		rotatedFaces = new ArrayList<TrixelFace>();
		updateRotation(0, 0);
	}

	/**
	 * makes a room for use
	 * @return
	 */
	private Floor makeFloor() {
		int[] x = new int[]{100,300,300,100};
		int[] z = new int[]{100,100,800,800};

		Point3D[] points = new Point3D[x.length];
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], 0, z[i]);
		}

		return new Floor(points);
	}

	/**
	 * @param rotateX : how much to rotate in x direction in radians
	 * @param rotateY : ^ y direction...
	 */
	Vector3D changeRotateAmount(int rotateX, int rotateY) {
		float rotateSpeed = 0.01f;
		return rotateAmounts.plus(
				new Vector3D(rotateX*rotateSpeed, rotateY*rotateSpeed, 0)
		);
	}

	/**
	 * @return all trixels to be drawn
	 */
	Iterator<Trixel> getTrixels(){
		return trixels.iterator();
	}

	/**
	 * Rotates trixels
	 * @param rotateX
	 * @param rotateY
	 */
	void updateRotation(int rotateX, int rotateY){

		rotateAmounts = changeRotateAmount(rotateX, rotateY);
		Transform trans = makeTransform(rotateAmounts);

		updateTrixelFaces();

		lastTransform = trans;
	}


	/**
	 * Makes a combined transform matrix involving several factors including
	 * @return
	 */
	Transform makeTransform(Vector3D rotateAmounts) {

		trixelsCentroid  = TrixelUtil.findTrixelsCentroid(trixels.iterator());
		Vector3D viewTranslation = Renderer.STANDARD_VIEW_TRANSLATION;
		return Renderer.makeTransform(rotateAmounts, trixelsCentroid, viewTranslation);
	}

	/**
	 * Searches whether x,y is within a trixel face.
	 * If x,y in a trixel face, make a new trixel next to that face and add it to the level.
	 * @param x
	 * @param y
	 */
	void dealWithMouseClick(int x, int y) {

		y = flipY - y; // flip y value

		TrixelFace face = getTrixelFaceAtViewPoint(x, y);

		if (face == null)	return;

		Trixel trixel = face.getParentTrixel();
		if (eraser){ // erase the trixel
			trixels.remove(trixel);
		}
		else { // make a new trixel next to this trixel
			trixels.add(makeTrixelNextToFace(face, colour));
		}
		updateTrixelFaces();
	}

	private void updateTrixelFaces() {
		// reset rotated trixels
		rotatedFaces = new ArrayList<TrixelFace>();

		for (Trixel trixel : trixels){
			for (TrixelFace face : TrixelUtil.makeTrixelFaces(trixel)){
				face.transform(lastTransform);
				rotatedFaces.add(face);
			}
		}

		// sort in order of closest trixels
		Collections.sort(rotatedFaces, new ZComparator());
		Collections.reverse(rotatedFaces);
	}

	TrixelFace getTrixelFaceAtViewPoint(int x, int y){
		for (TrixelFace face : rotatedFaces){
			GamePolygon facePoly = Renderer.makeGamePolygonFromTrixelFace(face);
			if (facePoly.contains(x, y)){
				return face;
			}
		}
		return null;
	}

	/**
	 * The trixel will be made directly next to neighbourFace.
	 *
	 * @param neighbourFace - the TrixelFace directly next to the trixel to be made
	 */
	Trixel makeTrixelNextToFace(TrixelFace neighbourFace, Color colour) {

		// find the true (unrotated) normal vector of the face by reversing the transform that was applied
		neighbourFace.transform(lastTransform.inverse());
		//Vector3D normal = neighbourFace.calculateNormal().unitVector();
		Vector3D normal = new Vector3D(0,1,0);
		Trixition faceTrixition = neighbourFace.getParentTrixel().getTrixition();
		Point3D faceRealPosition = TrixelUtil.trixitionToPosition(faceTrixition);
		// shift position by normal direction * trixel size, this will be at the position of a new trixel
		Point3D newTrixelPosition = faceRealPosition.getTranslatedPoint(normal.makeScaled(Trixel.SIZE));
		Trixition newTrixition = TrixelUtil.positionToTrixition(newTrixelPosition);

		/*System.out.println("new trixel position"+newTrixelPosition);
		System.out.println("normal: "+normal);
		System.out.println("trixition "+newTrixition);*/
		//System.out.println("center: "+centroid);

		return new Trixel(newTrixition, colour);
	}

	Transform getLastTransform() {
		return lastTransform;
	}

	/**
	 * writes all trixels to a file
	 */
	void writeTrixelsToFile(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		final int USER_SELECTION = chooser.showSaveDialog(null);

		File fileToSave;

		if (USER_SELECTION == JFileChooser.APPROVE_OPTION){
			fileToSave =  chooser.getSelectedFile();
		} else return;

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileToSave, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Trixel trixel : trixels){
			writer.println(trixel);
		}

		writer.close();
	}

	/**
	 * sets the colour of the next trixel
	 * @param colour
	 */
	public void setColour(Color colour){
		this.colour = colour;
	}

	/**
	 * toggles eraser mode on or off
	 */
	public void toggleEraser() {
		eraser = !eraser;
	}

	public boolean getIsEraserModeOn(){
		return eraser;
	}

}
