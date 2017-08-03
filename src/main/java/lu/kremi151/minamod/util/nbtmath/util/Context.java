package lu.kremi151.minamod.util.nbtmath.util;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

public interface Context {
	
	@Nullable Number resolveConstant(String name);
	@Nullable UnaryOperator<Number> resolveVariable(String name);
	@Nullable ToBooleanFunction<Number> resolveLogic(String name);
	Set<String> constantNameSet();
	Set<String> variableNameSet();
	Set<String> logicNameSet();

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
		public ToBooleanFunction<Number> resolveLogic(String name) {
			return null;
		}

		@Override
		public Set<String> constantNameSet() {
			return Collections.emptySet();
		}

		@Override
		public Set<String> variableNameSet() {
			return Collections.emptySet();
		}

		@Override
		public Set<String> logicNameSet() {
			return Collections.emptySet();
		}
	
	};
}
