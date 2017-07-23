package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class SerializableNamedFunction extends SerializableFunction<NBTTagCompound>{

	private final String functionName;
	protected final SerializableFunction<? extends NBTBase>[] args;
	
	SerializableNamedFunction(String functionName, SerializableFunction<? extends NBTBase>... args) throws MathFunctionException{
		super(Context.DEFAULT);
		this.functionName = functionName;
		this.args = args;
	}
	
	@Override
	public final NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList args = new NBTTagList();
		for(SerializableFunction<? extends NBTBase> f : this.args) {
			NBTTagCompound fnbt = new NBTTagCompound();
			fnbt.setTag("Arg", f.serialize());
			args.appendTag(fnbt);
		}
		nbt.setTag("Arguments", args);
		nbt.setString("Function", functionName);
		return nbt;
	}
	
	public static class Absolute extends SerializableNamedFunction{

		public Absolute(SerializableFunction<? extends NBTBase> arg) throws MathFunctionException{
			super("abs", new SerializableFunction[] {arg});
		}

		@Override
		public Number apply(Number t) {
			return Math.abs(args[0].apply(t).doubleValue());
		}
		
	}
	
	public static class Negate extends SerializableNamedFunction{

		public Negate(SerializableFunction<? extends NBTBase> arg) throws MathFunctionException{
			super("neg", new SerializableFunction[] {arg});
		}

		@Override
		public Number apply(Number t) {
			return -args[0].apply(t).doubleValue();
		}
		
	}
	
	public static class Max extends SerializableNamedFunction{

		public Max(SerializableFunction<? extends NBTBase>... args) throws MathFunctionException {
			super("max", args);
			if(args.length == 0) {
				throw new MathFunctionException("No arguments present");
			}
		}

		@Override
		public Number apply(Number t) {
			double max = args[0].apply(t).doubleValue();
			for(int i = 1 ; i < args.length ; i++) {
				double tmp = args[i].apply(t).doubleValue();
				if(tmp > max)max = tmp;
			}
			return max;
		}
		
	}
	
	public static class Min extends SerializableNamedFunction{

		public Min(SerializableFunction<? extends NBTBase>... args) throws MathFunctionException {
			super("min", args);
			if(args.length == 0) {
				throw new MathFunctionException("No arguments present");
			}
		}

		@Override
		public Number apply(Number t) {
			double min = args[0].apply(t).doubleValue();
			for(int i = 1 ; i < args.length ; i++) {
				double tmp = args[i].apply(t).doubleValue();
				if(tmp < min)min = tmp;
			}
			return min;
		}
		
	}
	
}