package lu.kremi151.minamod.util.nbtmath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.nbt.NBTTagString;

public class SerializableNashornFunction extends SerializableFunction<NBTTagString>{
	
	private final String scriptName;
	private final Invocable invocable;
	
	public SerializableNashornFunction(String scriptName) throws FileNotFoundException, ScriptException {
		this.scriptName = scriptName;
		
		ScriptEngine engine = new ScriptEngineManager(ClassLoader.getSystemClassLoader()).getEngineByName("JavaScript");
		engine.eval(new FileReader(new File(MinaMod.scriptsPath.get(), scriptName + ".js")));
		invocable = (Invocable) engine;
	}

	@Override
	public Number apply(Number arg0) {
		try {
			return ((Number)invocable.invokeFunction("calculate", new Object())).doubleValue();//TODO: Pass adequate objects
		} catch (NoSuchMethodException | ScriptException e) {
			System.err.println("Script \"" + scriptName + "\" has no method called \"calculate\"");
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
