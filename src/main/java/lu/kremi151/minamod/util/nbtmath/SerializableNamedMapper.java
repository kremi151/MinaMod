package lu.kremi151.minamod.util.nbtmath;

import java.util.function.UnaryOperator;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public class SerializableNamedMapper extends SerializableFunction<NBTTagString>{
	
	private final String functionName;
	private final UnaryOperator<Number> function;
	
	public SerializableNamedMapper(UnaryOperator<Number> function, String functionName) {
		super(Context.DEFAULT);
		this.functionName = functionName;
		this.function = function;
	}
	
	@Override
	public Number apply(Number t) {
		return function.apply(t);
	}
	@Override
	public NBTTagString serialize() {
		return new NBTTagString(functionName);
	}
	
	public static SerializableNamedMapper createPlaceholder(String name) {
		return new SerializableNamedMapper(x -> {
			throw new UnsupportedOperationException("A placeholder cannot be evaluated");
		}, name);
	}

}