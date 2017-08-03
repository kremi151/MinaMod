package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public abstract class SerializableNamedVariable extends SerializableFunctionBase<NBTTagString>{
	
	protected final String functionName;
	
	private SerializableNamedVariable(String functionName) {
		super(Context.DEFAULT);
		this.functionName = functionName;
	}
	
	@Override
	public NBTTagString serialize() {
		return new NBTTagString(functionName);
	}
	
	public static SerializableNamedVariable createByName(String name) {
		return new Unprovided(name);
	}
	
	public static SerializableNamedVariable createAndProvide(String name, @Nonnull UnaryOperator<Number> function) {
		if(function == null) {
			throw new NullPointerException();
		}
		return new Provided(function, name);
	}
	
	private static class Provided extends SerializableNamedVariable{
		
		private final UnaryOperator<Number> function;
		
		private Provided(UnaryOperator<Number> function, String name) {
			super(name);
			this.function = function;
		}

		@Override
		public Number apply(Number t, Context c) {
			return function.apply(t);
		}
	}
	
	private static class Unprovided extends SerializableNamedVariable{
		
		private Unprovided(String name) {
			super(name);
		}

		@Override
		public Number apply(Number t, Context c) {
			UnaryOperator<Number> function = c.resolveVariable(functionName);
			if(function != null) {
				return function.apply(t);
			}else {
				throw new MathCalculationException("Unable to resolve function named " + functionName);
			}
		}
	}

}