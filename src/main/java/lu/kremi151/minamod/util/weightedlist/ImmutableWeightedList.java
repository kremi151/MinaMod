package lu.kremi151.minamod.util.weightedlist;

import java.util.Iterator;
import java.util.Random;

public final class ImmutableWeightedList<T> implements WeightedList<T> {

	private final WeightedList<T> wrapped;
	
	ImmutableWeightedList(WeightedList<T> wrapped){
		this.wrapped = wrapped;
	}
	
	@Override
	public Iterator<WeightedItem<T>> iterator() {
		return wrapped.iterator();
	}
	
	public T randomElement(Random r){
		return wrapped.randomElement(r);
	}

	@Override
	public ImmutableWeightedList<T> immutable() {
		return this;
	}

	@Override
	public MutableWeightedList<T> mutable() {
		return (MutableWeightedList<T>) WeightedList.combine(wrapped);
	}

}
