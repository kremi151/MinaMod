package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SerializableConditional extends SerializableFunction<NBTTagCompound>{
	
	private final SerializableLogic cond;
	private final SerializableFunction<? extends NBTBase> _then, _else;

	public SerializableConditional(SerializableLogic condition, SerializableFunction _then, SerializableFunction _else) {
		super(Context.DEFAULT);
		this.cond = condition;
		this._then = _then;
		this._else = _else;
	}

	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("If", cond.serialize());
		nbt.setTag("Then", _then.serialize());
		nbt.setTag("Else", _else.serialize());
		return nbt;
	}

	@Override
	public Number apply(Number t) {
		if(cond.evaluate(t)) {
			return _then.apply(t);
		}else {
			return _else.apply(t);
		}
	}

}
