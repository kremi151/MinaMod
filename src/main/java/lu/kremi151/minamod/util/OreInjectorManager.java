package lu.kremi151.minamod.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.annotations.OreInjector;
import lu.kremi151.minamod.interfaces.IOreInjector;
import lu.kremi151.minamod.worldgen.WorldGenerators;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreInjectorManager {
	
	public static final String DATA_TAG = "oi";

	private static Bundle injectors[];
	private static int highestVersion = 0;
	private static Random rand = new Random(System.currentTimeMillis());
	
	public static void init(){
		Field fields[] = WorldGenerators.class.getDeclaredFields();
		ArrayList<Bundle> wg = new ArrayList<Bundle>();
		for(Field f : fields){
			f.setAccessible(true);
			OreInjector oi = f.getAnnotation(OreInjector.class);
			if(IOreInjector.class.isAssignableFrom(f.getType()) && oi != null){
				try {
					wg.add(new Bundle((IOreInjector)f.get(null), oi.chunkVersion()));
					if(oi.chunkVersion() > highestVersion){
						highestVersion = oi.chunkVersion();
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		injectors = new Bundle[wg.size()];
		injectors = wg.toArray(injectors);
		
		MinaMod.println("Found %d ore injectors with version %d", injectors.length, highestVersion);
	}
	
	public static int getChunkVersion(){
		return highestVersion;
	}
	
	public static void performOreInjection(World world, Chunk chunk, int chunkVersion){
		for(Bundle w : injectors){
			if(chunkVersion < w.versionCode){
				w.injector.injectOre(rand, chunk.xPosition, chunk.zPosition, world, world.getChunkProvider());
			}
		}
	}
	
	private static class Bundle{
		final IOreInjector injector;
		final int versionCode;
		private Bundle(IOreInjector injector, int versionCode){
			this.injector = injector;
			this.versionCode = versionCode;
		}
	}
}
