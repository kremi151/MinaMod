package lu.kremi151.minamod.recipe;

import com.google.gson.JsonObject;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeColoredKey extends RecipeBase.Dynamic{

	@Override
	public boolean matches(InventoryCrafting inv, World w) {
		int foundKey = 0;
		boolean foundDye = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			if(!inv.getStackInSlot(i).isEmpty()){
				if(inv.getStackInSlot(i).getItem() == MinaItems.KEY){
					foundKey++;
				}else if(inv.getStackInSlot(i).getItem() == Items.DYE){
					foundDye = true;
				}else{
					return false;
				}
			}
		}
		return foundDye && foundKey == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack old_key = ItemStack.EMPTY;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			if(inv.getStackInSlot(i).getItem() == MinaItems.KEY){
				old_key = inv.getStackInSlot(i);
				break;
			}
		}
		if(old_key.isEmpty()){
			return old_key;
		}
		ItemStack res = new ItemStack(MinaItems.KEY, 1);
		
		int hcount = 0;
		int reds = 0;
		int greens = 0;
		int blues = 0;
		
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack slot = inv.getStackInSlot(i);
			if(!slot.isEmpty()){
				if(slot.getItem() == Items.DYE){
					
					hcount ++;
					int rgb[] = MinaUtils.convertDecimalToRGB(ItemDye.DYE_COLORS[slot.getItemDamage()]);
					reds += rgb[0];
					greens += rgb[1];
					blues += rgb[2];
				}
			}
		}

		int fred = Math.min(reds / hcount, 255);
		int fgreen = Math.min(greens / hcount, 255);
		int fblue = Math.min(blues / hcount, 255);
		
		ItemKey.ExtendedKeyCapability cap = (ItemKey.ExtendedKeyCapability)res.getCapability(IKey.CAPABILITY_KEY, null);
		cap.applyFrom((ItemKey.ExtendedKeyCapability)old_key.getCapability(IKey.CAPABILITY_KEY, null));
		cap.setTint(MinaUtils.convertRGBToDecimal(fred, fgreen, fblue));
		
		return res;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.KEY, 1);
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) >= 2;
	}
	
	public static class Factory implements IRecipeFactory{

		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			return new RecipeColoredKey();
		}
		
	}

}
