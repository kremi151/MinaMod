package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemKey.State;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeCopyKey implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean raw_key = false, key = false;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.KEY){
				ItemKey.State state = ItemKey.getState(is);
				switch(state){
				case RAW:
					if(raw_key){
						return false;
					}else{
						raw_key = true;
					}
					break;
				case UNKNOWN:
				case DUPLICATE:
					return false;
				case NORMAL:
					if(key){
						return false;
					}else{
						key = true;
					}
					break;
				}
			}
		}
		return raw_key && key;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack raw_key = ItemStack.EMPTY, key = ItemStack.EMPTY;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.KEY){
				ItemKey.State state = ItemKey.getState(is);
				if(state == ItemKey.State.RAW){
					raw_key = is;
				}else{
					key = is;
				}
			}
		}
		if(raw_key.isEmpty() || key.isEmpty()){
			return ItemStack.EMPTY;
		}
		ItemStack res = raw_key.copy();
		ItemKey.KeyData capRes = ItemKey.getData(res);
		ItemKey.KeyData capSrc = ItemKey.getData(key);
		
		capRes.apply(capSrc);
		capRes.setState(State.DUPLICATE);
		
		return res;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.KEY, 9);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> aitemstack = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.KEY){
				if(ItemKey.getState(is) == ItemKey.State.NORMAL){
					aitemstack.set(i, is.copy());
					break;
				}
			}
		}
		return aitemstack;
	}

}
