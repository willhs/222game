package game.world.model;


import game.world.dimensions.*;

/**
 * @author Shane Brewer 300289850
 */
public class LockedPortal extends Exit{

	private final String name;
	private boolean isLocked;
	private int keysToOpen;
	private static final Rectangle3D boundingBox = new Rectangle3D(30, 60, 30);

	public LockedPortal(String name, Place placeOne, Point3D positionInPlaceOne,
		Place placeTwo, Point3D positionInPlaceTwo) {
		super(placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo);
		this.name = name;
		isLocked = true;
		keysToOpen = 1;
	}

	public LockedPortal(String name, Place placeOne, Point3D positionInPlaceOne,
			Place placeTwo, Point3D positionInPlaceTwo, int keyAmount) {
			super(placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo);
			this.name = name;
			isLocked = true;
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
		return isLocked ? "teleport_off":"teleporter_on";
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
				count--;
			}
		}
		isLocked = !(count <= 0);
		System.out.println(getImageName());
		return count <= 0;
	}

	@Override
	public void setLocked(boolean change) {
		isLocked = change;
	}

}
