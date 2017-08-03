package lu.kremi151.minamod.util.nbtmath.util;

import net.minecraft.nbt.NBTBase;

public interface ISerializable<NBTType extends NBTBase> {

	NBTType serialize();
	
}
