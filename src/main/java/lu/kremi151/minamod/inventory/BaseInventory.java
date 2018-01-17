package lu.kremi151.minamod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class BaseInventory implements IInventory{
	
	protected final NonNullList<ItemStack> inv;
	private final int size;
	
	public BaseInventory(int size){
		this.size = size;
		this.inv = NonNullList.withSize(size, ItemStack.EMPTY);
	}
	
	public BaseInventory(NonNullList<ItemStack> inv) {
		this.size = inv.size();
		this.inv = inv;
	}
	
	public abstract void onCraftMatrixChanged();

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
		return size;
	}
	
	private void validateIndex(int index){
		if(!(index >= 0 && index < size)){
			throw new RuntimeException("Slot index is not in range between 0 inclusive and " + size + " exclusive");
		}
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		validateIndex(index);
		return inv.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = getStackInSlot(index);
        if (!stack.isEmpty()) {
                if (stack.getCount() <= count) {
                        setInventorySlotContents(index, ItemStack.EMPTY);
                } else {
                        stack = stack.splitStack(count);
                        if (stack.getCount() == 0) {
                                setInventorySlotContents(index, ItemStack.EMPTY);
                        }
                }
        }
        onCraftMatrixChanged();
        return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		validateIndex(index);
		ItemStack is = inv.get(index);
		inv.set(index, ItemStack.EMPTY);
        onCraftMatrixChanged();
		return is;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		validateIndex(index);
		if(stack == null)throw new NullPointerException();
		boolean changed = inv.get(index) != stack;
		inv.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
                stack.setCount(getInventoryStackLimit());
        }
        if(changed)onCraftMatrixChanged();
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

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
		boolean changed = false;
		for(int i = 0 ; i < size ; i++){
			if(!inv.get(i).isEmpty())changed = true;
			inv.set(i, ItemStack.EMPTY);
		}
		if(changed)onCraftMatrixChanged();
	}

	@Override
	public boolean isEmpty() {
		for(int i = 0 ; i < size ; i++){
			if(!inv.get(i).isEmpty())return false;
		}
		return true;
	}

}
