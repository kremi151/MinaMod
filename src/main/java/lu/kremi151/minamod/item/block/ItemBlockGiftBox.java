package lu.kremi151.minamod.item.block;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.tileentity.TileEntityGiftBox.GiftItemHandler;
import lu.kremi151.minamod.capabilities.owner.IOwner;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemBlockGiftBox extends ItemCloth implements ISyncCapabilitiesToClient{
	
	public ItemBlockGiftBox(Block block) {
		super(block);
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
    	boolean has_item = false;
		if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ItemStack stack2 = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
			if(!stack2.isEmpty()) {
				IOwner owner = stack.getCapability(IOwner.CAPABILITY, null);
				if(owner != null && owner.getOwner() != null && owner.getOwner().equals(player.getUniqueID())) {
					tooltip.add(I18n.translateToLocalFormatted("tile.gift_box.contains", stack2.getCount(), stack2.getDisplayName()));
				}else {
					tooltip.add(I18n.translateToLocal("tile.gift_box.has_item"));
				}
				
				has_item = true;
			}
		}
		if(!has_item) {
			tooltip.add(I18n.translateToLocal("tile.gift_box.no_item"));
		}
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName();
    }
	
	private static NBTTagCompound serializeCapability(NBTTagCompound nbt, IItemHandler inv, IOwner owner) {
		if(!inv.getStackInSlot(0).isEmpty()) {
			NBTTagCompound inbt = new NBTTagCompound();
			inv.getStackInSlot(0).writeToNBT(inbt);
			nbt.setTag("Item", inbt);
		}
		if(owner.getOwner() != null) {
			nbt.setTag("Owner", IOwner.CAPABILITY.writeNBT(owner, null));
		}
		return nbt;
	}
	
	private static void deserializeCapability(NBTTagCompound nbt, GiftItemHandler inv, IOwner owner) {
		inv.clear();
		if(nbt.hasKey("Item", 10)) {
			ItemStack stack = new ItemStack(nbt.getCompoundTag("Item"));
			inv.insertItem(0, stack, false);
		}
		if(nbt.hasKey("Owner", 10)) {
			IOwner.CAPABILITY.readNBT(owner, null, nbt.getTag("Owner"));
		}
	}

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		return serializeCapability(nbt, stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), stack.getCapability(IOwner.CAPABILITY, null));
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		deserializeCapability(nbt, (GiftItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), stack.getCapability(IOwner.CAPABILITY, null));
	}
	
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ItemBlockGiftBoxItemProvider();
    }
	
	private static class ItemBlockGiftBoxItemProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{

		private final GiftItemHandler inv = new GiftItemHandler();
		private final IOwner owner = IOwner.CAPABILITY.getDefaultInstance();
		
		@Override
		public NBTTagCompound serializeNBT() {
			return serializeCapability(new NBTTagCompound(), inv, owner);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			deserializeCapability(nbt, inv, owner);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
					|| capability == IOwner.CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				return (T) inv;
			}else if(capability == IOwner.CAPABILITY) {
				return (T) owner;
			}else {
				return null;
			}
		}
		
	}

}
