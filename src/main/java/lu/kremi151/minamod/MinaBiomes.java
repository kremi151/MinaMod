package lu.kremi151.minamod;

public class MinaBiomes {

	// TODO: Biomes
	/*public final static Biome VELD;
	public final static Biome RURAL;*/

	private static boolean init = false;

	static {
		// TODO: Biomes
		/*VELD = new BiomeVeld().setRegistryName(new ResourceLocation(MinaMod.MODID, "veld"));
		RURAL = new BiomeRural().setRegistryName(new ResourceLocation(MinaMod.MODID, "rural"));*/
	}

	static void init() {
		if (init)throw new RuntimeException("Duplicate call of function");

		/*GameRegistry.register(VELD);
		GameRegistry.register(RURAL);*/
		MinaMod.getLogger().info("MinaMod biomes have been removed");

		init = true;
	}

}
