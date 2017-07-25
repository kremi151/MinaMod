package lu.kremi151.minamod.util.nbtmath.serialization;

import lu.kremi151.minamod.util.nbtmath.MathFunctionException;
import lu.kremi151.minamod.util.nbtmath.SerializableFunctionBase;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import net.minecraft.nbt.NBTBase;

public interface INBTFunctionDeserializer<FunctionName extends SerializableFunction> {

	FunctionName deserialize(SerializableFunctionBase<? extends NBTBase> args[]) throws MathFunctionException;
	
}
