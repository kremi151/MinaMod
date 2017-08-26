package lu.kremi151.minamod.capabilities.owner;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class OwnerImpl implements IOwner{
	
	private UUID owner = null;
	
	public OwnerImpl() {}
	
	public OwnerImpl(UUID owner) {
		this.owner = owner;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public static class Storage implements Capability.IStorage<IOwner>{
		
		public Storage() {}

		@Override
		public NBTBase writeNBT(Capability<IOwner> capability, IOwner instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			if(instance.getOwner() != null) {
				nbt.setUniqueId("Owner", instance.getOwner());
			}
			return nbt;
		}

		@Override
		public void readNBT(Capability<IOwner> capability, IOwner instance, EnumFacing side, NBTBase nbt) {
			if(nbt instanceof NBTTagCompound) {
				NBTTagCompound nbtc = (NBTTagCompound) nbt;
				if(nbtc.hasUniqueId("Owner")) {
					instance.setOwner(nbtc.getUniqueId("Owner"));
				}
			}
		}
		
	}

}
