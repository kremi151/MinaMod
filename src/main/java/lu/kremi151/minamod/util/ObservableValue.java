package lu.kremi151.minamod.util;

public interface ObservableValue<T> {

	T getValue();
	boolean hasChanged(boolean modifyState);
	
	default boolean hasChangedSinceLastCheck() {
		return hasChanged(true);
	}
}
