package lu.kremi151.minamod.util.weightedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.interfaces.ICallback;

public final class MutableWeightedList<T> implements WeightedList<T>{
	
	private final List<WeightedItem<T>> items;
	private double totalWeight = 0;
	
	MutableWeightedList(ICallback<List<WeightedItem<T>>> listFactory){
		this.items = listFactory.callback();
	}
	
	MutableWeightedList(){
		this.items = new ArrayList<WeightedItem<T>>();
	}
	
	public boolean add(T obj, double weight){
		items.add(new WeightedItem<T>(obj, weight));
		totalWeight += weight;
		return true;
	}
	
	public T randomElement(Random r){
		double w = totalWeight * r.nextDouble();
		for(int i = 0 ; i < items.size() ; i++){
			WeightedItem<T> p = items.get(i);
			w -= p.weight;
			if(w <= 0.0d){
				return p.obj;
			}
		}
		return items.get(r.nextInt(items.size())).obj;
	}
	
	void recalculateTotalWeight(){
		totalWeight = 0.0;
		for(int i = 0 ; i < items.size() ; i++){
			totalWeight += items.get(i).weight;
		}
	}
	
	@Override
	public WeightedList<T> clone(){
		MutableWeightedList<T> res = new MutableWeightedList<T>();
		for(WeightedItem<T> item : items){
			res.add(item.obj, item.weight);
		}
		return res;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("WeightedList[");
		for(int i = 0 ; i < items.size() ; i++){
			WeightedItem<T> item = items.get(i);
			if(i > 0)sb.append(", ");
			sb.append("{item=");
			sb.append(item.obj);
			sb.append(", weight=");
			sb.append(item.weight);
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Iterator<WeightedItem<T>> iterator() {
		return new WeightedListIterator(items);
	}

	@Override
	public ImmutableWeightedList<T> immutable() {
		return new ImmutableWeightedList<T>(this);
	}

	@Override
	public MutableWeightedList<T> mutable() {
		return this;
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public double totalWeight() {
		return totalWeight;
	}
}
