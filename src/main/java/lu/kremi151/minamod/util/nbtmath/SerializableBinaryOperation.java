package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SerializableBinaryOperation extends SerializableFunctionBase<NBTTagCompound>{
	
	private final SerializableFunctionBase<? extends NBTBase> a, b;
	private final SerializableOperator operation;
	
	public SerializableBinaryOperation(SerializableFunctionBase<? extends NBTBase> a, SerializableFunctionBase<? extends NBTBase> b, SerializableOperator operation) {
		super(Context.DEFAULT);
		this.a = a;
		this.b = b;
		this.operation = operation;
	}

	@Override
	public Number apply(Number t, Context c) {
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