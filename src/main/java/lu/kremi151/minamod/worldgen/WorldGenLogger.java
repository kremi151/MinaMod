package lu.kremi151.minamod.worldgen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.math.BlockPos;

public class WorldGenLogger {

	private static WorldGenLogger i;
	
	private Logger logger;
	
	private WorldGenLogger(){
		logger = LogManager.getLogger(WorldGenLogger.class);
	}
	
	public static WorldGenLogger get(){
		if(i == null){
			i = new WorldGenLogger();
		}
		return i;
	}
	
	public void log(String text){
		logger.info(text);
	}
	
	public void logBuilding(String building_name, BlockPos pos){
		logBuilding(building_name, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void logBuilding(String building_name, int x, int y, int z){
		logger.info("Generated %s at x=%d, y=%d, z=%d", building_name, x, y, z);
	}
}
