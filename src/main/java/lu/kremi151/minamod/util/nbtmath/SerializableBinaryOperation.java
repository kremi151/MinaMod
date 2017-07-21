package lu.kremi151.minamod.util.nbtmath;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SerializableBinaryOperation extends SerializableFunction<NBTTagCompound>{
	
	private final SerializableFunction<? extends NBTBase> a, b;
	private final SerializableOperator operation;
	
	public SerializableBinaryOperation(SerializableFunction<? extends NBTBase> a, SerializableFunction<? extends NBTBase> b, SerializableOperator operation) {
		this.a = a;
		this.b = b;
		this.operation = operation;
	}

	@Override
	public Number apply(Number t) {
		return operation.apply(a.apply(t), b.apply(t));
	}

	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("A", a.serialize());
		nbt.setTag("B", b.serialize());
		nbt.setString("Operation", ""+operation.getOperation());
		return nbt;
	}
	
}