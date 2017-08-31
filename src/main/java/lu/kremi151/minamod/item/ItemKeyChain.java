package lu.kremi151.minamod.item;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemKeyChain extends Item implements ISyncCapabilitiesToClient{
	
	public ItemKeyChain() {
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
    	if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
    		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    		int count = 0;
    		for(int i = 0 ; i < handler.getSlots() ; i++) {
    			if(handler.getStackInSlot(i).hasCapability(IKey.CAPABILITY_KEY, null)) {
    				count++;
    			}
    		}
    		tooltip.add(I18n.translateToLocalFormatted("item.key_chain.amount", count, handler.getSlots()));
    	}
		tooltip.add(I18n.translateToLocal("item.key_chain.lore"));
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(handIn == EnumHand.MAIN_HAND) {
			playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdKeyChain, worldIn, playerIn.inventory.currentItem, 0, 0);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}else {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
    }

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new KeyChainCapProvider(stack);
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		return serializeCapability((KeyChainItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), nbt);
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		deserializeCapability((KeyChainItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
				, (KeyChainCapability) stack.getCapability(IKey.CAPABILITY_KEY, null)
				, nbt);
	}
	
	private static NBTTagCompound serializeCapability(KeyChainItemHandler inv, NBTTagCompound nbt) {
		nbt.setTag("Keys", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null));
		return nbt;
	}
	
	private static void deserializeCapability(KeyChainItemHandler inv, KeyChainCapability key, NBTTagCompound nbt) {
		if(nbt.hasKey("Keys")) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt.getTag("Keys"));
		}else {
			inv.clear();
		}
		key.rebuildKeys();
	}
	
	private static class KeyChainCapProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{

		private final ItemStack stack;
		private final KeyChainItemHandler inv;
		private final KeyChainCapability key;
		
		private KeyChainCapProvider(ItemStack stack){
			this.stack = stack;
			this.inv = new KeyChainItemHandler(this, 8);
			this.key = new KeyChainCapability(this);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == IKey.CAPABILITY_KEY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
				return (T) inv;
			}else if(capability == IKey.CAPABILITY_KEY) {
				return (T) key;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return serializeCapability(inv, new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			deserializeCapability(inv, key, nbt);
		}
		
	}
	
	private static class KeyChainItemHandler extends ItemStackHandler{
		
		private final KeyChainCapProvider parent;
		
		private KeyChainItemHandler(KeyChainCapProvider parent, int size) {
			super(size);
			this.parent = parent;
		}
		
		private boolean isValidItem(ItemStack stack) {
			return stack.isEmpty() || stack.getItem() == MinaItems.KEY;
		}
		
		@Override
	    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	    {
			if(isValidItem(stack)) {
				super.setStackInSlot(slot, stack);
				parent.key.rebuildKeys();
			}
	    }
		
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	    {
			if(isValidItem(stack)) {
				ItemStack res = super.insertItem(slot, stack, simulate);
				if(res.isEmpty() || res.getCount() != stack.getCount()) {
					parent.key.rebuildKeys();
				}
				return res;
			}else {
				return stack;
			}
	    }
		
		private void clear() {
			for(int i = 0 ; i < this.stacks.size() ; i++) {
				this.stacks.set(i, ItemStack.EMPTY);
			}
		}
	}
	
	private static class KeyChainCapability implements IKey{
		
		private final KeyChainCapProvider parent;
		private final HashSet<UUID> keys = new HashSet<>();
		
		private KeyChainCapability(KeyChainCapProvider parent) {
			this.parent = parent;
		}
		
		private void rebuildKeys() {
			keys.clear();
			for(int i = 0 ; i < parent.inv.getSlots() ; i++) {
				ItemStack pkey = parent.inv.getStackInSlot(i);
				if(pkey.hasCapability(IKey.CAPABILITY_KEY, null)) {
					IKey key = pkey.getCapability(IKey.CAPABILITY_KEY, null);
					keys.addAll(key.getKeyIds());
				}
			}
		}

		@Override
		public boolean canUnlock(UUID uuid) {
			return keys.contains(uuid);
		}

		@Override
		public void registerUnlockable(UUID uuid) {}

		@Override
		public boolean empty() {
			return keys.size() == 0;
		}

		@Override
		public Set<UUID> getKeyIds() {
			return Collections.unmodifiableSet(keys);
		}
		
	}
}
