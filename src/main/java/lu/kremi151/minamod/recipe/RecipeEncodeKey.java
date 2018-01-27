package lu.kremi151.minamod.recipe;

import java.util.UUID;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemKey.State;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipeEncodeKey extends RecipeBase.Dynamic{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int raw_keys = 0;
		for(int i = 0 ; i < inv.getSizeInventory() ; i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem() == MinaItems.KEY){
					if(ItemKey.getState(stack) == ItemKey.State.RAW){
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
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == MinaItems.KEY){
				if(ItemKey.getState(stack) == ItemKey.State.RAW){
					res = stack.copy();
					break;
				}
			}
		}
		if(res.isEmpty())return res;
		ItemKey.ExtendedKeyCapability cap = (ItemKey.ExtendedKeyCapability)res.getCapability(IKey.CAPABILITY_KEY, null);
		cap.registerUnlockable(UUID.randomUUID());
		cap.setState(State.NORMAL);
		return res;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaItems.KEY, 1);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width > 0 && height > 0;
	}

}
