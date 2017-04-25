package lu.kremi151.minamod.recipe;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.item.block.ItemBlockGiftBox;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeGiftBox extends RecipeBase{
	
	private final Item compGB, compC;
	
	public RecipeGiftBox(){
		compGB = Item.getItemFromBlock(MinaBlocks.GIFT_BOX);
		compC = Item.getItemFromBlock(Blocks.CARPET);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return checkWools(inv) && !inv.getStackInRowAndColumn(1, 1).isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack item = inv.getStackInRowAndColumn(1, 1);
		int meta = inv.getStackInSlot(1).getMetadata();
		if(!item.isEmpty()){
			ItemStack gift = new ItemStack(MinaBlocks.GIFT_BOX, 1, meta);
			NBTTagCompound inbt = new NBTTagCompound();
			item.writeToNBT(inbt);
			gift.setTagInfo(ItemBlockGiftBox.GIFT_ITEM_TAG, inbt);
			return gift;
		}else{
			return getRecipeOutput();
		}
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MinaBlocks.GIFT_BOX, 1);
	}
	
	private boolean checkWools(IInventory inv){
		if(inv.getStackInSlot(0).isEmpty() && inv.getStackInSlot(2).isEmpty() && inv.getStackInSlot(6).isEmpty() && inv.getStackInSlot(8).isEmpty()){
			ItemStack s1 = inv.getStackInSlot(1);
			ItemStack s2 = inv.getStackInSlot(3);
			ItemStack s3 = inv.getStackInSlot(5);
			ItemStack s4 = inv.getStackInSlot(7);
			return !s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty()
					&& s1.getItem() == compC && s2.getItem() == compC && s3.getItem() == compC && s4.getItem() == compC
					&& s1.getMetadata() == s2.getMetadata() && s2.getMetadata() == s3.getMetadata() && s3.getMetadata() == s4.getMetadata();
		}else{
			return false;
		}
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {//leave it like that please
		return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}
}
