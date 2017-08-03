package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public class SerializableParameter extends SerializableFunctionBase<NBTTagString>{

	SerializableParameter() {
		super(Context.DEFAULT);
	}

	@Override
	public Number apply(Number t, Context c) {
		return t;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString("x");
	}
	
}