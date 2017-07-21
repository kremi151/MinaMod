package lu.kremi151.minamod.util.weightedlist;

import java.util.Iterator;
import java.util.List;

public class WeightedListIterator<T> implements Iterator<WeightedItem<T>>{
	
	private final List<WeightedItem<T>> items;
	private int index = -1;
	
	WeightedListIterator(List<WeightedItem<T>> items){
		this.items = items;
	}

	@Override
	public boolean hasNext() {
		return index < items.size() - 1;
	}

	@Override
	public WeightedItem<T> next() {
		return items.get(++index);
	}

}
