package lu.kremi151.minamod.recipe.combiner;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class CombinerRecipeDefault implements ICombinerRecipe{
	
	private final ItemStack output;
	private final NonNullList<Ingredient> input;
	
	public CombinerRecipeDefault(ItemStack output, Ingredient input[]) {
		if(input.length > INPUT_SIZE) {
			throw new IllegalArgumentException("Input size must not be greather than " + INPUT_SIZE);
		}
		this.output = output;
		this.input = NonNullList.from(Ingredient.EMPTY, input);
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public boolean matches(NonNullList<ItemStack> input) {
		boolean match[] = new boolean[input.size()];
		for(int i = 0 ; i < input.size() ; i++)match[i] = input.get(i).isEmpty();
		for(int i = 0 ; i < this.input.size() ; i++) {
			boolean found = false;
			for(int j = 0 ; j < input.size() ; j++) {
				if(!match[j] && this.input.get(i).apply(input.get(j))) {
					match[j] = true;
					found = true;
					break;
				}
			}
			if(!found)return false;
		}
		for(int i = 0 ; i < match.length ; i++) {
			if(!match[i])return false;
		}
		return true;
	}

	@Override
	public boolean isDynamic() {
		return false;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients(){
		return input;
	}

}
