package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends Item implements ISyncCapabilitiesToClient{

	public ItemBattery(){
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(cap != null && cap instanceof BatteryStorage) {
			tooltip.add(I18n.translateToLocal(((BatteryStorage)cap).isRechargeable()?"item.battery.rechargeable":"item.battery.not_rechargeable"));
			tooltip.add(I18n.translateToLocalFormatted("item.battery.charge", chargePercentage(stack) * 100.0));
		}
    }
	
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new EnergyProvider(stack);
    }
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
		return true;
    }
	
	private double chargePercentage(ItemStack stack) {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
        if(cap != null) {
        	return (double)cap.getEnergyStored() / (double)cap.getMaxEnergyStored();
        }else {
        	return 0.0;
        }
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		return 1.0 - chargePercentage(stack);
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(cap != null) {
			nbt.setBoolean("Accu", ((BatteryStorage)cap).isRechargeable());
			nbt.setInteger("Cap", cap.getEnergyStored());
			nbt.setInteger("Max", cap.getMaxEnergyStored());
		}
		return nbt;
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		IEnergyStorage cap = (IEnergyStorage) stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(cap != null && cap instanceof BatteryStorage) {
			if(nbt.hasKey("Cap", 99) && nbt.hasKey("Max", 99) && nbt.hasKey("Accu", 99)) {
				((BatteryStorage)cap).setRechargeable(nbt.getBoolean("Accu"));
				((BatteryStorage)cap).setCapacity(nbt.getInteger("Cap"));
				((BatteryStorage)cap).setMaxCapacity(nbt.getInteger("Max"));
			}
		}
	}
	
	public ItemStack createNotRechargeable(int maxCapacity) {
		ItemStack res = new ItemStack(this);
		((BatteryStorage)res.getCapability(CapabilityEnergy.ENERGY, null))
				.setRechargeable(false)
				.setMaxCapacity(maxCapacity)
				.setCapacity(maxCapacity);
		return res;
	}
	
	private static class EnergyProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private final BatteryStorage cap;
		
		private EnergyProvider(ItemStack stack){
			this.cap = new BatteryStorage(true, 2000);//TODO: Adjust
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
			nbt.setBoolean("Rechargeable", cap.isRechargeable());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			cap.setMaxCapacity(Math.max(1, nbt.getInteger("MaxCapacity")));
			cap.setCapacity(nbt.getInteger("Capacity"));
			cap.setRechargeable(nbt.getBoolean("Rechargeable"));
		}
		
	}
	
	private static class BatteryStorage implements IEnergyStorage{
		
		private int capacity = 0;
		private int maxCapacity;
		private boolean rechargeable;
		
		private BatteryStorage(boolean rechargeable, int maxCapacity){
			this.rechargeable = rechargeable;
			this.capacity = maxCapacity;
			this.maxCapacity = maxCapacity;
		}
		
		private int getMaxCapacity(){
			return maxCapacity;
		}
		
		private int getCapacity(){
			return capacity;
		}
		
		private BatteryStorage setCapacity(int cap){
			if(cap < 0){
				this.capacity = 0;
			}else if(cap > maxCapacity){
				this.capacity = maxCapacity;
			}else {
				this.capacity = cap;
			}
			return this;
		}
		
		private BatteryStorage setMaxCapacity(int max) {
			if(max > 0) {
				this.maxCapacity = max;
			}else {
				throw new IllegalArgumentException("Maximal capacity should be above zero");
			}
			return this;
		}
		
		private boolean isRechargeable() {
			return rechargeable;
		}
		
		private BatteryStorage setRechargeable(boolean v) {
			this.rechargeable = v;
			return this;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			if(rechargeable) {
				int emptyness = getMaxCapacity() - getCapacity();
				int accept = (maxReceive <= emptyness)?maxReceive:emptyness;
				if(!simulate){
					setCapacity(getCapacity() + accept);
				}
				return accept;
			}else {
				return 0;
			}
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
