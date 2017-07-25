package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class SerializableFunction extends SerializableFunctionBase<NBTTagCompound>{

	private final String functionName;
	protected final SerializableFunctionBase<? extends NBTBase>[] args;
	
	SerializableFunction(String functionName, SerializableFunctionBase<? extends NBTBase>... args) throws MathFunctionException{
		super(Context.DEFAULT);
		this.functionName = functionName;
		this.args = args;
	}
	
	@Override
	public final NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList args = new NBTTagList();
		for(SerializableFunctionBase<? extends NBTBase> f : this.args) {
			NBTTagCompound fnbt = new NBTTagCompound();
			fnbt.setTag("Arg", f.serialize());
			args.appendTag(fnbt);
		}
		nbt.setTag("Arguments", args);
		nbt.setString("Function", functionName);
		return nbt;
	}
	
	public static class Absolute extends SerializableFunction{

		public Absolute(SerializableFunctionBase<? extends NBTBase> arg) throws MathFunctionException{
			super("abs", new SerializableFunctionBase[] {arg});
		}

		@Override
		public Number apply(Number t, Context c) {
			return Math.abs(args[0].apply(t, c).doubleValue());
		}
		
	}
	
	public static class Negate extends SerializableFunction{

		public Negate(SerializableFunctionBase<? extends NBTBase> arg) throws MathFunctionException{
			super("neg", new SerializableFunctionBase[] {arg});
		}

		@Override
		public Number apply(Number t, Context c) {
			return -args[0].apply(t, c).doubleValue();
		}
		
	}
	
	public static class Max extends SerializableFunction{

		public Max(SerializableFunctionBase<? extends NBTBase>... args) throws MathFunctionException {
			super("max", args);
			if(args.length == 0) {
				throw new MathFunctionException("No arguments present");
			}
		}

		@Override
		public Number apply(Number t, Context c) {
			double max = args[0].apply(t, c).doubleValue();
			for(int i = 1 ; i < args.length ; i++) {
				double tmp = args[i].apply(t, c).doubleValue();
				if(tmp > max)max = tmp;
			}
			return max;
		}
		
	}
	
	public static class Min extends SerializableFunction{

		public Min(SerializableFunctionBase<? extends NBTBase>... args) throws MathFunctionException {
			super("min", args);
			if(args.length == 0) {
				throw new MathFunctionException("No arguments present");
			}
		}

		@Override
		public Number apply(Number t, Context c) {
			double min = args[0].apply(t, c).doubleValue();
			for(int i = 1 ; i < args.length ; i++) {
				double tmp = args[i].apply(t, c).doubleValue();
				if(tmp < min)min = tmp;
			}
			return min;
		}
		
	}
	
	public static class Sum extends SerializableFunction{

		public Sum(SerializableFunctionBase<? extends NBTBase>[] args) throws MathFunctionException {
			super("sum", args);
		}

		@Override
		public Number apply(Number t, Context c) {
			double sum = 0.0;
			for(SerializableFunctionBase<? extends NBTBase> f : args) {
				sum += f.apply(t, c).doubleValue();
			}
			return sum;
		}
		
	}
	
	public static class Product extends SerializableFunction{

		public Product(SerializableFunctionBase<? extends NBTBase>[] args) throws MathFunctionException {
			super("product", args);
		}

		@Override
		public Number apply(Number t, Context c) {
			if(args.length > 0) {
				double sum = 1.0;
				for(SerializableFunctionBase<? extends NBTBase> f : args) {
					sum *= f.apply(t, c).doubleValue();
				}
				return sum;
			}else {
				return 0.0;
			}
		}
		
	}
	
}