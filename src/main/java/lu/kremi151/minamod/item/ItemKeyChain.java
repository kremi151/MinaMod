package lu.kremi151.minamod.item;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemKeyChain extends Item{

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new KeyChainCapProvider(stack);
    }
	
	private static class KeyChainCapProvider implements ICapabilityProvider{

		private ItemStack stack;
		private ItemStackHandler inv;
		
		private KeyChainCapProvider(ItemStack stack){
			this.stack = stack;
			this.inv = new ItemStackHandler(8);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
				return (T) inv;
			}
			return null;
		}
		
	}
}
