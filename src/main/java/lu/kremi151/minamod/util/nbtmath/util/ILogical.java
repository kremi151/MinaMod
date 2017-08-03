package lu.kremi151.minamod.util.nbtmath.util;

import net.minecraft.nbt.NBTBase;

public interface ILogical<NBTType extends NBTBase> extends ISerializable<NBTType>{

	boolean evaluate(Number t, Context c);
	
}
