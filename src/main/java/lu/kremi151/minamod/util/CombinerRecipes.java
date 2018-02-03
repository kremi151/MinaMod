package lu.kremi151.minamod.util;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.recipe.combiner.CombinerRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public final class CombinerRecipes {
	
	static {
		CombinerRecipeManager.registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 1),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.RUBY))
				});
		CombinerRecipeManager.registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 2),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.SAPPHIRE))
				});
		CombinerRecipeManager.registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 3),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.CITRIN))
				});
	}
	
	public static ItemStack tryCombine(InventoryCrafting inv, World world) {
		NonNullList<ItemStack> input = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0 ; i < inv.getSizeInventory() ; i++) {
			input.set(i, inv.getStackInSlot(i));
		}
		return CombinerRecipeManager.getResult(input);
	}

}
