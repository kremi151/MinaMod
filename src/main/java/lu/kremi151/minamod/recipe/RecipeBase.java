package lu.kremi151.minamod.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class RecipeBase extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		int size = inv.getSizeInventory();
		NonNullList<ItemStack> nnl = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < size; ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			nnl.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}

		return nnl;
	}

	public static abstract class Dynamic extends RecipeBase {

		@Override
		public boolean isDynamic() {
			return true;
		}
	}

}
