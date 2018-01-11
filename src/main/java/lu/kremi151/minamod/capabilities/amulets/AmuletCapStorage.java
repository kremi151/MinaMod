package lu.kremi151.minamod.capabilities.amulets;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class AmuletCapStorage implements Capability.IStorage<IAmulet> {

	@Override
	public NBTBase writeNBT(Capability<IAmulet> capability, IAmulet instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Amulet", instance.saveData(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public void readNBT(Capability<IAmulet> capability, IAmulet instance, EnumFacing side, NBTBase nbt) {
		if(nbt instanceof NBTTagCompound) {
			NBTTagCompound nbtc = (NBTTagCompound) nbt;
			if(nbtc.hasKey("Amulet", 10)) {
				instance.loadData(nbtc);
			}
		}
	}

}
