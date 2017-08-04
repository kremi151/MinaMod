package lu.kremi151.minamod.util.nbtmath.serialization;

import lu.kremi151.minamod.util.nbtmath.MathFunctionException;
import lu.kremi151.minamod.util.nbtmath.SerializableFunctionBase;
import lu.kremi151.minamod.util.nbtmath.SerializableFunction;
import net.minecraft.nbt.NBTBase;

@FunctionalInterface
public interface INBTFunctionDeserializer<FunctionName extends SerializableFunctionBase> {

	FunctionName deserialize(SerializableFunctionBase<? extends NBTBase> args[]) throws MathFunctionException;
	
}
