package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lu.kremi151.minamod.interfaces.ICallback;

public class WeightedList<T> implements Cloneable, Iterable<WeightedList.WeightedItem<T>>{
	
	private final List<WeightedItem<T>> items;
	private boolean locked = false;
	private double totalWeight = 0;
	
	public WeightedList(ICallback<List<WeightedItem<T>>> listFactory){
		this.items = listFactory.callback();
	}
	
	public WeightedList(){
		this.items = new ArrayList<WeightedItem<T>>();
	}
	
	public boolean add(T obj, double weight){
		if(locked){
			throw new RuntimeException("This WeightedList is locked");
		}
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
	
	public void lock(){
		this.locked = true;
	}
	
	private void recalculateTotalWeight(){
		totalWeight = 0.0;
		for(int i = 0 ; i < items.size() ; i++){
			totalWeight += items.get(i).weight;
		}
	}
	
	public static <T> WeightedList<T> from(WeightedItem<T>... items){
		for(int i = 0 ; i < items.length ; i++){
			if(items[i] == null)throw new NullPointerException("Cannot accept null values");
		}
		WeightedList<T> wl = new WeightedList<T>(() -> new ArrayList<WeightedItem<T>>(Arrays.asList(items)));
		wl.recalculateTotalWeight();
		return wl;
	}
	
	@Override
	public WeightedList<T> clone(){
		WeightedList<T> res = new WeightedList<T>();
		for(WeightedItem<T> item : items){
			res.add(item.obj, item.weight);
		}
		return res;
	}
	
	public static <T> WeightedList<T> combine(WeightedList<T>... lists){
		WeightedList<T> res = new WeightedList<T>();
		if(lists.length == 0)return res;
		
		HashMap<T, Double> map = new HashMap<T, Double>();
		
		for(WeightedList<T> list : lists){
			for(WeightedItem<T> item : list.items){
				if(map.containsKey(item.obj)){
					double val = map.get(item.obj);
					val += item.weight;
					map.put(item.obj, val);
				}else{
					map.put(item.obj, item.weight);
				}
			}
		}
		
		for(Map.Entry<T, Double> e : map.entrySet()){
			res.add(e.getKey(), e.getValue());
		}
		
		return res;
	}
	
	public static class WeightedItem<T> implements Cloneable{
		public final T obj;
		public final double weight;
		
		public WeightedItem(T obj, double weight){
			this.obj = obj;
			this.weight = weight;
		}
		
		@Override
		public WeightedItem<T> clone(){
			return new WeightedItem<T>(obj, weight);
		}
		
		public boolean objectEqual(T obj){
			return this.obj.equals(obj);
		}
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
	
	/**
	 * A builder class to create a distributed weighted list. Each element belongs to a section having a specific percentage of the entire list and the weight of each element of a section is calculated to fit in that section.
	 * @author michm
	 *
	 * @param <T> The type of elements
	 */
	public static class DistributedBuilder<T>{
		
		private double percentageLeft = 100.0;
		private double currentPercentage = -1.0, currentWeight = 0.0;
		
		private ArrayList<WeightedItem<T>> result, current;
		
		public DistributedBuilder(){
			result = new ArrayList<WeightedItem<T>>();
			current = new ArrayList<WeightedItem<T>>();
		}
		
		/**
		 * Begins a new section (with an overall of the given percentage) and ends the previous one if one has been started
		 * @param percentage The percentage for this section. All section percentages combined have to be in between 0 and 100.
		 * @return The current builder
		 */
		public DistributedBuilder beginSection(double percentage){
			if(percentage <= 0){
				throw new RuntimeException("The percentage has to be positive and greater than 0");
			}else if(percentageLeft - percentage < 0){
				throw new RuntimeException("Percentage limit of 100% exceeded");
			}
			if(currentPercentage >= 0){
				processCurrent();
			}
			currentPercentage = percentage;
			currentWeight = 0.0;
			return this;
		}
		
		public DistributedBuilder add(T item, double weight){
			if(currentPercentage < 0){
				currentPercentage = 100.0;
				percentageLeft = 0.0;
				currentWeight = 0.0;
			}
			current.add(new WeightedItem(item, weight));
			currentWeight += weight;
			return this;
		}
		
		public WeightedList<T> build(){
			if(currentPercentage >= 0){
				processCurrent();
			}
			int l = result.size();
			WeightedItem<T> wia[] = new WeightedItem[l];
			wia = result.toArray(wia);
			return WeightedList.from(wia);
		}
		
		private void processCurrent(){
			for(WeightedItem<T> wi : current){
				double weight = wi.weight / currentWeight;
				weight *= currentPercentage;
				result.add(new WeightedItem(wi.obj, weight));
			}
			current.clear();
		}
		
	}

	@Override
	public Iterator<WeightedList.WeightedItem<T>> iterator() {
		return new WeightedIterator<T>(items);
	}
	
	private static class WeightedIterator<T> implements Iterator<WeightedItem<T>>{
		
		private final List<WeightedItem<T>> items;
		private int index = -1;
		
		private WeightedIterator(List<WeightedItem<T>> items){
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
}
