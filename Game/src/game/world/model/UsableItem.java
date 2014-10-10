package game.world.model;

public abstract class UsableItem extends Item{
	
	@Override
	public boolean canUse(){
		return true;
	}

	/**
	 * Player uses the item.
	 */
	public abstract boolean use(Player player);
}