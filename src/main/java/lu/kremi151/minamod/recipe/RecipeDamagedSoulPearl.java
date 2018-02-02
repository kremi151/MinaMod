package lu.kremi151.minamod.recipe;

import com.google.gson.JsonObject;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.item.ItemSoulPearl;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeDamagedSoulPearl extends RecipeBase.Dynamic{//TODO: Maybe replace with RecipeRepairItem

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int found = 0;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.SOUL_PEARL){
				if(!ItemSoulPearl.checkIfFull(is) && found < 2){
					found++;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return found == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int dam = 0;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.SOUL_PEARL){
				dam += (MinaItems.SOUL_PEARL.getMaxDamage() - is.getItemDamage());
			}
		}
		ItemStack r = new ItemStack(MinaItems.SOUL_PEARL, 1);
		r.setItemDamage(Math.max(0, MinaItems.SOUL_PEARL.getMaxDamage() - dam));
		return r;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.SOUL_PEARL, 1);
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) >= 2;
	}
	
	public static class Factory implements IRecipeFactory{

		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			return new RecipeDamagedSoulPearl();
		}
		
	}

}
