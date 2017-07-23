package lu.kremi151.minamod.util.weightedlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;

import lu.kremi151.minamod.interfaces.ICallback;

public interface WeightedList<T> extends Iterable<WeightedItem<T>>{

	/**
	 * Gets a random weighted element from this weighted list
	 * @param r The random number generator
	 * @return
	 */
	T randomElement(Random r);
	
	/**
	 * Returns an immutable view of this weighted list. If this list is already an immutable view of another list, it can return a reference to itself.
	 * @return
	 */
	ImmutableWeightedList<T> immutable();
	
	/**
	 * Returns a mutable view of this weighted list. If this list is already an mutable view of another list, it can return a reference to itself.
	 * @return
	 */
	MutableWeightedList<T> mutable();
	
	/**
	 * Returns the amount of items in this list
	 * @return
	 */
	int size();
	
	/**
	 * Returns the sum of each item's weight in this list
	 * @return
	 */
	double totalWeight();
	
	default double reduceWeight(double start, DoubleBinaryOperator reductor) {
		for(WeightedItem<T> i : this) {
			start = reductor.applyAsDouble(start, i.weight);
		}
		return start;
	}
	
	public static <T> MutableWeightedList<T> create(){
		return new MutableWeightedList<T>();
	}
	
	public static <T> MutableWeightedList<T> create(ICallback<List<WeightedItem<T>>> listFactory){
		return new MutableWeightedList<T>(listFactory);
	}
	
	public static <T> WeightedList<T> combine(WeightedList<T>... lists){
		MutableWeightedList<T> res = new MutableWeightedList<T>();
		if(lists.length == 0)return res;
		
		HashMap<T, Double> map = new HashMap<T, Double>();
		
		for(WeightedList<T> list : lists){
			for(WeightedItem<T> item : list){
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
	
	public static <T> WeightedList<T> from(WeightedItem<T>... items){
		for(int i = 0 ; i < items.length ; i++){
			if(items[i] == null)throw new NullPointerException("Cannot accept null values");
		}
		MutableWeightedList<T> wl = new MutableWeightedList<T>(() -> new ArrayList<WeightedItem<T>>(Arrays.asList(items)));
		wl.recalculateTotalWeight();
		return wl;
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
}
