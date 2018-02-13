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
	private String customCaption = null;
	
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
		if(nbt.hasKey("Caption", 8)) {
			customCaption = nbt.getString("Caption");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		return serializeGravestone(super.writeToNBT(nbt), true);
	}
	
	private NBTTagCompound serializeGravestone(NBTTagCompound nbt, boolean full) {
		if(full) {
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
		}
		if(customCaption != null) {
			nbt.setString("Caption", customCaption);
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
	
	@Nullable
	public String getCaption() {
		if(customCaption != null) {
			return customCaption;
		}else if(owner != null) {
			return owner.getName();
		}else {
			return null;
		}
	}
	
	public void setCaption(String caption) {
		this.customCaption = caption;
		markDirty();
		sync();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, serializeGravestone(new NBTTagCompound(), false));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		final NBTTagCompound nbt = packet.getNbtCompound();
		if(nbt.hasKey("Caption", 8)) {
			customCaption = nbt.getString("Caption");
		}else {
			customCaption = null;
		}
        sync();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		if(customCaption != null) {
			nbt.setString("Caption", customCaption);
		}else if(owner != null) {
			nbt.setString("Caption", owner.getName());
		}
		return nbt;
	}
}
