package lu.kremi151.minamod.item;

import lu.kremi151.minamod.interfaces.IEconomyValuable;
import lu.kremi151.minamod.interfaces.IUnitEconomyValuable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGoldenCoin extends Item implements IUnitEconomyValuable{

	public ItemGoldenCoin() {
		this.setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + (stack.getMetadata() == 1 ? ".five" : ".one");
    }
	
	public void regroupToCoins(NonNullList<ItemStack> coinStacks, int amount) {
		int fiveUnits = amount / 5;
		int oneUnits = amount % 5;
		while(fiveUnits > 0) {
			ItemStack coinStack = new ItemStack(this, Math.min(fiveUnits, this.getItemStackLimit()), 1);
			fiveUnits -= coinStack.getCount();
			coinStacks.add(coinStack);
		}
		coinStacks.add(new ItemStack(this, oneUnits, 0));
	}
	
	public int getUnitCoinValue(ItemStack stack) {
		if(stack.isEmpty() || stack.getItem() != this) {
			throw new IllegalArgumentException();
		}
		switch(stack.getMetadata()) {
		case 1:
			return 5;
		default:
			return 1;
		}
	}
	
	public int getCoinValue(ItemStack stack) {
		return getUnitCoinValue(stack) * stack.getCount();
	}

	@Override
	public int getEconomyValue(ItemStack stack) {
		return getCoinValue(stack);
	}

	@Override
	public int getUnitEconomyValue(ItemStack stack) {
		return getUnitCoinValue(stack);
	}

	@Override
	public Result shrinkStackValue(ItemStack stack, int amount, boolean simulate) {
		final int unit = getUnitCoinValue(stack);
		final int leftover = amount % unit;
		final int count = amount / unit;
		if(stack.getCount() > count) {
			final int newCount = stack.getCount() - count;
			if(!simulate)stack.shrink(count);
			return new Result(leftover, newCount * unit);
		}else {
			int left = count - stack.getCount();
			if(!simulate)stack.setCount(0);
			return new Result((left * unit) + leftover, 0);
		}
	}

	@Override
	public Result growStackValue(ItemStack stack, int amount, boolean simulate) {
		final int unit = getUnitCoinValue(stack);
		final int leftover = amount % unit;
		final int count = amount / unit;
		if(stack.getCount() + count <= stack.getMaxStackSize()) {
			final int newCount = stack.getCount() + count;
			if(!simulate)stack.grow(count);
			return new Result(leftover, newCount * unit);
		}else {
			int left = count - (stack.getMaxStackSize() - stack.getCount());
			if(!simulate)stack.setCount(stack.getMaxStackSize());
			return new Result((left * unit) + leftover, stack.getMaxStackSize() * unit);
		}
	}

	@Override
	public boolean hasEconomyValue(ItemStack stack) {
		return stack.getCount() > 0;
	}
}
