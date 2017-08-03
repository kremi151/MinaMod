package lu.kremi151.minamod.util.nbtmath.util;

@FunctionalInterface
public interface ToBooleanFunction<T> {

	boolean applyAsBoolean(T obj);
	
}
