package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public class SerializableNamedConstant extends SerializableFunction<NBTTagString>{

	private final Number primitive;
	private final String varName;

	public SerializableNamedConstant(Number primitive, String varName) {
		super(Context.DEFAULT);
		this.primitive = primitive;
		this.varName = varName;
	}

	@Override
	public Number apply(Number t) {
		return primitive;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString(varName);
	}
	
}