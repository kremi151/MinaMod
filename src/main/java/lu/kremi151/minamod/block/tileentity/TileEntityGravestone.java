package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		items.clear();
		for(int i = 0 ; i < nbttaglist.tagCount() ; i++) {
			ItemStack stack = new ItemStack(nbttaglist.getCompoundTagAt(i));
			if(!stack.isEmpty()) {
				items.add(stack);
			}
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
		NBTTagList nbttaglist = new NBTTagList();
		for(ItemStack stack : items) {
			if(!stack.isEmpty()) {
				nbttaglist.appendTag(stack.writeToNBT(new NBTTagCompound()));
			}
		}
		nbt.setTag("Items", nbttaglist);
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
		final NBTTagCompound nbt = packet.getNbtCompound();
		if(nbt.hasKey("Owner", 10)) {
			owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("Owner"));
		}else {
			owner = null;
		}
        sync();
	}
}
