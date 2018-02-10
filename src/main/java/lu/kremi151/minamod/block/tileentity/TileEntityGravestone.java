package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityGravestone extends BaseTileEntity{

	private final NonNullList<ItemStack> items = NonNullList.create();
	private GameProfile owner;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Items", 9)) {
			items.clear();
			ItemStackHelper.loadAllItems(nbt, items);
		}
		if(nbt.hasKey("Owner", 10)) {
			owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("Owner"));
		}else {
			owner = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		ItemStackHelper.saveAllItems(nbt, items, false);
		if(owner != null) {
			nbt.setTag("Owner", NBTUtil.writeGameProfile(new NBTTagCompound(), owner));
		}
		return nbt;
	}
	
	public NonNullList<ItemStack> getItems(){
		return items;
	}
	
	@Nullable
	public GameProfile getOwner() {
		return owner;
	}
	
	public void setOwner(@Nullable GameProfile owner) {
		this.owner = owner;
		markDirty();
		sync();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound tagPacket = new NBTTagCompound();
		if(owner != null) {
			tagPacket.setTag("Owner", NBTUtil.writeGameProfile(new NBTTagCompound(), owner));
		}
		return new SPacketUpdateTileEntity(this.pos, 0, tagPacket);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        sync();
	}
	
	@Override
	public void onLoad()
    {
        if(world != null && !world.isRemote) {
        	System.out.println("Grave loaded");
        }
    }
}
