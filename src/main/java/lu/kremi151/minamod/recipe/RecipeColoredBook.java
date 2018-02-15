package lu.kremi151.minamod.recipe;

import com.google.gson.JsonObject;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.item.ItemColoredWrittenBook;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeColoredBook extends RecipeBase.Dynamic{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int foundBook = 0;
		boolean foundDye = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			if(!inv.getStackInSlot(i).isEmpty()){
				if(inv.getStackInSlot(i).getItem() == Items.WRITTEN_BOOK || inv.getStackInSlot(i).getItem() == MinaItems.COLORED_BOOK){
					foundBook++;
				}else if(inv.getStackInSlot(i).getItem() == Items.DYE){
					foundDye = true;
				}else{
					return false;
				}
			}
		}
		return foundDye && foundBook == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack old_book = ItemStack.EMPTY;
		boolean vanillaBook = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			if(!inv.getStackInSlot(i).isEmpty()){
				if(inv.getStackInSlot(i).getItem() == Items.WRITTEN_BOOK || inv.getStackInSlot(i).getItem() == MinaItems.COLORED_BOOK){
					old_book = inv.getStackInSlot(i);
					vanillaBook = (inv.getStackInSlot(i).getItem() == Items.WRITTEN_BOOK);
					break;
				}
			}
		}
		if(old_book.isEmpty()){
			return old_book;
		}
		
		ItemStack res = new ItemStack(MinaItems.COLORED_BOOK);
		if(old_book.hasTagCompound()){
			res.setTagCompound((NBTTagCompound) old_book.getTagCompound().copy());
		}else{
			res.setTagCompound(new NBTTagCompound());
		}
		
		int hcount = 0;
		int reds = 0;
		int greens = 0;
		int blues = 0;
		
		if(!vanillaBook && old_book.hasTagCompound() && old_book.getTagCompound().hasKey("color", 3)){
			hcount = 1;
			int rgb[] = MinaUtils.convertDecimalToRGB(old_book.getTagCompound().getInteger("color"));
			reds = rgb[0];
			greens = rgb[1];
			blues = rgb[2];
		}
		
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack slot = inv.getStackInSlot(i);
			if(!slot.isEmpty() && slot.getItem() == Items.DYE){
				hcount ++;
				int rgb[] = MinaUtils.convertDecimalToRGB(ItemDye.DYE_COLORS[slot.getItemDamage()]);
				reds += rgb[0];
				greens += rgb[1];
				blues += rgb[2];
			}
		}

		int fred = Math.min(reds / hcount, 255);
		int fgreen = Math.min(greens / hcount, 255);
		int fblue = Math.min(blues / hcount, 255);
		
		ItemColoredWrittenBook.setBookColor(res, MinaUtils.convertRGBToDecimal(fred, fgreen, fblue));
		
		return res;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.COLORED_BOOK);
	}

	@Override
	public boolean canFit(int width, int height) {
		return (width * height) >= 2;
	}
	
	public static class Factory implements IRecipeFactory{

		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			return new RecipeColoredBook();
		}
		
	}

}
