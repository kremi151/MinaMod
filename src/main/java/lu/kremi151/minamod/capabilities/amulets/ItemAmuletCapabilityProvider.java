package lu.kremi151.minamod.capabilities.amulets;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class ItemAmuletCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
	
	private final IAmulet cap;
	
	public ItemAmuletCapabilityProvider(IAmulet capability) {
		this.cap = capability;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == IAmulet.CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return (capability == IAmulet.CAPABILITY) ? (T)cap : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Amulet", cap.saveData(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("Amulet", 10)) {
			cap.loadData(nbt.getCompoundTag("Amulet"));
		}
	}

}
