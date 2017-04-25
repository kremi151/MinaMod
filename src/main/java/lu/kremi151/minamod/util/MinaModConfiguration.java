package lu.kremi151.minamod.util;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MinaModConfiguration {
	
	public static final String CATEGORY_DEVELOPER = "developer";
	public static final String CATEGORY_TERRAIN = "terrain";
	public static final String CATEGORY_FEATURES = "features";
	public static final String CATEGORY_DATA = "data";
	
	private Configuration config;

    private boolean debug;
    private boolean log_world_gen;
    private int save_interval;
    private boolean enable_soul_pearl;
    private boolean enable_player_stats;    
    private boolean use_fancy_elevator_movement;
    private boolean use_new_honeycomb_generation;

    @SideOnly(Side.CLIENT)
    private int overlay_x;
    @SideOnly(Side.CLIENT)
    private int overlay_y;
    @SideOnly(Side.CLIENT)
    private boolean enable_overlay;
	
	public MinaModConfiguration(Configuration config){
		this.config = config;
		
    	reload();
		
		config.setCategoryRequiresMcRestart("dimension", true);
	}
	
	public void load(){
		config.load();
	}
	
	public void save(){
		config.save();
	}
	
	public void reload(){
		debug = config.getBoolean("debug", CATEGORY_DEVELOPER, false, "Only for developer purposes, set to true to see development specific information in the game log");
		log_world_gen = config.getBoolean("log_world_gen", CATEGORY_DEVELOPER, false, "Only for developer purposes, set true to log world generating related information");
		save_interval = config.getInt("save_interval", CATEGORY_DATA, 15, 1, 120, "The interval in minutes for the automatic saving of Mina Mods data");
		enable_soul_pearl = config.getBoolean("soulpearl", CATEGORY_FEATURES, true, "Defines if Soul pearls should be enabled");
		enable_player_stats = config.getBoolean("player_stats", CATEGORY_FEATURES, true, "Defines if the player stats system should be enabled");
		use_new_honeycomb_generation = config.getBoolean("new_honeycomb_generation", CATEGORY_TERRAIN, true, "Defines if the new method to generate honeycombs in the world should be used");
		use_fancy_elevator_movement = config.getBoolean("use_fancy_elevator_movement", CATEGORY_FEATURES, false, "Defines is elevators should use the fancy but slow transport between levels. Set to false to teleport instantly.");
		
		if(!MinaMod.getProxy().isServerSide()){
			enable_overlay = config.getBoolean("enable_overlay", Configuration.CATEGORY_CLIENT, true, "Enables or disables overlay elements");
			overlay_x = config.getInt("overlay_x", Configuration.CATEGORY_CLIENT, 2, 0, 1900, "The starting x position for the overlay elements");
			overlay_y = config.getInt("overlay_y", Configuration.CATEGORY_CLIENT, 2, 0, 1900, "The starting y position for the overlay elements");
		}
		
		if(config.hasChanged()){
			config.save();
		}
	}
	
	public Configuration getConfiguration(){
		return config;
	}
	
	public boolean isDebugging(){
		return debug;
	}
	
	public boolean canLogWorldGenInfo(){
		return log_world_gen;
	}
	
	public int getSaveInterval(){
		return save_interval;
	}
	
	public boolean enableSoulPearls(){
		return enable_soul_pearl;
	}
	
	public boolean enablePlayerStats(){
		return enable_player_stats;
	}
	
	public boolean useNewHoneycombGeneration(){
		return use_new_honeycomb_generation;
	}
	
	public boolean useFancyElevatorMovement(){
		return use_fancy_elevator_movement;
	}
	
	public boolean getCustomBoolean(String node, String category){ // NO_UCD (unused code)
		if(config.hasKey(category, node)){
			return config.getBoolean(node, category, false, "");
		}
		return false;
	}
	
	public int getCustomInt(String node, String category, int def){
		if(config.hasKey(category, node)){
			return config.getInt(node, category, def, Integer.MIN_VALUE, Integer.MAX_VALUE, "");
		}
		return def;
	}

    @SideOnly(Side.CLIENT)
    public int getOverlayX(){
    	return overlay_x;
    }

    @SideOnly(Side.CLIENT)
    public int getOverlayY(){
    	return overlay_y;
    }

    @SideOnly(Side.CLIENT)
    public boolean isOverlayEnabled(){
    	return enable_overlay;
    }
	
//	public NBTTagCompound getImportantPropertiesAsNBT(){
//		NBTTagCompound tag = new NBTTagCompound();
//		
//		return tag;
//	}
}
