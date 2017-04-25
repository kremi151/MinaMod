package lu.kremi151.minamod;

import lu.kremi151.minamod.biome.BiomeRural;
import lu.kremi151.minamod.biome.BiomeVeld;
import net.minecraft.world.biome.Biome;

public class MinaBiomes {

	// TODO: Biomes
	public final static Biome VELD;
	public final static Biome RURAL;
	// public static Biome biomeKremiForest;
	// public static Biome biomeCherryForest;

	private static boolean init = false;

	static {
		// TODO: Biomes
		VELD = new BiomeVeld();
		RURAL = new BiomeRural();
		// biomeKremiForest = new BiomeGenKremiForest(204).setBiomeName("Kremi
		// Forest");
		// biomeCherryForest = new
		// BiomeGenCherryForest(205).setBiomeName("Cherry Forest");
	}

	static void init() {
		if (init)throw new RuntimeException("Duplicate call of function");

		int start_id = 208;
		Biome.registerBiome(start_id, "Veld", VELD);
		Biome.registerBiome(start_id + 1, "Rural", RURAL);

		init = true;
	}

}
