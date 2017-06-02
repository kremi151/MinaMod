package lu.kremi151.minamod.capabilities.bliss;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ICapabilityBliss {

	@CapabilityInject(ICapabilityBliss.class)
	public static final Capability<ICapabilityBliss> CAPABILITY_BLISS = null;

	boolean chanceOutOf(int n, int total);
	
	default boolean chanceOneIn(int total){
		return chanceOutOf(1, total);
	}
	
	void incrementBliss(int amount);
	void decrementBliss(int amount);
	int getBliss();
	void setBliss(int bliss) throws IllegalArgumentException;
	

	public static final Capability.IStorage<ICapabilityBliss> STORAGE = new Capability.IStorage<ICapabilityBliss>(){

		@Override
		public NBTBase writeNBT(Capability<ICapabilityBliss> capability, ICapabilityBliss instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("bliss", instance.getBliss());
			return nbt;
		}

		@Override
		public void readNBT(Capability<ICapabilityBliss> capability, ICapabilityBliss instance, EnumFacing side, NBTBase nbt) {
			if(nbt instanceof NBTTagCompound){
				int bliss = ((NBTTagCompound)nbt).getInteger("bliss");
				if(bliss <= 0)bliss = 1;
				instance.setBliss(bliss);
			}
		}
		
	};
}
