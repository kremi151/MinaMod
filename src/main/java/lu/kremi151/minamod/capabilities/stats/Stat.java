package lu.kremi151.minamod.capabilities.stats;

import java.util.function.IntConsumer;
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
	
	public static class Value{
		
		private final IntSupplier getter, remaining;
		private final IntConsumer setter;
		private final int maxValue;
		private IntConsumer listener = null;
		
		public Value(IntSupplier getter, IntConsumer setter){
			this(getter, setter, -1);
		}
		
		public Value(IntSupplier getter, IntConsumer setter, int maxValue){
			this(getter, setter, -1, () -> maxValue - getter.getAsInt());
		}
		
		public Value(IntSupplier getter, IntConsumer setter, int maxValue, IntSupplier remaining){
			this.getter = getter;
			this.setter = setter;
			this.maxValue = maxValue;
			this.remaining = remaining;
		}
		
		public Value setListener(IntConsumer listener){
			this.listener = listener;
			return this;
		}
		
		public void set(int value){
			setter.accept(value);
			if(listener != null)listener.accept(get());
		}
		
		public int get(){
			return getter.getAsInt();
		}
		
		public void increment(int amount){
			setter.accept(getter.getAsInt() + amount);
			if(listener != null)listener.accept(get());
		}
		
		public void decrement(int amount){
			setter.accept(getter.getAsInt() - amount);
			if(listener != null)listener.accept(get());
		}
		
		public boolean canBeInfinite(){
			return maxValue < 0;
		}
		
		public int remaining(){
			return remaining.getAsInt();
		}
		
		public int getMaxValue(){
			return maxValue;
		}
		
		public static final Value ZERO = new Value(() -> 0, value -> {}, 0);
	}
}
