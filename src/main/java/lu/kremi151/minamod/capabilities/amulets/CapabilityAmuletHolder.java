package lu.kremi151.minamod.capabilities.amulets;

import lu.kremi151.minamod.item.ItemAmulet;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityAmuletHolder implements IAmuletHolder{

	@CapabilityInject(IAmuletHolder.class)
	public static final Capability<IAmuletHolder> CAPABILITY_AMULET_HOLDER = null;
	
	private final NonNullList<ItemStack> inv = NonNullList.withSize(3, ItemStack.EMPTY);

	@Override
	public ItemStack getAmuletAt(int slot) {
		return inv.get(slot);
	}

	@Override
	public boolean setAmuletAt(int slot, ItemStack amulet) {
		if(amulet.isEmpty() || amulet.getItem() instanceof ItemAmulet) {
			inv.set(slot, amulet);
			return true;
		}else {
			return false;
		}
	}
	
	public static final Capability.IStorage<IAmuletHolder> STORAGE = new Capability.IStorage<IAmuletHolder>(){

		@Override
		public NBTBase writeNBT(Capability<IAmuletHolder> capability, IAmuletHolder instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("amuletInv", ItemStackHelper.saveAllItems(new NBTTagCompound(), instance.getAmulets()));
			return nbt;
		}

		@Override
		public void readNBT(Capability<IAmuletHolder> capability, IAmuletHolder instance, EnumFacing side, NBTBase nbt_) {
			if(nbt_ instanceof NBTTagCompound){
				NBTTagCompound nbt = (NBTTagCompound)nbt_;
				try {
					ItemStackHelper.loadAllItems(nbt.getCompoundTag("amuletInv"), instance.getAmulets());
				}catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
		
	};

	@Override
	public int amuletAmount() {
		return 3;
	}

	@Override
	public NonNullList<ItemStack> getAmulets() {
		return inv;
	}

}
