package lu.kremi151.minamod.util.nbtmath.util;

import lu.kremi151.minamod.util.nbtmath.MathParseException;
import lu.kremi151.minamod.util.nbtmath.SerializableConditional;
import lu.kremi151.minamod.util.nbtmath.SerializableFunctionBase;
import net.minecraft.nbt.NBTBase;

public interface ParserContext {

	ILogical parseLogic(NBTBase nbt) throws MathParseException;
	SerializableConditional parseConditional(NBTBase nbt) throws MathParseException;
	SerializableFunctionBase<? extends NBTBase> parse(NBTBase nbt) throws MathParseException;
	
}
