package lu.kremi151.minamod.util.nbtmath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.nbtmath.util.Context;
import net.minecraft.nbt.NBTTagString;

public class SerializableNashornFunction extends SerializableFunctionBase<NBTTagString>{

	private static final HashMap<String, ScriptEngine> ENGINE_CACHE = new HashMap<>();
	private static final HashMap<String, String> SCRIPT_CACHE = new HashMap<>();
	
	private final String scriptName, script;
	private final ScriptEngine engine;
	
	private SerializableNashornFunction(Context context, String scriptName) throws ScriptException, IOException {
		super(context);
		this.scriptName = scriptName;
		
		String script = SCRIPT_CACHE.get(scriptName);
		if(script == null) {
			try (BufferedReader br = new BufferedReader(new FileReader(new File(MinaMod.scriptsPath.get(), scriptName + ".js")))) {
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				script = sb.toString();
			}
		}
		this.script = script;
		
		ScriptEngine engine = ENGINE_CACHE.get(scriptName);
		if(engine == null) {
			engine = new ScriptEngineManager(ClassLoader.getSystemClassLoader()).getEngineByName("JavaScript");
			ENGINE_CACHE.put(scriptName, engine);
		}
		this.engine = engine;
	}
	
	public static SerializableNashornFunction getOrLoad(Context context, String scriptName) throws ScriptException, IOException {
		return new SerializableNashornFunction(context, scriptName);
	}

	@Override
	public Number apply(Number t, Context c) {
		Bindings newBindings = engine.createBindings();
		for(String var : c.variableNameSet()) {
			newBindings.put(var, (FunctionalInterface)c.resolveVariable(var));
		}
		for(String logical : c.logicNameSet()) {
			newBindings.put(logical, (FunctionalInterface)c.resolveLogic(logical));
		}
		for(String constant : c.constantNameSet()) {
			newBindings.put(constant, c.resolveConstant(constant).doubleValue());
		}
		engine.setBindings(newBindings, ScriptContext.ENGINE_SCOPE);
		try {
			engine.eval(script);
			return ((Number)((Invocable) engine).invokeFunction("calculate", t.doubleValue())).doubleValue();
		} catch (NoSuchMethodException e) {
			System.err.println("Script \"" + scriptName + "\" has no method called \"calculate\"");
			e.printStackTrace();
		} catch (ScriptException e) {
			System.err.println("Script \"" + scriptName + "\" produced an error");
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.err.println("Script \"" + scriptName + "\" does not return a value of type double");
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public NBTTagString serialize() {
		return new NBTTagString("js:" + scriptName);
	}

}
