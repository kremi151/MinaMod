package lu.kremi151.minamod.util;

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

	private static final Map<String, IOreInjector> INJECTORS = Collections.unmodifiableMap(
			new AnnotationProcessor<>(OreInjector.class, IOreInjector.class)
				.processWithResult(WorldGenerators.class, (oi, injector, map) -> {
			    	if(!map.containsKey(oi.value())) {
						map.put(oi.value(), injector);
					}else {
						throw new RuntimeException("Ore injector with id " + oi.value() + " already defined");
					}
			    	return map;
			    }, new HashMap<String, IOreInjector>()));
	
	private static Random rand = new Random(System.currentTimeMillis());
	
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
