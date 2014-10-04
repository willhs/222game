package game.world.util;

import game.world.dimensions.Point3D;
import game.world.dimensions.Rectangle3D;
import game.world.model.Inventory;
import game.world.model.Player;

import java.util.Scanner;

public class Parser {

	public static String parseName(Scanner scan) {
		String name = "";
		scan.next();
		scan.next();
		name = scan.next();
		scan.next();
		return name;
	}

	public static Player parsePlayer(Scanner scan) {
		String name = "Unknowen";
		if (scan.hasNext("Name")) {
			name = Parser.parseName(scan);
		}
		Inventory inventory = new Inventory();
		if (scan.hasNext("Inventory")) {
			inventory = Parser.parseInventory(scan);
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

	public static Inventory parseInventory(Scanner scan) {
		return null;
	}
	
	public static void removeUnneedText(String textToRemoveUpto, Scanner scan){
		while (!scan.hasNext(textToRemoveUpto)) {
			scan.next();
		}
	}

}
