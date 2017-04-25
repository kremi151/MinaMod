package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

//TODO: Fix & remove
/**
 * Temporary fix for the default herb guide recipe bug. To be removed as soon as fixed.
 * @author michm
 *
 */
public class TMPRecipeHerbGuide extends RecipeBase{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean book = false;
		boolean herb = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack stack = inv.getStackInSlot(0);
			if(!stack.isEmpty()){
				if(stack.getItem() == MinaItems.HERB){
					if(herb){
						return false;
					}else{
						herb = true;
					}
				}else if(stack.getItem() == Items.WRITABLE_BOOK){
					if(book){
						return false;
					}else{
						book = true;
					}
				}
			}
		}
		return book && herb;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return getRecipeOutput();
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.HERB_GUIDE, 1);
	}

}
