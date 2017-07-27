package lu.kremi151.minamod.util;

import javax.annotation.Nonnull;

import com.google.common.base.Objects;

import lu.kremi151.minamod.interfaces.ICallback;

public class ValueObserver<T> implements ObservableValue<T>{
	
	private final ICallback<T> getter;
	private T lastValue = null;
	
	public ValueObserver(@Nonnull ICallback<T> getter) {
		if(getter == null) {
			throw new NullPointerException();
		}
		this.getter = getter;
	}

	@Override
	public T getValue() {
		return getter.callback();
	}

	@Override
	public boolean hasChanged(boolean modifyState) {
		T actual = getter.callback();
		if(!Objects.equal(lastValue, actual)){
			if(modifyState) {
				this.lastValue = actual;
			}
			return true;
		}else {
			return false;
		}
	}

}
