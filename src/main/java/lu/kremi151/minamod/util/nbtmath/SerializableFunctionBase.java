package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;

public abstract class SerializableFunctionBase<NBTType extends NBTBase> implements UnaryOperator<Number>{
	
	private final Context context;
	
	SerializableFunctionBase(Context context){
		this.context = context;
	}
	
	protected final Context getContext() {
		return context;
	}

	public abstract NBTType serialize();
	
	@Override
	public final Number apply(Number t) {
		return apply(t, Context.DEFAULT);
	}
	
	public abstract Number apply(Number t, Context c);
	
}