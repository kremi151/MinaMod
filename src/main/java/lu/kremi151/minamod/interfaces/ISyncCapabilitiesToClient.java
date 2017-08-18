package lu.kremi151.minamod.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ISyncCapabilitiesToClient {

	default boolean canSyncCapabilitiesToClient(ItemStack stack) {
		return true;
	}
	
	NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt);
	void readSyncableData(ItemStack stack, NBTTagCompound nbt);
}
