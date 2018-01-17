package lu.kremi151.minamod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BaseInventoryImpl extends BaseInventory{
	
	private final String name;

	public BaseInventoryImpl(String name, int size) {
		super(size);
		this.name = name;
	}

	public BaseInventoryImpl(String name, NonNullList<ItemStack> inv) {
		super(inv);
		this.name = name;
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
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onCraftMatrixChanged() {}

}
