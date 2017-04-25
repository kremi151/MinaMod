package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeColoredBookCloning implements IRecipe
{
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
	public boolean matches(InventoryCrafting inv, World worldIn)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = inv.getStackInSlot(j);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() == MinaItems.COLORED_BOOK)
                {
                    if (!itemstack.isEmpty())
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK)
                    {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.isEmpty() && i > 0;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); ++j)
        {
            ItemStack itemstack1 = inv.getStackInSlot(j);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() == MinaItems.COLORED_BOOK)
                {
                    if (!itemstack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK)
                    {
                        return ItemStack.EMPTY;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.isEmpty() && i >= 1 && ItemWrittenBook.getGeneration(itemstack) < 2)
        {
            ItemStack itemstack2 = new ItemStack(MinaItems.COLORED_BOOK, i);
            itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
            itemstack2.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(itemstack) + 1);

            if (itemstack.hasDisplayName())
            {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
            }

            return itemstack2;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize()
    {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(MinaItems.COLORED_BOOK);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
    	int l = inv.getSizeInventory();
    	NonNullList<ItemStack> nnl = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < l; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (itemstack.getItem() instanceof ItemWrittenBook)
            {
                ItemStack is = itemstack.copy();
                is.setCount(1);
                nnl.set(i, is.copy());
                break;
            }
        }

        return nnl;
    }

}
