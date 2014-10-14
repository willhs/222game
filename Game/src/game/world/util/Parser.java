package game.world.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Inventory;
import game.world.model.Player;

import java.util.Scanner;

/**
 * @author Shane Brewer 300289850
 */
public class Parser {

	public static String parseName(Scanner scan) {
		String name = "";
		scan.next();
		scan.next();

		name = scan.next();
		String temp = scan.next();
		while(!temp.equals(")")){
			name = name+" "+temp;
			temp = scan.next();
		}
		return name;
	}

	/**
	 * Parses a player from a set kind of string.
	 */
	public static Player parsePlayer(Scanner scan) {
		String name = "Unknowen";
		if (scan.hasNext("Name")) {
			name = Parser.parseName(scan);
		}
		Inventory inventory = new Inventory();
		if (scan.hasNext("Inventory")) {
			//inventory = Parser.parseInventory(scan);
		}
		Point3D point = new Point3D(0, 0, 0);
		if (scan.hasNext("Position")) {
			point = Parser.parsePosition(scan);
		}
		Rectangle3D boundingBox = new Rectangle3D(0, 0, 0);
		if (scan.hasNext("BoundingBox")) {
			boundingBox = Parser.parseBoundingBox(scan);
		}
		return new Player(name);
	}

	/**
	 * Parses a boundingBox from a set kind of string.
	 */
	public static Rectangle3D parseBoundingBox(Scanner scan) {
		while(!scan.hasNextDouble() && scan.hasNext()){
			scan.next();
		}
		float width = (float)scan.nextDouble();
		while(!scan.hasNextDouble() && scan.hasNext()){
			scan.next();
		}
		float length = (float)scan.nextDouble();
		while(!scan.hasNextDouble() && scan.hasNext()){
			scan.next();
		}
		float height = (float)scan.nextDouble();
		return new Rectangle3D(width, height, length);
	}

	/**
	 * Parses a Point from a set kind of string.
	 */
	public static Point3D parsePosition(Scanner scan) {
		while (!scan.hasNextDouble()&& scan.hasNext()){
			scan.next();
		}
		float x = (float)scan.nextDouble();
		while (!scan.hasNextDouble()&& scan.hasNext()){
			scan.next();
		}
		float y = (float)scan.nextDouble();
		while (!scan.hasNextDouble()&& scan.hasNext()){
			scan.next();
		}
		float z = (float)scan.nextDouble();
		return new Point3D(x, y, z);
	}

	/**
	 * Removes unneeded text form the string..
	 */
	public static void removeUnneedText(String textToRemoveUpto, Scanner scan){
		while (!scan.hasNext(textToRemoveUpto)) {
			scan.next();
		}
	}

}
