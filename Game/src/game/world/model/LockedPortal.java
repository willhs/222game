package game.world.model;


import game.world.dimensions.*;

/**
 * @author Shane Brewer 300289850
 */
public class LockedPortal extends Exit{

	private final String name;
	private boolean isLocked = true;
	private int keysToOpen;
	private static final Rectangle3D boundingBox = new Rectangle3D(30, 60, 30);

	public LockedPortal(String name, Place placeOne, Point3D positionInPlaceOne,
		Place placeTwo, Point3D positionInPlaceTwo) {
		super(placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo);
		this.name = name;
		keysToOpen = 1;
	}

	public LockedPortal(String name, Place placeOne, Point3D positionInPlaceOne,
			Place placeTwo, Point3D positionInPlaceTwo, int keyAmount) {
			super(placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo);
			this.name = name;
			keysToOpen = keyAmount;
	}

	@Override
	public Rectangle3D getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getImageName() {
		return isLocked() ? "teleporter_off" : "teleported_on";
	}

	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public boolean unlock(Inventory inventory) {
		int count = keysToOpen;
		for(Item i: inventory){
			if (i instanceof Crystal){
				System.out.println(count);
				count--;
			}
		}
		isLocked = !(count <= 0);
		return count <= 0;
	}

}
