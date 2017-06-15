package lu.kremi151.minamod.capabilities.stats;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

public class Stat {
	
	private final Value actual, training;

	public Stat(Value actual, Value training){
		this.actual = actual;
		this.training = training;
	}
	
	public Value getActual(){
		return actual;
	}
	
	public Value getTraining(){
		return training;
	}
	
	public int getMinTrainingValue(){
		return -Math.min(actual.get(), actual.minValue);
	}
	
	public int getMaxTrainingValue(){
		return actual.maxValue - actual.get();
	}
	
	public void initialize(){}
	
	public static class Value{
		
		private final IntSupplier getter, remaining;
		private final IntFunction<Integer> setter;
		private final int defaultValue, minValue, maxValue;
		private IntConsumer listener = null;
		
		public Value(IntSupplier getter, IntFunction<Integer> setter, int defaultValue){
			this(getter, setter, defaultValue, 0, -1);
		}
		
		public Value(IntSupplier getter, IntFunction<Integer> setter, int defaultValue, int minValue, int maxValue){
			this(getter, setter, defaultValue, 0, maxValue, () -> maxValue - getter.getAsInt());
		}
		
		public Value(IntSupplier getter, IntFunction<Integer> setter, int defaultValue, int minValue, int maxValue, IntSupplier remaining){
			if(defaultValue < minValue || (maxValue >= 0 && defaultValue > maxValue)){
				throw new IllegalArgumentException("The supplied defaultValue violates the bounds given by minValue and maxValue");
			}
			this.getter = getter;
			this.setter = setter;
			this.defaultValue = defaultValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.remaining = remaining;
		}
		
		public Value setListener(IntConsumer listener){
			this.listener = listener;
			return this;
		}
		
		public void set(int value){
			if(setter.apply(value) != value && listener != null){
				listener.accept(get());
			}
		}
		
		public int get(){
			return getter.getAsInt();
		}
		
		public void increment(int amount){
			setter.apply(getter.getAsInt() + amount);
			if(listener != null)listener.accept(get());
		}
		
		public void decrement(int amount){
			setter.apply(getter.getAsInt() - amount);
			if(listener != null)listener.accept(get());
		}
		
		public boolean canBeInfinite(){
			return maxValue < 0;
		}
		
		public int remaining(){
			return remaining.getAsInt();
		}
		
		public int getDefaultValue(){
			return defaultValue;
		}
		
		public void setToDefault(){
			set(defaultValue);
		}
		
		public int getMinValue(){
			return minValue;
		}
		
		public int getMaxValue(){
			return maxValue;
		}
		
		public static final Value ZERO = new Value(() -> 0, value -> 0, 0);
	}
}
