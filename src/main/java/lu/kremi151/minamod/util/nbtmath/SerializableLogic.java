package lu.kremi151.minamod.util.nbtmath;

import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.nbtmath.util.ILogical;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SerializableLogic extends SerializableFunctionBase<NBTTagCompound> implements ILogical<NBTTagCompound>{
	
	protected final String name;

	SerializableLogic(String name) {
		super(Context.DEFAULT);
		this.name = name;
	}

	@Override
	public final Number apply(Number t, Context c) {
		return evaluate(t, c) ? 1 : 0;
	}
	
	public abstract static class Comparison extends SerializableLogic{
		
		protected final SerializableFunctionBase<? extends NBTBase> a, b;

		Comparison(String name, SerializableFunctionBase<? extends NBTBase> a, SerializableFunctionBase<? extends NBTBase> b) {
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
	
	public static class Equals extends Comparison{

		public Equals(SerializableFunctionBase<? extends NBTBase> a, SerializableFunctionBase<? extends NBTBase> b) {
			super("equals", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() == b.apply(t, c).doubleValue();
		}
		
	}
	
	public static class BiggerThan extends Comparison{

		public BiggerThan(SerializableFunctionBase a, SerializableFunctionBase b) {
			super("bigger", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() > b.apply(t, c).doubleValue();
		}
		
	}
	
	public static class BiggerOrEqualThan extends Comparison{

		public BiggerOrEqualThan(SerializableFunctionBase a, SerializableFunctionBase b) {
			super("biggereq", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() >= b.apply(t, c).doubleValue();
		}
		
	}
	
	public static class LowerThan extends Comparison{

		public LowerThan(SerializableFunctionBase a, SerializableFunctionBase b) {
			super("lower", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() < b.apply(t, c).doubleValue();
		}
		
	}
	
	public static class LowerOrEqualThan extends Comparison{

		public LowerOrEqualThan(SerializableFunctionBase a, SerializableFunctionBase b) {
			super("lowereq", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.apply(t, c).doubleValue() <= b.apply(t, c).doubleValue();
		}
		
	}
	
	public abstract static class Binary extends SerializableLogic{
		
		protected final ILogical a, b;

		Binary(String name, ILogical a, ILogical b) {
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
		
		protected final ILogical a;

		Unary(String name, ILogical a) {
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

		public And(ILogical a, ILogical b) {
			super("and", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) && b.evaluate(t, c);
		}
		
	}
	
	public static class NAnd extends Binary{

		public NAnd(ILogical a, ILogical b) {
			super("nand", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) && b.evaluate(t, c));
		}
		
	}
	
	public static class Or extends Binary{

		public Or(ILogical a, ILogical b) {
			super("or", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) || b.evaluate(t, c);
		}
		
	}
	
	public static class NOr extends Binary{

		public NOr(ILogical a, ILogical b) {
			super("nor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) || b.evaluate(t, c));
		}
		
	}
	
	public static class XOr extends Binary{

		public XOr(ILogical a, ILogical b) {
			super("xor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return a.evaluate(t, c) ^ b.evaluate(t, c);
		}
		
	}
	
	public static class XNOr extends Binary{

		public XNOr(ILogical a, ILogical b) {
			super("xnor", a, b);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !(a.evaluate(t, c) ^ b.evaluate(t, c));
		}
		
	}
	
	public static class Not extends Unary{

		public Not(ILogical a) {
			super("not", a);
		}

		@Override
		public boolean evaluate(Number t, Context c) {
			return !a.evaluate(t, c);
		}
		
	}

}
