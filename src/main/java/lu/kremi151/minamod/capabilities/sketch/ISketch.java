package lu.kremi151.minamod.capabilities.sketch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISketch {
	
	@CapabilityInject(ISketch.class)
	public static final Capability<ISketch> CAPABILITY = null;

	@Nullable IRecipe getCachedRecipe();
	void setCachedRecipe(@Nullable IRecipe recipe);
	NonNullList<ItemStack> getOrder();
	void setOrder(@Nonnull NonNullList<ItemStack> order);
	
}
