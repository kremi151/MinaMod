package lu.kremi151.minamod.util;

import java.util.Arrays;
import java.util.LinkedList;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public final class CombinerRecipes {
	
	private final static LinkedList<ShapelessRecipes> RECIPES = new LinkedList<>();
	
	static {
		RECIPES.add(new ShapelessRecipes(new ItemStack(Items.NETHER_STAR), Arrays.asList(new ItemStack(MinaItems.STRAWBERRY, 1, 0), new ItemStack(MinaItems.CITRIN))));
	}
	
	public static ItemStack tryCombine(InventoryCrafting inv, World world) {
		for(ShapelessRecipes recipe : RECIPES) {
			if(recipe.matches(inv, world)) {
				return recipe.getCraftingResult(inv);
			}
		}
		return ItemStack.EMPTY;
	}

}
