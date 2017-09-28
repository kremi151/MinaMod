package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.annotations.OreInjector;
import lu.kremi151.minamod.interfaces.IOreInjector;
import lu.kremi151.minamod.worldgen.WorldGenerators;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class OreInjectorManager {

	private static final Map<String, IOreInjector> INJECTORS;
	private static Random rand = new Random(System.currentTimeMillis());
	
	static{
		Field fields[] = WorldGenerators.class.getDeclaredFields();
		HashMap<String, IOreInjector> injectors = new HashMap<>();
		for(Field f : fields){
			f.setAccessible(true);
			OreInjector oi = f.getAnnotation(OreInjector.class);
			if(IOreInjector.class.isAssignableFrom(f.getType()) && oi != null){
				try {
					if(!injectors.containsKey(oi.value())) {
						injectors.put(oi.value(), (IOreInjector)f.get(null));
					}else {
						throw new RuntimeException("Ore injector with id " + oi.value() + " already defined");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		INJECTORS = Collections.unmodifiableMap(injectors);
		
		MinaMod.println("Found %d ore injectors", injectors.size());
	}
	
	public static void performOreInjection(World world, Chunk chunk, String... injectorIds){
		for(String injid : injectorIds) {
			IOreInjector inj = INJECTORS.get(injid);
			if(inj != null) {
				inj.injectOre(rand, chunk.x, chunk.z, world, world.getChunkProvider());
			}else {
				MinaMod.errorln("Ore injector %s not found. Skipping...", injid);
			}
		}
	}
	
	public static Set<String> getAvailableInjectors(){
		return new HashSet<>(INJECTORS.keySet());
	}
	
}
