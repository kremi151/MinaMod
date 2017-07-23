package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagDouble;

public class SerializableConstant extends SerializableFunction<NBTPrimitive>{
	
	private final Number primitive;
	
	public SerializableConstant(Number primitive) {
		super(Context.DEFAULT);
		this.primitive = primitive;
	}

	@Override
	public Number apply(Number t) {
		return primitive;
	}

	@Override
	public NBTPrimitive serialize() {
		return new NBTTagDouble(primitive.doubleValue());
	}
	
}