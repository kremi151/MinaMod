package lu.kremi151.minamod.worlddata;

import java.io.File;
import java.util.Optional;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class MinaWorldConfiguration {

	private static final String CATEGORY_ORE_INJECTION = "oreInjection";
	private static final String CATEGORY_STRUCTURE_WOOKIE_HOUSE = "structureWookieHouse";
	
	private Configuration conf;
	
	private boolean structure_wookie_house;
	private Biome structure_wookie_biome;
	private File custom_oreinj_pwd;

	public MinaWorldConfiguration(Configuration conf){
		this.conf = conf;
		
    	reload();
	}
	
	public void reload(){
		structure_wookie_house = conf.getBoolean("generate", CATEGORY_STRUCTURE_WOOKIE_HOUSE, true, "Defines if wookie houses should be generated for this world");
		structure_wookie_biome = getBiome(conf, "biome", CATEGORY_STRUCTURE_WOOKIE_HOUSE, Biomes.FOREST, "The biome for wookie houses to generate. This value should be in resource location style like {domain}:{value}");
		String custom_oreinj_pwd = conf.getString("pwd", CATEGORY_STRUCTURE_WOOKIE_HOUSE, "", "Defined the custom working directory of the ore injector for this world. The value null or an empty string will apply the default one.");
		if(custom_oreinj_pwd != null && custom_oreinj_pwd.length() > 0) {
			this.custom_oreinj_pwd = new File(custom_oreinj_pwd);
		}
		
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
	
	public boolean shouldGenerateWookieHouses(){
		return structure_wookie_house;
	}
	
	public Biome getBiomeForWookieHouses(){
		return structure_wookie_biome;
	}
	
	public Optional<File> getCustomOreInjectorWorkingPath(){
		return Optional.ofNullable(custom_oreinj_pwd);
	}
	
	public void setCustomOreInjectorWorkingPath(File path) {
		this.custom_oreinj_pwd = path;
		conf.get(CATEGORY_ORE_INJECTION, "pwd", "").set(path.getAbsolutePath());
		conf.save();
	}
}
