package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.nbtmath.util.ISerializable;
import net.minecraft.nbt.NBTBase;

public abstract class SerializableFunctionBase<NBTType extends NBTBase> implements UnaryOperator<Number>, ISerializable<NBTType>{
	
	private final Context context;
	
	SerializableFunctionBase(Context context){
		if(context == null) {
			throw new NullPointerException();
		}
		this.context = context;
	}
	
	@Override
	@Deprecated
	/**
	 * This method exists to guarantee a compatibility with the {@code UnaryOperator} interface. This method may be removed in future revisions.
	 */
	public final Number apply(Number t) {
		return apply(t, context);
	}
	
	public abstract Number apply(Number t, Context c);
	
}