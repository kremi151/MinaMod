package lu.kremi151.minamod.block.tileentity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lu.kremi151.minamod.block.BlockLock;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.exceptions.InvalidFormatException;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLock extends TileEntity{
	
	private final Set<UUID> lockIds = new HashSet<>();
	private boolean powered = false;
	
	public boolean tryUnlock(IKey key) {
		if(lockIds.isEmpty()) {
			lockIds.addAll(key.getKeyIds());
			return true;
		}else {
			for(UUID keyId : key.getKeyIds()) {
				if(lockIds.contains(keyId)) {
					return true;
				}
			}
		}
		return false;
	}

	/*public boolean checkUUID(IKey key){
		if(this.uuid == null){
			return true;
		}
		return key.canUnlock(uuid);
	}*/
	
	/*public boolean isRegistred(){
		return !lockIds.isEmpty();
	}*/
	
	public boolean isPowered(){
		return powered;
	}
	
	public void setPowered(boolean v){
		boolean old = this.powered;
		this.powered = v;
		if(old != this.powered) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state.withProperty(BlockLock.POWERED, old), state.withProperty(BlockLock.POWERED, powered), 3);
		}
	}
	
	/*public void setUUID(UUID uuid){
		this.uuid = uuid;
	}*/
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		/*if(nbt.hasKey("UUID", 10)){
			this.uuid = MinaUtils.convertNBTToUUID(nbt.getCompoundTag("UUID"));
		}*/
		if(nbt.hasKey("KeyIds", 9)) {
			NBTTagList keyIds = nbt.getTagList("KeyIds", 10);
			for(NBTBase bnbt : keyIds) {
				if(bnbt instanceof NBTTagCompound) {
					try {
						this.lockIds.add(MinaUtils.convertNBTToUUID((NBTTagCompound)bnbt));
					}catch(InvalidFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.powered = nbt.getBoolean("Powered");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		writeSharedToNBT(super.writeToNBT(nbt));
		/*if(uuid != null){
			nbt.setTag("UUID", MinaUtils.convertUUIDToNBT(this.uuid));
		}*/
		NBTTagList keyIds = new NBTTagList();
		for(UUID keyId : this.lockIds) {
			keyIds.appendTag(MinaUtils.convertUUIDToNBT(keyId));
		}
		nbt.setTag("KeyIds", keyIds);
		return nbt;
	}
	
	private NBTTagCompound writeSharedToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("Powered", powered);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
        return writeSharedToNBT(super.getUpdateTag());
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());

		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state.getBlock().getActualState(state, world, pos), 3);
	}

}
