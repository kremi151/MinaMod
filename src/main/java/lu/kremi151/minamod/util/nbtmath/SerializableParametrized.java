package lu.kremi151.minamod.util.nbtmath;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SerializableParametrized extends SerializableFunctionBase<NBTTagCompound>{
	
	private final SerializableFunctionBase<? extends NBTBase> function, param;

	public SerializableParametrized(@Nonnull SerializableFunctionBase<? extends NBTBase> function, @Nonnull SerializableFunctionBase<? extends NBTBase> param) {
		super(Context.DEFAULT);
		if(function == null) {
			throw new NullPointerException();
		}
		if(param == null) {
			throw new NullPointerException();
		}
		this.function = function;
		this.param = param;
	}

	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Function", function.serialize());
		nbt.setTag("Parameter", param.serialize());
		return nbt;
	}

	@Override
	public Number apply(Number t, Context c) {
		return function.apply(param.apply(t, c), c);
	}

}
