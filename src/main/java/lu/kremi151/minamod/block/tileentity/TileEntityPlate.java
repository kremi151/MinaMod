package lu.kremi151.minamod.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlate extends TileEntity{
	
	private ItemStack item = ItemStack.EMPTY;
	
	public TileEntityPlate(){
		super();
	}
	
	public void setItem(ItemStack is){
		if(is == null)throw new NullPointerException();
		item = is.copy();
		item.setCount(1);
		is.shrink(1);
		markDirty();
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("plate_item")){
			NBTTagCompound inbt = nbt.getCompoundTag("plate_item");
			this.item = new ItemStack(inbt);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(!item.isEmpty()){
			NBTTagCompound inbt = new NBTTagCompound();
			item.writeToNBT(inbt);
			nbt.setTag("plate_item", inbt);
		}
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
