package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public final class CombinerRecipes {
	
	private final static LinkedList<ShapelessRecipes> RECIPES = new LinkedList<>();
	
	static {
		registerRecipe(new ItemStack(MinaItems.STRAWBERRY, 1, 1), MinaItems.STRAWBERRY, MinaItems.RUBY);
		registerRecipe(new ItemStack(MinaItems.STRAWBERRY, 1, 2), MinaItems.STRAWBERRY, MinaItems.SAPPHIRE);
		registerRecipe(new ItemStack(MinaItems.STRAWBERRY, 1, 3), MinaItems.STRAWBERRY, MinaItems.CITRIN);
	}
	
	public static ItemStack tryCombine(InventoryCrafting inv, World world) {
		for(ShapelessRecipes recipe : RECIPES) {
			if(recipe.matches(inv, world)) {
				return recipe.getCraftingResult(inv);
			}
		}
		return ItemStack.EMPTY;
	}
	
	public static void registerRecipe(ItemStack output, Object... recipeComponents) {
		if(recipeComponents.length == 0 || recipeComponents.length > 3) {
			throw new IllegalArgumentException("Invalid size of recipe ingredients, should be between 1 and 3 inclusive, was " + recipeComponents.length);
		}
		List<ItemStack> list = new ArrayList<>(recipeComponents.length);

        for (Object object : recipeComponents)
        {
            if (object instanceof ItemStack)
            {
                list.add(((ItemStack)object).copy());
            }
            else if (object instanceof Item)
            {
                list.add(new ItemStack((Item)object));
            }
            else
            {
                if (!(object instanceof Block))
                {
                    throw new IllegalArgumentException("Invalid combiner recipe: unknown type " + object.getClass().getName() + "!");
                }

                list.add(new ItemStack((Block)object));
            }
        }

        //TODO: RECIPES.add(new ShapelessRecipes(output, list));
	}

}
