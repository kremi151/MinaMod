package lu.kremi151.minamod.util.nbtmath;

import net.minecraft.nbt.NBTTagString;

public class SerializableVariable extends SerializableFunction<NBTTagString>{

	@Override
	public Number apply(Number t) {
		return t;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString("x");
	}
	
}