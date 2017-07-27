package lu.kremi151.minamod.util;

import java.util.Objects;

public class ValueHolder<T> implements MutableObservableValue<T>{

	private T value = null;
	private boolean dirty = false;
	
	public ValueHolder() {}
	
	public ValueHolder(T startValue) {
		this.value = startValue;
	}
	
	@Override
	public boolean hasChanged(boolean modifyState) {
		return dirty;
	}

	@Override
	public void setValue(T newValue) {
		boolean changed = !Objects.equals(value, newValue);
		this.value = newValue;
		if(changed) {
			this.dirty = true;
		}
	}
	
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void unmarkChanged() throws UnsupportedOperationException {
		this.dirty = false;
	}
	
}
