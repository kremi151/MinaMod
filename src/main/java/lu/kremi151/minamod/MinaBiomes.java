package lu.kremi151.minamod;

import lu.kremi151.minamod.biome.BiomeRural;
import lu.kremi151.minamod.biome.BiomeVeld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinaBiomes {

	// TODO: Biomes
	public final static Biome VELD;
	public final static Biome RURAL;

	private static boolean init = false;

	static {
		// TODO: Biomes
		VELD = new BiomeVeld().setRegistryName(new ResourceLocation(MinaMod.MODID, "veld"));
		RURAL = new BiomeRural().setRegistryName(new ResourceLocation(MinaMod.MODID, "rural"));
	}

	static void init() {
		if (init)throw new RuntimeException("Duplicate call of function");

		GameRegistry.register(VELD);
		GameRegistry.register(RURAL);

		init = true;
	}

}
