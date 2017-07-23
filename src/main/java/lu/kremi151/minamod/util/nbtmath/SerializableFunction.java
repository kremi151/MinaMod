package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;

public abstract class SerializableFunction<NBTType extends NBTBase> implements UnaryOperator<Number>{
	
	private final Context context;
	
	SerializableFunction(Context context){
		this.context = context;
	}
	
	protected final Context getContext() {
		return context;
	}

	public abstract NBTType serialize();
	
}