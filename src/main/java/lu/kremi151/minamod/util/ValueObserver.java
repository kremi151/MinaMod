package lu.kremi151.minamod.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.base.Objects;

public class ValueObserver<T> implements ObservableValue<T>{
	
	private final Supplier<T> getter;
	private T lastValue = null;
	
	public ValueObserver(@Nonnull Supplier<T> getter) {
		if(getter == null) {
			throw new NullPointerException();
		}
		this.getter = getter;
	}

	@Override
	public T getValue() {
		return getter.get();
	}

	@Override
	public boolean hasChanged(boolean modifyState) {
		T actual = getter.get();
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
