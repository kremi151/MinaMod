package lu.kremi151.minamod.recipe;

import java.util.ArrayList;
import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.item.block.ItemBlockGiftBox;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeAddToGiftBox extends RecipeBase{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		final Item comp = Item.getItemFromBlock(MinaBlocks.GIFT_BOX);
		List<ItemStack> nes = MinaUtils.getNonEmptyStacks(inv);
		final int l = nes.size();
		if(l == 2){
			ItemStack stacks[] = new ItemStack[]{nes.get(0), nes.get(1)};
			if(stacks[1].getItem() == comp){
				ItemStack tmp = stacks[0];
				stacks[0] = stacks[1];
				stacks[1] = tmp;
			}else if(stacks[0].getItem() != comp){
				return false;
			}
			NBTTagCompound inbt = stacks[0].getSubCompound(ItemBlockGiftBox.GIFT_ITEM_TAG);
			if(inbt != null){
				ItemStack gift = new ItemStack(inbt);
				return MinaUtils.areItemsStackable(gift, stacks[1], true);
			}else{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		final Item comp = Item.getItemFromBlock(MinaBlocks.GIFT_BOX);
		List<ItemStack> nes = MinaUtils.getNonEmptyStacks(inv);
		final int l = nes.size();
		ItemStack stacks[] = new ItemStack[]{nes.get(0), nes.get(1)};
		if(stacks[1].getItem() == comp){
			ItemStack tmp = stacks[0];
			stacks[0] = stacks[1];
			stacks[1] = tmp;
		}
		ItemStack result = stacks[0].copy();
		NBTTagCompound inbt = result.getSubCompound(ItemBlockGiftBox.GIFT_ITEM_TAG);
		if(inbt != null){
			ItemStack gift = new ItemStack(inbt);
			gift.grow(1);
			gift.writeToNBT(inbt);
		}else{
			inbt = result.getOrCreateSubCompound(ItemBlockGiftBox.GIFT_ITEM_TAG);
			ItemStack giftitem = stacks[1].copy();
			giftitem.setCount(1);
			giftitem.writeToNBT(inbt);
		}
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaBlocks.GIFT_BOX);
	}

}
