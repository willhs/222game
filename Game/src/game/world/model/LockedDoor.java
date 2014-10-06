package game.world.model;


import game.world.dimensions.*;

public class LockedDoor extends Exit{
	
	private final String name;
	private final String imageName;
	private boolean isLocked = true;
	private int keysToOpen = 1;
	private static final Rectangle3D boundingBox = new Rectangle3D(30, 60, 30);

	public LockedDoor(String name, Place placeOne, Point3D positionInPlaceOne,
		Place placeTwo, Point3D positionInPlaceTwo) {
		super(placeOne, positionInPlaceOne, placeTwo, positionInPlaceTwo);
		this.name = name;
		this.imageName = "Door";
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
		return imageName;
	}

	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public boolean unlock(Inventory inventory) {
		int count = keysToOpen;
		for(Item i: inventory){
			if (i instanceof Key){
				System.out.println(count);
				count--;
			}
		}
		isLocked = !(count <= 0);
		return count <= 0;
	}

}
