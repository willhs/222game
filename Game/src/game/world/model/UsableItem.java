package game.world.model;

import game.world.dimensions.Point3D;

/**
 * @author Shane Brewer 300289850
 */
public abstract class UsableItem extends Item{

	public UsableItem(String name, Point3D position){
		super(name, position);
	}

	@Override
	public boolean canUse(){
		return true;
	}

	/**
	 * Player uses the item.
	 */
	public abstract boolean use(Player player);
}