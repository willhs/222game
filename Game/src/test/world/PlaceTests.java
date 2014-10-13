package test.world;

import static org.junit.Assert.*;
import game.world.dimensions.Point3D;
import game.world.model.*;
import game.world.util.Drawable;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

public class PlaceTests {

	@Test
	public void chcekGetterTests1() {
		Place place = getAPlace();
		if (place.getExits() == null) {
			fail("A iterator of exits should never be null");
		}
		Iterator<Exit> iterator = place.getExits();
		while (iterator.hasNext()) {
			fail("No exits were pervided to the place");
		}
	}

	@Test
	public void chcekGetterTests2() {
		Place place = getAPlace();
		if (place.getPlayers() == null) {
			fail("A list of players should never be null");
		}
		Iterator<Player> iterator = place.getPlayers();
		while (iterator.hasNext()) {
			fail("No player were added to the place");
		}
	}

	@Test
	public void chcekGetterTests3() {
		Place place = getAPlace();
		if (place.getItems() == null) {
			fail("A List of items should never be null");
		}
		Iterator<Item> iterator = place.getItems();
		while (iterator.hasNext()) {
			fail("No Items were previded or added to the place");
		}
	}

	@Test
	public void testDrawables(){
		Place place = getAPlace();
		if (place.getDrawable() == null) {
			fail("A List of drawables should never be null");
		}
		Iterator<Drawable> iterator = place.getDrawable();
		while (iterator.hasNext()) {
			fail("No Drawables were added to the place");
		}
	}

	@Test
	public void testContains(){
		Place place = getAPlace();
		Point3D point = new Point3D(2,0,2);
		assertTrue(place.contains(point));
	}

	// ===============HelperMethods================//
	public Place getAPlace() {
		int[] xpoints = new int[]{0,200,200,0,0};
		int[] ypoints = new int[]{0,0,200,200,0};
		Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
		return new Room(new ArrayList<Item>(), new ArrayList<Enviroment>() , p, "Room1");
	}

}
