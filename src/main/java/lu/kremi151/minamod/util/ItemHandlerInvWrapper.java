package lu.kremi151.minamod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerInvWrapper implements IInventory{
	
	private IItemHandler handler;
	
	public ItemHandlerInvWrapper(IItemHandler handler) {
		this.handler = handler;
	}

	@Override
	public String getName() {
		return "wrapped";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getName());
	}

	@Override
	public int getSizeInventory() {
		return handler.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int slot = 0; slot < handler.getSlots(); slot++)
        {
            ItemStack stackInSlot = handler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0)
            {
                return false;
            }
        }
        return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return index >= 0 && index < handler.getSlots() && !handler.getStackInSlot(index).isEmpty() && count > 0 ? handler.getStackInSlot(index).splitStack(count) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = handler.getStackInSlot(index);
		if(!stack.isEmpty()) {
			return handler.extractItem(index, stack.getCount(), false);
		}else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		handler.insertItem(index, stack, false);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int slot = 0 ; slot < handler.getSlots() ; slot++) {
			if(!handler.getStackInSlot(slot).isEmpty())handler.extractItem(slot, handler.getStackInSlot(slot).getCount(), false);
		}
	}

}
