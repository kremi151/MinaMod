package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipeFixDrill extends RecipeBase.Dynamic{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean drill = false, bit = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() == MinaItems.DRILL && stack.getItemDamage() > 0 && !drill) {
					drill = true;
				}else if(stack.getItem() == Items.IRON_NUGGET && !bit) {
					bit = true;
				}else {
					return false;
				}
			}
		}
		return drill && bit;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack drill = null;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == MinaItems.DRILL) {
				drill = stack.copy();
				break;
			}
		}
		if(drill != null) {
			drill.setItemDamage(0);
			return drill;
		}else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.DRILL);
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) >= 2;
	}

}
