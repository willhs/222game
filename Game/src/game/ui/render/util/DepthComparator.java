package game.ui.render.util;

import java.util.Comparator;

/**
 * @author will
 *
 * compares depth components
 */
public class DepthComparator implements Comparator<DepthComparable> {

	@Override
	public int compare(DepthComparable o1, DepthComparable o2) {
		return Float.compare(o1.getDepth(), o2.getDepth());
	}

}
