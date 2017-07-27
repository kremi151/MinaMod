package lu.kremi151.minamod.util;

public interface MutableObservableValue<T> extends ObservableValue<T> {

	void setValue(T value);
	void unmarkChanged() throws UnsupportedOperationException;
	
}
