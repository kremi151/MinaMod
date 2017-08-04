package lu.kremi151.minamod.util.nbtmath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import javax.script.ScriptException;

import lu.kremi151.minamod.util.nbtmath.serialization.INBTFunctionDeserializer;
import lu.kremi151.minamod.util.nbtmath.serialization.INBTLogicDeserializer;
import lu.kremi151.minamod.util.nbtmath.util.Context;
import lu.kremi151.minamod.util.nbtmath.util.ILogical;
import lu.kremi151.minamod.util.nbtmath.util.ParserContext;
import lu.kremi151.minamod.util.nbtmath.util.ToBooleanFunction;
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
	
	private static NBTMathHelper defaultInstance = null;

	private final Map<String, INBTFunctionDeserializer<? extends SerializableFunction>> mathFunctionDeserializers;
	private final Map<String, INBTLogicDeserializer<? extends SerializableLogic>> mathLogicDeserializers;
	private final Map<Character, SerializableOperator> operatorDeserializers;
	
	private NBTMathHelper(Map<String, INBTFunctionDeserializer<? extends SerializableFunction>> mathFunctionDeserializers,
			Map<String, INBTLogicDeserializer<? extends SerializableLogic>> mathLogicDeserializers,
			Map<Character, SerializableOperator> operatorDeserializers) {
		this.mathFunctionDeserializers = Collections.unmodifiableMap(mathFunctionDeserializers);
		this.mathLogicDeserializers = Collections.unmodifiableMap(mathLogicDeserializers);
		this.operatorDeserializers = Collections.unmodifiableMap(operatorDeserializers);
	}

	public NBTMathHelper() {//TODO
		HashMap<String, INBTFunctionDeserializer<? extends SerializableFunction>> mathFunctionDeserializers = new HashMap<>();
		registerDefaultMathFunctions(mathFunctionDeserializers);

		HashMap<String, INBTLogicDeserializer<? extends SerializableLogic>> mathLogicDeserializers = new HashMap<>();
		registerDefaultMathLogics(mathLogicDeserializers);

		HashMap<Character, SerializableOperator> operatorDeserializers = new HashMap<>();
		registerDefaultOperators(operatorDeserializers);
		
		this.mathFunctionDeserializers = Collections.unmodifiableMap(mathFunctionDeserializers);
		this.mathLogicDeserializers = Collections.unmodifiableMap(mathLogicDeserializers);
		this.operatorDeserializers = Collections.unmodifiableMap(operatorDeserializers);
	}
	
	private static void registerDefaultMathFunctions(Map<String, INBTFunctionDeserializer<? extends SerializableFunction>> map) {
		map.put("abs", args -> new SerializableFunction.Absolute(args[0]));
		map.put("neg", args -> new SerializableFunction.Negate(args[0]));
		map.put("max", args -> new SerializableFunction.Max(args));
		map.put("min", args -> new SerializableFunction.Min(args));
		map.put("sum", args -> new SerializableFunction.Sum(args));
		map.put("product", args -> new SerializableFunction.Product(args));
	}
	
	private static void registerDefaultMathLogics(Map<String, INBTLogicDeserializer<? extends SerializableLogic>> map) {
		map.put("and", (a, b, c) -> new SerializableLogic.And(c.parseLogic(a), c.parseLogic(b)));
		map.put("nand", (a, b, c) -> new SerializableLogic.NAnd(c.parseLogic(a), c.parseLogic(b)));
		map.put("or", (a, b, c) -> new SerializableLogic.Or(c.parseLogic(a), c.parseLogic(b)));
		map.put("nor", (a, b, c) -> new SerializableLogic.NOr(c.parseLogic(a), c.parseLogic(b)));
		map.put("xor", (a, b, c) -> new SerializableLogic.XOr(c.parseLogic(a), c.parseLogic(b)));
		map.put("xnor", (a, b, c) -> new SerializableLogic.XNOr(c.parseLogic(a), c.parseLogic(b)));
		map.put("not", (a, b, c) -> new SerializableLogic.Not(c.parseLogic(a)));
		map.put("equals", (a, b, c) -> new SerializableLogic.Equals(c.parse(a), c.parse(b)));
		map.put("bigger", (a, b, c) -> new SerializableLogic.BiggerThan(c.parse(a), c.parse(b)));
		map.put("biggereq", (a, b, c) -> new SerializableLogic.BiggerOrEqualThan(c.parse(a), c.parse(b)));
		map.put("lower", (a, b, c) -> new SerializableLogic.LowerThan(c.parse(a), c.parse(b)));
		map.put("lowereq", (a, b, c) -> new SerializableLogic.LowerOrEqualThan(c.parse(a), c.parse(b)));
	}
	
	private static void registerDefaultOperators(Map<Character, SerializableOperator> map) {
		map.put('+', ADDITION);
		map.put('-', DIFFERENCE);
		map.put('*', MULTIPLICATION);
		map.put('/', DIVISION);
		map.put('^', POWER);
	}
	
	/**
	 * Parses a mathematical operation from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public SerializableBinaryOperation parseOperation(NBTTagCompound nbt) throws MathParseException{
		return parseOperation(nbt, Context.DEFAULT);
	}

	/**
	 * Parses a mathematical operation from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public SerializableBinaryOperation parseOperation(NBTTagCompound nbt, Context context) throws MathParseException{
		NBTBase a = nbt.getTag("A");
		NBTBase b = nbt.getTag("B");
		String operation = nbt.getString("Operation");
		
		if(a == null || b == null) {
			throw new MathParseException("Invalid structure");
		}
		if(operation.length() != 1) {
			throw new MathParseException("Invalid operation format");
		}
		
		SerializableOperator operator = operatorDeserializers.get(operation.charAt(0));
		if(operator != null) {
			final SerializableFunctionBase parsedA, parsedB;
			parsedA = parseFunction(a, context);
			parsedB = parseFunction(b, context);
			return new SerializableBinaryOperation(parsedA, parsedB, operator);
		}else {
			throw new MathParseException("Unknown operation: " + operation);
		}
	}
	
	/**
	 * Parses a pure mathematical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 * @throws MathFunctionException Thrown if the function could not be created
	 */
	private SerializableFunction parseMathFunction(NBTTagCompound nbt, Context context) throws MathParseException, MathFunctionException{
		NBTTagList args = nbt.getTagList("Arguments", 10);
		String functionName = nbt.getString("Function");
		if(args.tagCount() == 0) {
			throw new MathParseException("No arguments parsed for function " + functionName);
		}
		
		ArrayList<SerializableFunctionBase<? extends NBTBase>> parsedArgs = new ArrayList<>();
		for(int i = 0 ; i < args.tagCount() ; i++) {
			NBTTagCompound anbt = args.getCompoundTagAt(i);
			if(anbt.hasKey("Arg")) {
				parsedArgs.add(parseFunction(anbt.getTag("Arg"), context));
			}
		}
		SerializableFunctionBase<? extends NBTBase> parsedArgsArray[] = parsedArgs.toArray(new SerializableFunctionBase[parsedArgs.size()]);
		
		INBTFunctionDeserializer<? extends SerializableFunction> deserializer = mathFunctionDeserializers.get(functionName);
		if(deserializer != null) {
			return deserializer.deserialize(parsedArgsArray);
		}else {
			throw new MathParseException("Unknown math function: " + functionName);
		}
	}
	
	/**
	 * Parses a logical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	private ILogical parseLogic(NBTBase nbt, Context context) throws MathParseException{
		if(nbt instanceof NBTTagCompound) {
			return parseLogic((NBTTagCompound) nbt, context);
		}else if(nbt instanceof NBTTagString) {
			String name = ((NBTTagString)nbt).getString();
			ToBooleanFunction<Number> logic = context.resolveLogic(name);
			if(name != null) {
				return SerializableNamedLogical.createAndProvide(name, logic);
			}else {
				return SerializableNamedLogical.createByName(name);
			}
		}else {
			throw new MathParseException("Unknown logical data structure: " + nbt);
		}
	}
	
	/**
	 * Parses a logical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 * @throws MathFunctionException Thrown if the function could not be created
	 */
	private SerializableLogic parseLogic(NBTTagCompound nbt) throws MathParseException, MathFunctionException{
		return parseLogic(nbt, Context.DEFAULT);
	}
	
	/**
	 * Parses a logical function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	private SerializableLogic parseLogic(NBTTagCompound nbt, Context context) throws MathParseException{
		String logic = nbt.getString("Logic");
		if(nbt.hasKey("A")) {
			NBTBase _a = nbt.getTag("A");
			NBTBase _b = nbt.getTag("B");
			INBTLogicDeserializer<? extends SerializableLogic> deserializer = mathLogicDeserializers.get(logic);
			if(deserializer != null) {
				return deserializer.deserialize(_a, _b, parserContext);
			}else {
				throw new MathParseException("Unknown logical function: " + logic);
			}
		}else {
			throw new MathParseException("The logical structure is missing an \"A\" parameter: " + nbt);
		}
	}
	
	/**
	 * Parses a conditional function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	private SerializableConditional parseConditional(NBTTagCompound nbt) throws MathParseException{
		return parseConditional(nbt, Context.DEFAULT);
	}
	
	/**
	 * Parses a conditional function from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	private SerializableConditional parseConditional(NBTTagCompound nbt, Context context) throws MathParseException{
		try {
			ILogical cond = parseLogic(nbt.getTag("If"), context);
			SerializableFunctionBase<? extends NBTBase> t, e;
			t = parseFunction(nbt.getTag("Then"), context);
			e = parseFunction(nbt.getTag("Else"), context);
			return new SerializableConditional(cond, t, e);
		}catch(Exception e) {
			throw new MathParseException(e);
		}
	}
	
	/**
	 * Parses a mathematical functional structure from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public SerializableFunctionBase parseFunction(NBTBase nbt) throws MathParseException{
		return parseFunction(nbt, Context.DEFAULT);
	}
	
	/**
	 * Parses a mathematical functional structure from NBT data
	 * @param nbt The NBT data structure holding the function
	 * @param context Context data
	 * @return Returns the parsed mathematical function if successful
	 * @throws MathParseException Thrown if the function could not be parsed
	 */
	public SerializableFunctionBase parseFunction(NBTBase nbt, Context context) throws MathParseException{
		if(nbt instanceof NBTTagCompound) {
			NBTTagCompound raw_nbt = (NBTTagCompound)nbt;
			if(raw_nbt.hasKey("Operation", 8)) {
				return parseOperation((NBTTagCompound)nbt, context);
			}else if(raw_nbt.hasKey("Function", 8)) {
				try {
					return parseMathFunction((NBTTagCompound)nbt, context);
				} catch (MathFunctionException e) {
					throw new MathParseException("Could not parse math function named " + raw_nbt.getString("Function"), e);
				}
			}else if(raw_nbt.hasKey("Logic", 8)) {
				try {
					return parseLogic((NBTTagCompound)nbt, context);
				} catch (MathFunctionException e) {
					throw new MathParseException("Could not parse logic named " + raw_nbt.getString("Logic"), e);
				}
			}else if(raw_nbt.hasKey("If")) {
				try {
					return parseConditional((NBTTagCompound)nbt, context);
				} catch (MathFunctionException e) {
					throw new MathParseException("Could not parse conditional: " + raw_nbt, e);
				}
			}else {
				throw new MathParseException("Unknown data structure: " + raw_nbt);
			}
		}else if(nbt instanceof NBTPrimitive) {
			return new SerializableConstant(((NBTPrimitive)nbt).getDouble());
		}else if(nbt instanceof NBTTagString) {
			String varName = ((NBTTagString)nbt).getString();
			if(varName.startsWith("js:")) {
				varName = varName.substring(3);
				try {
					return SerializableNashornFunction.getOrLoad(context, varName);
				} catch (IOException | ScriptException e) {
					throw new MathParseException(e);
				}
			}
			UnaryOperator<Number> var;
			if(varName.equals("x")) {
				return new SerializableParameter();
			}else {
				Number constant = context.resolveConstant(varName);
				if(constant != null) {
					return SerializableNamedConstant.createAndProvide(varName, constant);
				}else {
					var = context.resolveVariable(varName);
				}
			}
			if(var != null) {
				return SerializableNamedVariable.createAndProvide(varName, var);
			}else {
				throw new MathParseException("Unknown variable: " + varName);
			}
		}else {
			throw new MathParseException("Cannot parse: " + nbt);
		}
	}
	
	private ParserContext parserContext = new ParserContext() {

		@Override
		public ILogical parseLogic(NBTBase nbt) throws MathParseException {
			return NBTMathHelper.this.parseLogic(nbt, Context.DEFAULT);
		}

		@Override
		public SerializableConditional parseConditional(NBTBase nbt) throws MathParseException {
			if(nbt instanceof NBTTagCompound) {
				return NBTMathHelper.this.parseConditional((NBTTagCompound) nbt);
			}else {
				throw new MathParseException("The tag should be a tag compound");
			}
		}

		@Override
		public SerializableFunctionBase<? extends NBTBase> parse(NBTBase nbt) throws MathParseException {
			return NBTMathHelper.this.parseFunction(nbt);
		}
		
	};
	
	public static final SerializableOperator ADDITION = new SerializableOperator('+') {
		@Override
		public Number apply(Number a, Number b) {
			return a.doubleValue() + b.doubleValue();
		}
	};

	public static final SerializableOperator DIFFERENCE = new SerializableOperator('-') {
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
	
	public static NBTMathHelper getDefaultInstance() {
		if(defaultInstance == null) {
			defaultInstance = new NBTMathHelper();
		}
		return defaultInstance;
	}
	
	public static class Builder{
		private final Map<String, INBTFunctionDeserializer<? extends SerializableFunction>> mathFunctionDeserializers = new HashMap<>();
		private final Map<String, INBTLogicDeserializer<? extends SerializableLogic>> mathLogicDeserializers = new HashMap<>();
		private final Map<Character, SerializableOperator> operatorDeserializers = new HashMap<>();
		
		public Builder(boolean initDefaults) {
			if(initDefaults) {
				registerDefaultMathFunctions(mathFunctionDeserializers);
				registerDefaultMathLogics(mathLogicDeserializers);
				registerDefaultOperators(operatorDeserializers);
			}
		}
		
		public Builder clearFunctions() {
			mathFunctionDeserializers.clear();
			return this;
		}
		
		public Builder clearLogics() {
			mathLogicDeserializers.clear();
			return this;
		}
		
		public Builder clearOperators() {
			operatorDeserializers.clear();
			return this;
		}
		
		public Builder registerFunctionDeserializer(String name, INBTFunctionDeserializer<? extends SerializableFunction> deserializer) {
			if(!mathFunctionDeserializers.containsKey(name)) {
				mathFunctionDeserializers.put(name, deserializer);
			}else {
				throw new IllegalArgumentException("A deserializer for the function named " + name + " is already registered");
			}
			return this;
		}
		
		public Builder registerLogicDeserializer(String name, INBTLogicDeserializer<? extends SerializableLogic> deserializer) {
			if(!mathLogicDeserializers.containsKey(name)) {
				mathLogicDeserializers.put(name, deserializer);
			}else {
				throw new IllegalArgumentException("A deserializer for the logical function named " + name + " is already registered");
			}
			return this;
		}
		
		public Builder registerOperator(char id, SerializableOperator operator) {
			if(!operatorDeserializers.containsKey(id)) {
				operatorDeserializers.put(id, operator);
			}else {
				throw new IllegalArgumentException("An operator " + id + " is already registered");
			}
			return this;
		}
		
		public NBTMathHelper build() {
			return new NBTMathHelper(mathFunctionDeserializers, mathLogicDeserializers, operatorDeserializers);
		}
	}
}
