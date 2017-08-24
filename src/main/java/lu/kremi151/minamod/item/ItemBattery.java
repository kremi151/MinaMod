package lu.kremi151.minamod.item;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBattery extends Item implements ISyncCapabilitiesToClient{

	public ItemBattery(){
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new EnergyProvider(stack);
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
        if(cap != null) {
        	return 1.0 - ((double)cap.getEnergyStored() / (double)cap.getMaxEnergyStored());
        }else {
        	return 1.0;
        }
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(cap != null) {
			nbt.setInteger("Cap", cap.getEnergyStored());
			nbt.setInteger("Max", cap.getMaxEnergyStored());
		}
		return nbt;
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(cap != null && cap instanceof BatteryStorage) {
			if(nbt.hasKey("Cap", 99) && nbt.hasKey("Max", 99)) {
				((BatteryStorage)cap).setCapacity(nbt.getInteger("Cap"));
				((BatteryStorage)cap).setMaxCapacity(nbt.getInteger("Max"));
			}
		}
	}
	
	private static class EnergyProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private final BatteryStorage cap;
		
		private EnergyProvider(ItemStack stack){
			this.cap = new BatteryStorage(2000);//TODO: Adjust
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityEnergy.ENERGY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return (capability == CapabilityEnergy.ENERGY)?((T) cap):null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("Capacity", cap.getCapacity());
			nbt.setInteger("MaxCapacity", cap.getMaxCapacity());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			cap.setCapacity(nbt.getInteger("Capacity"));
			cap.setMaxCapacity(Math.max(1, nbt.getInteger("MaxCapacity")));
		}
		
	}
	
	private static class BatteryStorage implements IEnergyStorage{
		
		private int capacity = 0;
		private int maxCapacity;
		
		private BatteryStorage(int maxCapacity){
			this.maxCapacity = maxCapacity;
		}
		
		private int getMaxCapacity(){
			return maxCapacity;
		}
		
		private int getCapacity(){
			return capacity;
		}
		
		private void setCapacity(int cap){
			if(cap < 0){
				this.capacity = 0;
			}else if(cap > maxCapacity){
				this.capacity = maxCapacity;
			}else {
				this.capacity = cap;
			}
		}
		
		private void setMaxCapacity(int max) {
			if(max > 0) {
				this.maxCapacity = max;
			}else {
				throw new IllegalArgumentException("Maximal capacity should be above zero");
			}
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			short emptyness = (short) (getMaxCapacity() - getCapacity());
			int accept = (maxReceive <= emptyness)?maxReceive:emptyness;
			if(!simulate){
				setCapacity(getCapacity() + accept);
			}
			return accept;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			if(maxExtract < 0)return 0;
			int extract = (maxExtract <= getCapacity())?maxExtract:getCapacity();
			if(!simulate){
				setCapacity(getCapacity() - extract);
			}
			return extract;
		}

		@Override
		public int getEnergyStored() {
			return getCapacity();
		}

		@Override
		public int getMaxEnergyStored() {
			return getMaxCapacity();
		}

		@Override
		public boolean canExtract() {
			return getCapacity() > 0;
		}

		@Override
		public boolean canReceive() {
			return getCapacity() < getMaxCapacity();
		}
		
	}
}
