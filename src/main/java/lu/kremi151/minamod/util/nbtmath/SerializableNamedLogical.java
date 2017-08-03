package lu.kremi151.minamod.util.nbtmath;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.nbtmath.util.ILogical;
import lu.kremi151.minamod.util.nbtmath.util.ToBooleanFunction;
import net.minecraft.nbt.NBTTagString;

public abstract class SerializableNamedLogical extends SerializableFunctionBase<NBTTagString> implements ILogical<NBTTagString>{

	protected final String name;
	
	private SerializableNamedLogical(String name) {
		super(Context.DEFAULT);
		this.name = name;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString(name);
	}

	public static SerializableNamedLogical createByName(String name) {
		return new Unprovided(name);
	}
	
	public static SerializableNamedLogical createAndProvide(String name, @Nonnull ToBooleanFunction<Number> logic) {
		if(logic == null) {
			throw new NullPointerException();
		}
		return new Provided(logic, name);
	}
	
	private static class Provided extends SerializableNamedLogical{
		
		private final ToBooleanFunction<Number> logic;
		
		private Provided(ToBooleanFunction<Number> logic, String name) {
			super(name);
			this.logic = logic;
		}

		@Override
		public Number apply(Number t, Context c) {
			return evaluate(t, c) ? 1 : 0;
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return logic.applyAsBoolean(t);
		}
	}
	
	private static class Unprovided extends SerializableNamedLogical{
		
		private Unprovided(String name) {
			super(name);
		}

		@Override
		public Number apply(Number t, Context c) {
			return evaluate(t, c) ? 1 : 0;
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			ToBooleanFunction<Number> function = c.resolveLogic(name);
			if(function != null) {
				return function.applyAsBoolean(t);
			}else {
				throw new MathCalculationException("Unable to resolve logic named " + name);
			}
		}
	}

}
