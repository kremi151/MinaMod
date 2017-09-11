package lu.kremi151.minamod.util;

import java.util.Iterator;
import java.util.LinkedList;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public final class CombinerRecipes {
	
	private final static LinkedList<ICombinerRecipe> RECIPES = new LinkedList<>();
	
	static {
		RECIPES.add(new CombinerRecipe(new ItemStack(Items.NETHER_STAR), MinaItems.STRAWBERRY, MinaItems.CITRIN));
	}
	
	public static ItemStack tryCombine(NonNullList<ItemStack> input) {
		for(ICombinerRecipe recipe : RECIPES) {
			ItemStack result = recipe.getResult(input);
			if(!result.isEmpty()) {
				return result;
			}
		}
		return ItemStack.EMPTY;
	}
	
	public static interface ICombinerRecipe{
		ItemStack getResult(NonNullList<ItemStack> input);
	}
	
	private static class CombinerRecipe implements ICombinerRecipe{
		private final Object ingredients[];
		private final ItemStack result;
		
		private CombinerRecipe(ItemStack result, Object... ingredients) {
			this.result = result;
			this.ingredients = ingredients;
		}

		@Override
		public ItemStack getResult(NonNullList<ItemStack> input) {
			NonNullList<ItemStack> input_copy = NonNullList.create();
			boolean found = false;
			input_copy.addAll(input);
			Iterator<ItemStack> it = input_copy.iterator();
			while(it.hasNext()) {
				ItemStack inputStack = it.next();
				if(inputStack.isEmpty()) {
					it.remove();
				}else {
					secondloop:
					for(Object obj : this.ingredients) {
						if(obj instanceof String) {
							NonNullList<ItemStack> ores = OreDictionary.getOres((String)obj, false);
							if(ores.size() == 0) {
								throw new RuntimeException("Unknown ore name found: " + obj);
							}
							for(ItemStack ore : ores) {
								if(ore.isItemEqual(inputStack)) {
									it.remove();
									found = true;
									break secondloop;
								}
							}
						}else if(obj instanceof Block) {
							ItemStack blockStack = new ItemStack((Block)obj);
							if(blockStack.isItemEqual(inputStack)) {
								it.remove();
								found = true;
								break secondloop;
							}
						}else if(obj instanceof Item) {
							ItemStack itemStack = new ItemStack((Item)obj);
							if(itemStack.isItemEqual(inputStack)) {
								it.remove();
								found = true;
								break secondloop;
							}
						}else if(obj instanceof ItemStack) {
							if(((ItemStack)obj).isItemEqual(inputStack)) {
								it.remove();
								found = true;
								break secondloop;
							}
						}
					}
				}
			}
			if(found && input_copy.size() == 0) {
				return result.copy();
			}else {
				return ItemStack.EMPTY;
			}
		}
		
		
	}

}
