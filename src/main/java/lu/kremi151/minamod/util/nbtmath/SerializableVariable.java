package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public class SerializableVariable extends SerializableFunction<NBTTagString>{

	SerializableVariable() {
		super(Context.DEFAULT);
	}

	@Override
	public Number apply(Number t) {
		return t;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString("x");
	}
	
}