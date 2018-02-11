package lu.kremi151.minamod.capabilities.amulets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IAmuletHolder {

	ItemStack getAmuletAt(int slot);
	boolean setAmuletAt(int slot, ItemStack amulet);
	int amuletAmount();
	NonNullList<ItemStack> getAmulets();
	void clear();
	
}
