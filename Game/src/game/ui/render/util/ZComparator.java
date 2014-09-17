package game.ui.render.util;

import java.util.Comparator;

/**
 * @author will
 * compares z components
 */
public class ZComparator implements Comparator<ZComparable> {

	@Override
	public int compare(ZComparable o1, ZComparable o2) {
		return Float.compare(o1.getZ(), o2.getZ());
	}

}
