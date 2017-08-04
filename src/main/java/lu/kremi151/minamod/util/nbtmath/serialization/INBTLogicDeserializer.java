package lu.kremi151.minamod.util.nbtmath.serialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lu.kremi151.minamod.util.nbtmath.MathFunctionException;
import lu.kremi151.minamod.util.nbtmath.MathParseException;
import lu.kremi151.minamod.util.nbtmath.SerializableLogic;
import lu.kremi151.minamod.util.nbtmath.util.ParserContext;
import net.minecraft.nbt.NBTBase;

@FunctionalInterface
public interface INBTLogicDeserializer<LogicName extends SerializableLogic> {

	LogicName deserialize(@Nonnull NBTBase a, @Nullable NBTBase b, ParserContext context) throws MathFunctionException, MathParseException;
	
}
