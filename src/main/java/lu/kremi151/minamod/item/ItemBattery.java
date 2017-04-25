package lu.kremi151.minamod.item;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaCreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBattery extends Item{

	public ItemBattery(){
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
		this.setMaxDamage(500);
	}
	
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new EnergyProvider(stack);
    }
	
	private static class EnergyProvider implements ICapabilityProvider{
		
		private final BatteryStorage cap;
		
		private EnergyProvider(ItemStack stack){
			this.cap = new BatteryStorage(stack);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityEnergy.ENERGY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return (capability == CapabilityEnergy.ENERGY)?((T) cap):null;
		}
		
	}
	
	private static class BatteryStorage implements IEnergyStorage{
		
		private ItemStack stack;
		
		private BatteryStorage(ItemStack stack){
			this.stack = stack;
		}
		
		private short getMaxCapacity(){
			return (short) stack.getMaxDamage();
		}
		
		private short getCapacity(){
			return (short) (getMaxCapacity() - stack.getItemDamage());
		}
		
		private void setCapacity(int cap){
			if(cap < 0){
				cap = 0;
			}else if(cap > getMaxCapacity()){
				cap = getMaxCapacity();
			}
			
			stack.setItemDamage(getMaxCapacity() - cap);
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
