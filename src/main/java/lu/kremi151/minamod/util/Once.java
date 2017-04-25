package lu.kremi151.minamod.util;

import javax.annotation.Nonnull;

public class Once<T> {

	private T obj = null;
	
	private Once(T obj){
		this.obj = obj;
	}
	
	public void set(@Nonnull T obj){
		if(this.obj == null){
			this.obj = obj;
		}else{
			throw new UnsupportedOperationException("Error thrown at trying to override a value in a once wrapper class");
		}
	}
	
	public T get(){
		return this.obj;
	}
	
	public boolean isSet(){
		return obj != null;
	}
	
	public static <T> Once<T> ready(){
		return new Once<T>(null);
	}
	
	public static <T> Once<T> alreadySet(T obj){
		return new Once<T>(obj);
	}
}
