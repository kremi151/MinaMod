package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SerializableLogic extends SerializableFunctionBase<NBTTagCompound>{
	
	protected final String name;

	SerializableLogic(String name) {
		super(Context.DEFAULT);
		this.name = name;
	}

	@Override
	public final Number apply(Number t, Context c) {
		return evaluate(t, c) ? 1 : 0;
	}
	
	public abstract boolean evaluate(Number t, Context c);
	
	public static class Equals extends SerializableLogic{
		
		protected final SerializableFunctionBase<? extends NBTBase> a, b;

		public Equals(SerializableFunctionBase a, SerializableFunctionBase b) {
			super("equals");
			this.a = a;
			this.b = b;
		}

		@Override
		public final NBTTagCompound serialize() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Logic", name);
			nbt.setTag("A", a.serialize());
			nbt.setTag("B", b.serialize());
			return nbt;
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() == b.apply(t, c).doubleValue();
		}
		
	}
	
	public abstract static class Binary extends SerializableLogic{
		
		protected final SerializableLogic a, b;

		Binary(String name, SerializableLogic a, SerializableLogic b) {
			super(name);
			this.a = a;
			this.b = b;
		}

		@Override
		public final NBTTagCompound serialize() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Logic", name);
			nbt.setTag("A", a.serialize());
			nbt.setTag("B", b.serialize());
			return nbt;
		}
		
	}
	
	public abstract static class Unary extends SerializableLogic{
		
		protected final SerializableLogic a;

		Unary(String name, SerializableLogic a) {
			super(name);
			this.a = a;
		}

		@Override
		public final NBTTagCompound serialize() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Logic", name);
			nbt.setTag("A", a.serialize());
			return nbt;
		}
		
	}
	
	public static class And extends Binary{

		public And(SerializableLogic a, SerializableLogic b) {
			super("and", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) && b.evaluate(t, c);
		}
		
	}
	
	public static class NAnd extends Binary{

		public NAnd(SerializableLogic a, SerializableLogic b) {
			super("nand", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) && b.evaluate(t, c));
		}
		
	}
	
	public static class Or extends Binary{

		public Or(SerializableLogic a, SerializableLogic b) {
			super("or", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) || b.evaluate(t, c);
		}
		
	}
	
	public static class NOr extends Binary{

		public NOr(SerializableLogic a, SerializableLogic b) {
			super("nor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) || b.evaluate(t, c));
		}
		
	}
	
	public static class XOr extends Binary{

		public XOr(SerializableLogic a, SerializableLogic b) {
			super("xor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) ^ b.evaluate(t, c);
		}
		
	}
	
	public static class XNOr extends Binary{

		public XNOr(SerializableLogic a, SerializableLogic b) {
			super("xnor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) ^ b.evaluate(t, c));
		}
		
	}
	
	public static class Not extends Unary{

		public Not(SerializableLogic a) {
			super("not", a);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !a.evaluate(t, c);
		}
		
	}

}
