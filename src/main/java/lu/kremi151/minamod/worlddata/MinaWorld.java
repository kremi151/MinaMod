package lu.kremi151.minamod.worlddata;

import java.io.File;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.worlddata.data.FrostTempleMeta;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.config.Configuration;

public class MinaWorld extends WorldSavedData{
	
	private static File worldsSaveDir = null;
	
	private FrostTempleMeta ftmeta;
	private MinaWorldConfiguration conf;
	private String dimenName;

	private MinaWorld(World w) {
		super(MinaMod.MODID);
		ftmeta = new FrostTempleMeta(this);
		dimenName = w.getWorldInfo().getWorldName();
		
		if(worldsSaveDir == null){
			worldsSaveDir = new File(MinaMod.minaConfigPath.get(), "worlds");
			if(!worldsSaveDir.exists()){
				worldsSaveDir.mkdirs();
			}
		}
		
		loadConfig();
	}
	
	private void loadConfig(){
		Configuration config = new Configuration(new File(worldsSaveDir, dimenName + ".cfg"));
		config.load();
		conf = new MinaWorldConfiguration(config);
		config.save();
	}
	
	public FrostTempleMeta getFrostTempleMeta(){
		return ftmeta;
	}
	
	public MinaWorldConfiguration getConfiguration(){
		return conf;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey(MinaMod.MODID)){
			NBTTagCompound mina = nbt.getCompoundTag(MinaMod.MODID);
			if(mina.hasKey("frostMeta")){
				NBTTagCompound fnbt = mina.getCompoundTag("frostMeta");
				ftmeta.readFromNBT(fnbt);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound mina = new NBTTagCompound();
		
		NBTTagCompound fnbt = new NBTTagCompound();
		ftmeta.writeToNBT(fnbt);
		mina.setTag("frostMeta", fnbt);
		
		nbt.setTag(MinaMod.MODID, mina);
		return nbt;
	}
	
	public static MinaWorld forWorld(World world) {
		MapStorage storage = world.getPerWorldStorage();
		MinaWorld result = (MinaWorld)storage.getOrLoadData(MinaWorld.class, MinaMod.MODID);
		if (result == null) {
			result = new MinaWorld(world);
			storage.setData(MinaMod.MODID, result);
		}
		return result;
	}

}
