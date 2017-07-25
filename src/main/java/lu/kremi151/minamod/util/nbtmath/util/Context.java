package lu.kremi151.minamod.util.nbtmath.util;

import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

public interface Context {
	
	@Nullable Number resolveConstant(String name);
	@Nullable UnaryOperator<Number> resolveVariable(String name);
	void applyMappings(BiConsumer<String,Object> consumer);

	final static Context DEFAULT = new Context() {

		@Override
		public Number resolveConstant(String name) {
			return null;
		}

		@Override
		public UnaryOperator<Number> resolveVariable(String name) {
			return null;
		}

		@Override
		public void applyMappings(BiConsumer<String, Object> consumer) {}
	
	};
}
