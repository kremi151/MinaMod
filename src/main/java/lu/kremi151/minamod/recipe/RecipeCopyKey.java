package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.IKey;
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
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == MinaItems.KEY){
				ItemKey.State state = ItemKey.getState(stack);
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
				case MASTER:
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
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == MinaItems.KEY){
				ItemKey.State state = ItemKey.getState(stack);
				if(state == ItemKey.State.RAW){
					raw_key = stack;
				}else{
					key = stack;
				}
			}
		}
		if(raw_key.isEmpty() || key.isEmpty()){
			return ItemStack.EMPTY;
		}
		ItemStack res = raw_key.copy();
		ItemKey.ExtendedKeyCapability capRes = ((ItemKey.ExtendedKeyCapability)res.getCapability(IKey.CAPABILITY_KEY, null));
		ItemKey.ExtendedKeyCapability capSrc = ((ItemKey.ExtendedKeyCapability)key.getCapability(IKey.CAPABILITY_KEY, null));
		
		capRes.applyFrom(capSrc);
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
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == MinaItems.KEY){
				if(ItemKey.getState(stack) == ItemKey.State.NORMAL){
					aitemstack.set(i, stack.copy());
					break;
				}
			}
		}
		return aitemstack;
	}

}
