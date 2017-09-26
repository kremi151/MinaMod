package lu.kremi151.minamod.util.eventlisteners;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.OreInjectorManager;
import lu.kremi151.minamod.util.TaskOreInjector;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEvents {

	private MinaMod mod;
	
	public WorldEvents(MinaMod mod){
		this.mod = mod;
	}
	
	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save event){ // NO_UCD (unused code)
		NBTTagCompound newnbt = new NBTTagCompound();
		
		newnbt.setInteger(OreInjectorManager.DATA_TAG, OreInjectorManager.getChunkVersion());
		
		event.getData().setTag(MinaMod.MODID, newnbt);
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load event){ // NO_UCD (unused code)
		if(event.getData().hasKey(MinaMod.MODID)){
			NBTTagCompound newnbt = event.getData().getCompoundTag(MinaMod.MODID);
			
			if(MinaWorld.forWorld(event.getWorld()).getConfiguration().isOreInjectionEnabled()){
				int last_vcode = 0;
				if(newnbt.hasKey(OreInjectorManager.DATA_TAG, 99)){
					last_vcode = newnbt.getInteger(OreInjectorManager.DATA_TAG);
				}
				if(last_vcode < OreInjectorManager.getChunkVersion()){
					TaskOreInjector.instance().enqueueChunk(event);
				}
			}
			
//			MinaMod.debugPrintln("Chunk " + event.getChunk().xPosition + " " + event.getChunk().zPosition + ": last vcode was " + last_vcode + " (" + MinaMod.VERSION_CODE + ")");
		}else{
//			MinaMod.debugPrintln("Chunk " + event.getChunk().xPosition + " " + event.getChunk().zPosition + " hasn't minamod specific information");
		}
	}
	
	//WTF?!?
//	@SubscribeEvent
//	public void onChunkWatch(ChunkWatchEvent.Watch event){
//		World worldObj = event.getPlayer().worldObj;
//		if(!worldObj.isRemote){
//			Chunk chunk = event.getPlayer().worldObj.getChunkFromChunkCoords(event.getChunk().chunkXPos, event.getChunk().chunkZPos);
//			
//		}
//	}
	
	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load event){ // NO_UCD (unused code)
		GameRules gr = event.getWorld().getGameRules();
		if(!gr.hasRule("minamod.bushSpreading"))gr.addGameRule("minamod.bushSpreading", "true", ValueType.BOOLEAN_VALUE);
		if(!gr.hasRule("minamod.bushPopulating"))gr.addGameRule("minamod.bushPopulating", "true", ValueType.BOOLEAN_VALUE);
	}
}
