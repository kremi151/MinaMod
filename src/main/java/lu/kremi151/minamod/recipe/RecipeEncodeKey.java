package lu.kremi151.minamod.recipe;

import java.util.UUID;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemKey.State;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipeEncodeKey extends RecipeBase{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int raw_keys = 0;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(!is.isEmpty()){
				if(is.getItem() == MinaItems.KEY){
					if(ItemKey.getState(is) == ItemKey.State.RAW){
						raw_keys++;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
		}
		return raw_keys == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack res = ItemStack.EMPTY;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack is = inv.getStackInSlot(i);
			if(is.getItem() == MinaItems.KEY){
				if(ItemKey.getState(is) == ItemKey.State.RAW){
					res = is.copy();
					break;
				}
			}
		}
		if(res.isEmpty())return res;
		ItemKey.MinaKeyCapability cap = ItemKey.getData(res);
		cap.registerUnlockable(UUID.randomUUID());
		cap.setState(State.NORMAL);
		return res;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.KEY, 1);
	}

}
