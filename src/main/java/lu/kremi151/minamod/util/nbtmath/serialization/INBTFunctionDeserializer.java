package lu.kremi151.minamod.util.nbtmath.serialization;

import lu.kremi151.minamod.util.nbtmath.MathFunctionException;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import lu.kremi151.minamod.util.nbtmath.SerializableNamedFunction;
import net.minecraft.nbt.NBTBase;

public interface INBTFunctionDeserializer<FunctionName extends SerializableNamedFunction> {

	FunctionName deserialize(SerializableFunction<? extends NBTBase> args[]) throws MathFunctionException;
	
}
