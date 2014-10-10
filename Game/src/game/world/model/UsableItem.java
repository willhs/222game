package game.world.model;

public abstract class UsableItem implements Item{

	@Override
	public boolean canUse(){
		return true;
	}

	/**
	 * Player uses the item.
	 */
	public abstract boolean use(Player player);
}