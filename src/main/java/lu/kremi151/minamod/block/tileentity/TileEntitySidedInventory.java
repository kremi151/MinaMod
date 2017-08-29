package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class TileEntitySidedInventory extends TileEntity implements ISidedInventory{
	
	private final NonNullList<ItemStack> inv;
	private final int defaultSlots[], size;
	private final String inventoryName;
	protected final IItemHandler itemHandler;
	
	protected TileEntitySidedInventory(int size, String inventoryName){
		this.size = size;
		this.inv = NonNullList.withSize(size, ItemStack.EMPTY);
		this.inventoryName = inventoryName;
		this.defaultSlots = new int[size];
		for(int i = 0 ; i < size ; i++)this.defaultSlots[i] = i;
		this.itemHandler = new InvWrapper(this);
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
        markDirty();
        return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		validateIndex(index);
		ItemStack is = inv.get(index);
		inv.set(index, ItemStack.EMPTY);
        markDirty();
		return is;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(stack == null)throw new NullPointerException("Cannot set null as an item");
		validateIndex(index);
		boolean changed = inv.get(index) != stack;
		inv.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
                stack.setCount(getInventoryStackLimit());
        }
        if(changed)onCraftMatrixChanged();
        markDirty();
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
        markDirty();
		if(changed)onCraftMatrixChanged();
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getName() {
		return inventoryName;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return defaultSlots;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		validateIndex(index);
		ItemStack stack = inv.get(index);
		return stack.isEmpty() || MinaUtils.areItemsStackable(stack, itemStackIn, true);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for(int i = 0 ; i < size ; i++){
			if(!inv.get(i).isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	protected final void loadItems(NBTTagCompound nbt){
		if(nbt.hasKey("Items", 9)){
			ItemStackHelper.loadAllItems(nbt, inv);
		}
	}
	
	protected final void saveItems(NBTTagCompound nbt){
		ItemStackHelper.saveAllItems(nbt, inv);
	}
	
	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
        	return true;
        }else{
        	return super.hasCapability(capability, facing);
        }
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
        	return (T) itemHandler;
        }else{
        	return super.getCapability(capability, facing);
        }
    }

}
