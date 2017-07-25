package lu.kremi151.minamod.util.nbtmath;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public abstract class SerializableNamedConstant extends SerializableFunctionBase<NBTTagString>{

	protected final String varName;

	private SerializableNamedConstant(String varName) {
		super(Context.DEFAULT);
		this.varName = varName;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString(varName);
	}

	public static SerializableNamedConstant createByName(String name) {
		return new Unprovided(name);
	}
	
	public static SerializableNamedConstant createAndProvide(String name, @Nonnull Number constant) {
		if(constant == null) {
			throw new NullPointerException();
		}
		return new Provided(constant, name);
	}
	
	private static class Provided extends SerializableNamedConstant{

		private final Number constant;
		
		private Provided(Number constant, String name) {
			super(name);
			this.constant = constant;
		}

		@Override
		public Number apply(Number t, Context c) {
			return constant;
		}
	}
	
	private static class Unprovided extends SerializableNamedConstant{
		
		private Unprovided(String name) {
			super(name);
		}

		@Override
		public Number apply(Number t, Context c) {
			Number constant = c.resolveConstant(varName);
			if(constant != null) {
				return constant;
			}else {
				throw new MathCalculationException("Unable to resolve constant named " + varName);
			}
		}
	}
	
}