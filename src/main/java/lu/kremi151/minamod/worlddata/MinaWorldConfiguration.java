package lu.kremi151.minamod.worlddata;

import lu.kremi151.minamod.util.FeatureList;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class MinaWorldConfiguration {

	private static final String CATEGORY_ORE_INJECTION = "oreInjection";
	private static final String CATEGORY_STRUCTURE_WOOKIE_HOUSE = "structureWookieHouse";
	
	private Configuration conf;
	
	private boolean ore_injection = false;
	
	private boolean structure_wookie_house;
	private Biome structure_wookie_biome;

	public MinaWorldConfiguration(Configuration conf){
		this.conf = conf;
		
    	reload();
	}
	
	public void reload(){
		ore_injection = conf.getBoolean("enabled", CATEGORY_ORE_INJECTION, false, "EXPERIMENTAL FEATURE!!! It lets generate new added ores on already created chunks if you update the Mina mod");

		structure_wookie_house = conf.getBoolean("generate", CATEGORY_STRUCTURE_WOOKIE_HOUSE, true, "Defines if wookie houses should be generated for this world");
		structure_wookie_biome = getBiome(conf, "biome", CATEGORY_STRUCTURE_WOOKIE_HOUSE, Biomes.FOREST, "The biome for wookie houses to generate. This value should be in resource location style like {domain}:{value}");
		
		if(conf.hasChanged()){
			conf.save();
		}
	}
	
	private Biome getBiome(Configuration conf, String key, String category, Biome def, String comment){
		ResourceLocation rl = new ResourceLocation(conf.getString(key, category, def.getRegistryName().toString(), comment));
		Biome entry = Biome.REGISTRY.getObject(rl);
		if(entry == null){
			return def;
		}else{
			return entry;
		}
	}
	
	@SuppressWarnings("unused")
	public boolean isOreInjectionEnabled(){
		return FeatureList.enable_ore_injection && ore_injection;
	}
	
	public boolean shouldGenerateWookieHouses(){
		return structure_wookie_house;
	}
	
	public Biome getBiomeForWookieHouses(){
		return structure_wookie_biome;
	}
}
