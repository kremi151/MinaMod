package lu.kremi151.minamod.util.nbtmath;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class SerializableNamedFunction extends SerializableFunction<NBTTagCompound>{

	private final String functionName;
	protected final SerializableFunction<? extends NBTBase>[] args;
	
	SerializableNamedFunction(String functionName, SerializableFunction<? extends NBTBase>... args) throws MathFunctionException{
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
		return null;
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
	
}