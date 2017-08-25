package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGiftBox extends TileEntity{
	
	private GiftItemHandler inv;
	public static final String GIFT_ITEM_TAG = "giftItem";
	
	public TileEntityGiftBox(){
		this.inv = new GiftItemHandler();
	}
	
	public ItemStack getGiftItem(){
		return inv.getStackInSlot(0);
	}
	
	public void setGiftItem(ItemStack stack){
		if(stack == null)throw new NullPointerException();
		inv.setStackInSlot(0, stack);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey(TileEntityGiftBox.GIFT_ITEM_TAG, 10)){
			setGiftItem(new ItemStack(nbt.getCompoundTag(TileEntityGiftBox.GIFT_ITEM_TAG)));
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		ItemStack gift = getGiftItem();
		if(!gift.isEmpty()){
			NBTTagCompound inbt = new NBTTagCompound();
			gift.writeToNBT(inbt);
			nbt.setTag(TileEntityGiftBox.GIFT_ITEM_TAG, inbt);
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
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);//CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return (T) ((capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? inv : super.getCapability(capability, facing));
    }
    
    private static class GiftItemHandler extends ItemStackHandler{
    	
    	private GiftItemHandler(){
    		super(1);
    	}
    	
    	@Override
    	public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
    		return ItemStack.EMPTY;
        }
    }
	
}
