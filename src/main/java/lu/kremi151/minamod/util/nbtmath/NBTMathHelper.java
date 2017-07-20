package lu.kremi151.minamod.util.nbtmath;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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
	 * @param constGetter A mapper function which allows to map literals to constant numbers
	 * @param varGetter A mapper function which allows to map literals to variable numbers (acting like functions)
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableOperation parseOperation(NBTTagCompound nbt, Function<String, Number> constGetter, Function<String, UnaryOperator<Number>> varGetter) throws MathParseException{
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
		parsedA = parseFunction(a, constGetter, varGetter);
		parsedB = parseFunction(b, constGetter, varGetter);
		
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
	 * Parses a pure mathematical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param constGetter A mapper function which allows to map literals to constant numbers
	 * @param varGetter A mapper function which allows to map literals to variable numbers (acting like functions)
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 * @throws MathFunctionException Thrown if the function could not be created
	 */
	private static SerializableNamedFunction parseMathFunction(NBTTagCompound nbt, Function<String, Number> constGetter, Function<String, UnaryOperator<Number>> varGetter) throws MathParseException, MathFunctionException{
		NBTTagList args = nbt.getTagList("Arguments", 10);
		String functionName = nbt.getString("Function");
		if(args.tagCount() == 0) {
			throw new MathParseException("No arguments parsed for function " + functionName);
		}
		
		ArrayList<SerializableFunction<? extends NBTBase>> parsedArgs = new ArrayList<>();
		for(int i = 0 ; i < args.tagCount() ; i++) {
			NBTTagCompound anbt = args.getCompoundTagAt(i);
			if(anbt.hasKey("Arg", 10)) {
				parsedArgs.add(parseFunction(anbt.getTag("Arg"), constGetter, varGetter));
			}
		}
		SerializableFunction<? extends NBTBase> parsedArgsArray[] = parsedArgs.toArray(new SerializableFunction[parsedArgs.size()]);
		
		//TODO: Make function determination dynamic
		if(functionName.equals("abs")) {
			return new SerializableNamedFunction.Absolute(parsedArgsArray[0]);
		}else if(functionName.equals("neg")) {
			return new SerializableNamedFunction.Negate(parsedArgsArray[0]);
		}else {
			throw new MathParseException("Unknown math function: " + functionName);
		}
	}
	
	/**
	 * Parses a mathematical functional structure from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableFunction parseFunction(NBTBase nbt) throws MathParseException{
		return parseFunction(nbt, var -> null, var -> null);
	}
	
	/**
	 * Parses a mathematical functional structure from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param constGetter A mapper function which allows to map literals to constant numbers
	 * @param varGetter A mapper function which allows to map literals to variable numbers (acting like functions)
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public static SerializableFunction parseFunction(NBTBase nbt, Function<String, Number> constGetter, Function<String, UnaryOperator<Number>> varGetter) throws MathParseException{
		if(nbt instanceof NBTTagCompound) {
			NBTTagCompound raw_nbt = (NBTTagCompound)nbt;
			if(raw_nbt.hasKey("Operation", 8)) {
				return parseOperation((NBTTagCompound)nbt, constGetter, varGetter);
			}else if(raw_nbt.hasKey("Function", 8)) {
				try {
					return parseMathFunction((NBTTagCompound)nbt, constGetter, varGetter);
				} catch (MathFunctionException e) {
					throw new MathParseException("Could not parse math function named " + raw_nbt.getString("Function"), e);
				}
			}else {
				throw new MathParseException("Unknown data structure: " + raw_nbt);
			}
		}else if(nbt instanceof NBTPrimitive) {
			return new SerializableConstant(((NBTPrimitive)nbt).getDouble());
		}else if(nbt instanceof NBTTagString) {
			String varName = ((NBTTagString)nbt).getString();
			UnaryOperator<Number> var;
			if(varName.equals("x")) {
				return new SerializableVariable();
			}else {
				Number constant = constGetter.apply(varName);
				if(constant != null) {
					return new SerializableNamedConstant(constant, varName);
				}else {
					var = varGetter.apply(varName);
				}
			}
			if(var != null) {
				return new SerializableNamedMapper(var, varName);
			}else {
				throw new MathParseException("Unknown variable: " + varName);
			}
		}else {
			throw new MathParseException("Cannot parse: " + nbt);
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
}
