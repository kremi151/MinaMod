package lu.kremi151.minamod.recipe.combiner;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class CombinerRecipeManager {

	private static final LinkedList<ICombinerRecipe> REGISTRY = new LinkedList<>();
	
	public static void registerRecipe(ItemStack output, Ingredient input[]) {
		REGISTRY.add(new CombinerRecipeDefault(output, input));
	}
	
	public static void registerRecipe(ICombinerRecipe recipe) {
		REGISTRY.add(recipe);
	}
	
	public static ItemStack getResult(NonNullList<ItemStack> input) {
		for(ICombinerRecipe recipe : REGISTRY) {
			if(recipe.matches(input)) {
				return recipe.getOutput().copy();
			}
		}
		return ItemStack.EMPTY;
	}
	
	public static Collection<ICombinerRecipe> getRecipes(){
		return Collections.unmodifiableCollection(REGISTRY);
	}
}
