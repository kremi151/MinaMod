package lu.kremi151.minamod.util.weightedlist;

public class WeightedItem<T> implements Cloneable{
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
