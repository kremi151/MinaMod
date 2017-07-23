package lu.kremi151.minamod.util.nbtmath.util;

import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public interface Context {
	
	Number getConstant(String name);
	UnaryOperator<Number> getVariable(String name);
	void applyMappings(BiConsumer<String,Object> consumer);

	final static Context DEFAULT = new Context() {

		@Override
		public Number getConstant(String name) {
			return null;
		}

		@Override
		public UnaryOperator<Number> getVariable(String name) {
			return null;
		}

		@Override
		public void applyMappings(BiConsumer<String, Object> consumer) {}
	
	};
}
