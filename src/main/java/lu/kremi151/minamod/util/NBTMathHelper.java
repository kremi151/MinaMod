package lu.kremi151.minamod.util;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A helper class to parse mathematical functions from NBT data
 * @author michm
 *
 */
public class NBTMathHelper {

	/**
	 * Parses a mathematical operation from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableOperation parseOperation(NBTTagCompound nbt) throws MathParseException{
		return parseOperation(nbt, var -> null, var -> null);
	}

	/**
	 * Parses a mathematical operation from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param varGetter A mapper function which allows to map literals to numbers
	 * @param functionGetter A mapper function which allows to map literals to functions
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableOperation parseOperation(NBTTagCompound nbt, Function<String, Number> varGetter, Function<String, UnaryOperator<Number>> functionGetter) throws MathParseException{
		NBTBase a = nbt.getTag("A");
		NBTBase b = nbt.getTag("B");
		String operation = nbt.getString("Operation");
		
		if(a == null || b == null) {
			throw new MathParseException("Invalid structure");
		}
		if(operation.length() != 1) {
			throw new MathParseException("Invalid operation format");
		}
		
		final SerializableFunction parsedA, parsedB;
		parsedA = parseFunction(a, varGetter, functionGetter);
		parsedB = parseFunction(b, varGetter, functionGetter);
		
		switch(operation.charAt(0)) {
		case '+':
			return new SerializableOperation(parsedA, parsedB, ADDITION);
		case '-':
			return new SerializableOperation(parsedA, parsedB, SUBSTRACTION);
		case '*':
			return new SerializableOperation(parsedA, parsedB, MULTIPLICATION);
		case '/':
			return new SerializableOperation(parsedA, parsedB, DIVISION);
		case '^':
			return new SerializableOperation(parsedA, parsedB, POWER);
		case 'M':
			return new SerializableOperation(parsedA, parsedB, MAX);
		case 'm':
			return new SerializableOperation(parsedA, parsedB, MIN);
		default:
			throw new MathParseException("Unknown operation: " + operation);
		}
	}
	
	/**
	 * Parses a mathematical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableFunction parseFunction(NBTBase nbt) throws MathParseException{
		return parseFunction(nbt, var -> null, var -> null);
	}
	
	/**
	 * Parses a mathematical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param varGetter A mapper function which allows to map literals to numbers
	 * @param functionGetter A mapper function which allows to map literals to functions
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableFunction parseFunction(NBTBase nbt, Function<String, Number> varGetter, Function<String, UnaryOperator<Number>> functionGetter) throws MathParseException{
		if(nbt instanceof NBTTagCompound) {
			return parseOperation((NBTTagCompound)nbt, varGetter, functionGetter);
		}else if(nbt instanceof NBTPrimitive) {
			return new SerializableConstant(((NBTPrimitive)nbt).getDouble());
		}else if(nbt instanceof NBTTagString) {
			String varName = ((NBTTagString)nbt).getString();
			UnaryOperator<Number> var;
			if(varName.equals("x")) {
				return new SerializableVariable();
			}else {
				Number constant = varGetter.apply(varName);
				if(constant != null) {
					return new SerializableNamedConstant(constant, varName);
				}else {
					var = functionGetter.apply(varName);
				}
			}
			if(var != null) {
				return new SerializableNamedFunction(var, varName);
			}else {
				throw new MathParseException("Unknown variable: " + varName);
			}
		}else {
			throw new MathParseException("Cannot parse: " + nbt);
		}
	}
	
	public static class MathParseException extends Exception{
		
		private MathParseException(String msg) {
			super(msg);
		}
	}

	public static final SerializableOperator ADDITION = new SerializableOperator('+') {
		@Override
		public Number apply(Number a, Number b) {
			return a.doubleValue() + b.doubleValue();
		}
	};

	public static final SerializableOperator SUBSTRACTION = new SerializableOperator('-') {
		@Override
		public Number apply(Number a, Number b) {
			return a.doubleValue() - b.doubleValue();
		}
	};

	public static final SerializableOperator MULTIPLICATION = new SerializableOperator('*') {
		@Override
		public Number apply(Number a, Number b) {
			return a.doubleValue() * b.doubleValue();
		}
	};

	public static final SerializableOperator DIVISION = new SerializableOperator('/') {
		@Override
		public Number apply(Number a, Number b) {
			return a.doubleValue() / b.doubleValue();
		}
	};

	public static final SerializableOperator POWER = new SerializableOperator('^') {
		@Override
		public Number apply(Number a, Number b) {
			return Math.pow(a.doubleValue(), b.doubleValue());
		}
	};

	public static final SerializableOperator MAX = new SerializableOperator('M') {
		@Override
		public Number apply(Number a, Number b) {
			return Math.max(a.doubleValue(), b.doubleValue());
		}
	};

	public static final SerializableOperator MIN = new SerializableOperator('m') {
		@Override
		public Number apply(Number a, Number b) {
			return Math.min(a.doubleValue(), b.doubleValue());
		}
	};
	
	public abstract static class SerializableOperator implements BinaryOperator<Number>{
		
		private final char operation;
		
		private SerializableOperator(char operation) {
			this.operation = operation;
		}
		
	}
	
	public static abstract class SerializableFunction<NBTType extends NBTBase> implements UnaryOperator<Number>{

		public abstract NBTType serialize();
		
	}
	
	public static class SerializableOperation extends SerializableFunction<NBTTagCompound>{
		
		private final SerializableFunction<? extends NBTBase> a, b;
		private final SerializableOperator operation;
		
		public SerializableOperation(SerializableFunction<? extends NBTBase> a, SerializableFunction<? extends NBTBase> b, SerializableOperator operation) {
			this.a = a;
			this.b = b;
			this.operation = operation;
		}

		@Override
		public Number apply(Number t) {
			return operation.apply(a.apply(t), b.apply(t));
		}

		@Override
		public NBTTagCompound serialize() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("A", a.serialize());
			nbt.setTag("B", b.serialize());
			nbt.setString("Operation", ""+operation.operation);
			return nbt;
		}
		
	}
	
	public static class SerializableConstant extends SerializableFunction<NBTPrimitive>{
		
		private final Number primitive;
		
		public SerializableConstant(Number primitive) {
			this.primitive = primitive;
		}

		@Override
		public Number apply(Number t) {
			return primitive;
		}

		@Override
		public NBTPrimitive serialize() {
			return new NBTTagDouble(primitive.doubleValue());
		}
		
	}
	
	public static class SerializableNamedConstant extends SerializableFunction<NBTTagString>{

		private final Number primitive;
		private final String varName;

		public SerializableNamedConstant(Number primitive, String varName) {
			this.primitive = primitive;
			this.varName = varName;
		}

		@Override
		public Number apply(Number t) {
			return primitive;
		}

		@Override
		public NBTTagString serialize() {
			return new NBTTagString(varName);
		}
		
	}
	
	public static class SerializableNamedFunction extends SerializableFunction<NBTTagString>{
		
		private final String functionName;
		private final UnaryOperator<Number> function;
		
		public SerializableNamedFunction(UnaryOperator<Number> function, String functionName) {
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

	}
	
	public static class SerializableVariable extends SerializableFunction<NBTTagString>{

		@Override
		public Number apply(Number t) {
			return t;
		}

		@Override
		public NBTTagString serialize() {
			return new NBTTagString("x");
		}
		
	}
}
