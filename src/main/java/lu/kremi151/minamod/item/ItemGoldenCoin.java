package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.interfaces.IUnitEconomyValuable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemGoldenCoin extends Item implements IUnitEconomyValuable{

	public ItemGoldenCoin() {
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if(isInCreativeTab(tab)) {
        	subItems.add(new ItemStack(this, 1, 0));
            subItems.add(new ItemStack(this, 1, 1));
        }
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + (stack.getMetadata() == 1 ? ".five" : ".one");
    }
	
	public static void regroupToCoins(NonNullList<ItemStack> coinStacks, int amount) {
		int fiveUnits = amount / 5;
		int oneUnits = amount % 5;
		while(fiveUnits > 0) {
			ItemStack coinStack = new ItemStack(MinaItems.GOLDEN_COIN, Math.min(fiveUnits, MinaItems.GOLDEN_COIN.getItemStackLimit()), 1);
			fiveUnits -= coinStack.getCount();
			coinStacks.add(coinStack);
		}
		coinStacks.add(new ItemStack(MinaItems.GOLDEN_COIN, oneUnits, 0));
	}
	
	public static int getUnitCoinValue(ItemStack stack) {
		if(stack.isEmpty() || stack.getItem() != MinaItems.GOLDEN_COIN) {
			throw new IllegalArgumentException();
		}
		switch(stack.getMetadata()) {
		case 1:
			return 5;
		default:
			return 1;
		}
	}
	
	public static int getCoinValue(ItemStack stack) {
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
