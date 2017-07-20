package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import net.minecraft.nbt.NBTBase;

public abstract class SerializableFunction<NBTType extends NBTBase> implements UnaryOperator<Number>{

	public abstract NBTType serialize();
	
}