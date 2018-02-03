package lu.kremi151.minamod.recipe.combiner;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class CombinerRecipeManager {

	private static final LinkedList<ICombinerRecipe> REGISTRY = new LinkedList<>();
	
	static {
		registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 1),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.RUBY))
				});
		registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 2),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.SAPPHIRE))
				});
		registerRecipe(
				new ItemStack(MinaItems.STRAWBERRY, 1, 3),
				new Ingredient[] {
						Ingredient.fromStacks(new ItemStack(MinaItems.STRAWBERRY)),
						Ingredient.fromStacks(new ItemStack(MinaItems.CITRIN))
				});
	}
	
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
	
	public static ItemStack getResult(InventoryCrafting inv) {
		NonNullList<ItemStack> input = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0 ; i < inv.getSizeInventory() ; i++) {
			input.set(i, inv.getStackInSlot(i));
		}
		return getResult(input);
	}
	
	public static Collection<ICombinerRecipe> getRecipes(){
		return Collections.unmodifiableCollection(REGISTRY);
	}
}
