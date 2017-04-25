package lu.kremi151.minamod.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public abstract class RecipeBase implements IRecipe{

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		int size = inv.getSizeInventory();
		NonNullList<ItemStack> nnl = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < size; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            nnl.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nnl;
	}

}
