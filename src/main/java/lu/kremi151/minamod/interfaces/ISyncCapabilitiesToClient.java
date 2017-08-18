package lu.kremi151.minamod.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ISyncCapabilitiesToClient {

	NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt);
	void readSyncableData(ItemStack stack, NBTTagCompound nbt);
}
