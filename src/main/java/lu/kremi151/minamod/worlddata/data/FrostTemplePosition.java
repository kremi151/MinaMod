package lu.kremi151.minamod.worlddata.data;

import java.util.Optional;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumFrostTempleType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class FrostTemplePosition {
	
	private final FrostTempleMeta parent;

	private final BlockPos altar_pos;
	private final EnumFrostTempleType type;
	private boolean available = true;
	
	public FrostTemplePosition(FrostTempleMeta parent, BlockPos altar_pos, EnumFrostTempleType type){
		this.altar_pos = altar_pos;
		this.type = type;
		this.parent = parent;
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		nbt.setInteger("altar_x", altar_pos.getX());
		nbt.setInteger("altar_y", altar_pos.getY());
		nbt.setInteger("altar_z", altar_pos.getZ());
		nbt.setInteger("type", type.getType());
		nbt.setBoolean("available", available);
	}
	
	public static Optional<FrostTemplePosition> createFromNBT(FrostTempleMeta parent, NBTTagCompound nbt){
		BlockPos altar_pos = new BlockPos(
				nbt.getInteger("altar_x"),
				nbt.getInteger("altar_y"),
				nbt.getInteger("altar_z")
				);
		try{
			EnumFrostTempleType type = EnumFrostTempleType.getByType(nbt.getInteger("type"));
			FrostTemplePosition ftp = new FrostTemplePosition(parent, altar_pos, type);
			if(nbt.hasKey("available")){
				ftp.setAvailable(nbt.getBoolean("available"));
			}
			return Optional.of(ftp);
		}catch(Throwable t){
			MinaMod.println("Errored frost temple entry at %s, skipping", altar_pos.toString());
		}
		return Optional.empty();
	}
	
	public BlockPos getAltarPosition(){
		return altar_pos;
	}
	
	public EnumFrostTempleType getType(){
		return type;
	}
	
	public FrostTemplePosition setAvailable(boolean v){
		this.available = v;
		parent.getExtent().markDirty();
		return this;
	}
	
	public boolean isAvailable(){
		return available;
	}
	
	@Override
	public String toString(){
		return "FrostTemplePosition{x=" + altar_pos.getX() + ",y=" + altar_pos.getY() + ",z=" + altar_pos.getZ() + "}";
	}
	
}
