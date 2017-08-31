package lu.kremi151.minamod.item;

import javax.annotation.Nullable;

import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.interfaces.ISyncCapabilitiesToClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class ItemSketch extends Item implements ISyncCapabilitiesToClient{

	public ItemSketch() {
		this.setMaxStackSize(1);
	}
	
	@Override
	public final net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new SketchCapabilityProvider();
    }

	@Override
	public NBTTagCompound writeSyncableData(ItemStack stack, NBTTagCompound nbt) {
		return serializeCapability(stack.getCapability(ISketch.CAPABILITY, null), nbt);
	}

	@Override
	public void readSyncableData(ItemStack stack, NBTTagCompound nbt) {
		deserializeCapability(stack.getCapability(ISketch.CAPABILITY, null), nbt);
	}
	
	private static NBTTagCompound serializeCapability(ISketch cap, NBTTagCompound nbt) {
		NBTTagList order = new NBTTagList();
		for(int i = 0 ; i < cap.getOrder().size() ; i++) {
			ItemStack stack = cap.getOrder().get(i);
			NBTTagCompound inbt = new NBTTagCompound();
			
			ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(stack.getItem());
			inbt.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
			inbt.setByte("Count", (byte)1);
			inbt.setShort("Damage", (short)stack.getItemDamage());
	        
	        order.appendTag(inbt);
		}
		nbt.setTag("Order", order);
		return nbt;
	}
	
	private static void deserializeCapability(ISketch cap, NBTTagCompound nbt) {
		NBTTagList order = nbt.getTagList("Order", 10);
		NonNullList list = NonNullList.withSize(9, ItemStack.EMPTY);
		for(int i = 0 ; i < order.tagCount() ; i++) {
			list.set(i, new ItemStack(order.getCompoundTagAt(i)));
		}
		cap.setCachedRecipe(null);
		cap.setOrder(list);
	}
	
	private static class SketchCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>{
		
		private static final SketchImpl cap = new SketchImpl();

		@Override
		public NBTTagCompound serializeNBT() {
			return serializeCapability(cap, new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			deserializeCapability(cap, nbt);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ISketch.CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return (T) ((capability == ISketch.CAPABILITY) ? cap : null);
		}
		
	}
	
	private static class SketchImpl implements ISketch{
		
		private IRecipe recipe;
		private NonNullList<ItemStack> order = NonNullList.withSize(9, ItemStack.EMPTY);

		@Override
		public IRecipe getCachedRecipe() {
			return recipe;
		}

		@Override
		public NonNullList<ItemStack> getOrder() {
			return order;
		}

		@Override
		public void setCachedRecipe(IRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void setOrder(NonNullList<ItemStack> order) {
			if(order.size() == 0 || order.size() > 9) {
				throw new IllegalArgumentException("Invalid size");
			}
			this.order = order;
			this.recipe = null;
		}
		
	}
	
}
