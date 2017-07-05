package lu.kremi151.minamod.block.tileentity;

import java.util.UUID;

import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLock extends TileEntity{
	
	private UUID uuid = null;
	boolean powered = false;

	public boolean checkUUID(IKey key){
		if(this.uuid == null){
			return true;
		}
		return key.canUnlock(uuid);
	}
	
	public boolean isRegistred(){
		return uuid != null;
	}
	
	public boolean isPowered(){
		return powered;
	}
	
	public void setPowered(boolean v){
		this.powered = v;
	}
	
	public void setUUID(UUID uuid){
		this.uuid = uuid;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("UUID", 10)){
			this.uuid = MinaUtils.convertNBTToUUID(nbt.getCompoundTag("UUID"));
		}
		this.powered = nbt.getBoolean("Powered");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(uuid != null){
			nbt.setTag("UUID", MinaUtils.convertUUIDToNBT(this.uuid));
		}
		nbt.setBoolean("Powered", powered);
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound tagPacket = new NBTTagCompound();
		writeToNBT(tagPacket);
		return new SPacketUpdateTileEntity(this.pos, 0, tagPacket);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}

}
