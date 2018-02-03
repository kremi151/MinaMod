package lu.kremi151.minamod.recipe.combiner;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface ICombinerRecipe {
	
	int INPUT_SIZE = 3;

	ItemStack getOutput();
	boolean matches(NonNullList<ItemStack> input);
	boolean isDynamic();
	
	default NonNullList<Ingredient> getIngredients(){
		return NonNullList.from(Ingredient.EMPTY);
	}
	
}
