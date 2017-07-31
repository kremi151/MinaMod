package lu.kremi151.minamod.capabilities.coinhandler;

import java.util.Iterator;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.interfaces.IEconomyValuable;
import lu.kremi151.minamod.interfaces.IUnitEconomyValuable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BaseInventoryCoinHandler implements ICoinHandler{
	
	private ICapabilityProvider provider;
	
	public BaseInventoryCoinHandler(ICapabilityProvider provider){
		this.provider = provider;
	}

	@Override
	public int getAmountCoins() {
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)){
			return countCoins(provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		}
		return 0;
	}
	
	private int countCoins(IItemHandler handler){
		NonNullList<ItemStack> list = listValuables(handler);
		int count = 0;
		for(ItemStack stack : list) {
			count += ((IEconomyValuable)stack.getItem()).getEconomyValue(stack);
		}
		return count;
	}

	@Override
	public boolean hasCoins(int amount) {
		return getAmountCoins() >= amount;
	}
	
	private NonNullList<ItemStack> listValuablesSorted(IItemHandler handler){
		NonNullList<ItemStack> list = listValuables(handler);
		list.sort((a,b) -> {
			if(!(a.getItem() instanceof IUnitEconomyValuable)) {
				return 1;
			}else if(!(b.getItem() instanceof IUnitEconomyValuable)) {
				return -1;
			}else {
				return ((IUnitEconomyValuable)b.getItem()).getUnitEconomyValue(b) - ((IUnitEconomyValuable)a.getItem()).getUnitEconomyValue(a);
			}
		});
		return list;
	}
	
	private NonNullList<ItemStack> listValuables(IItemHandler handler){
		NonNullList<ItemStack> list = NonNullList.create();
		for(int i = 0 ; i < handler.getSlots() ; i++) {
			ItemStack stack = handler.getStackInSlot(i);
			if(stack.getItem() instanceof IEconomyValuable) {
				list.add(stack);
			}
		}
		return list;
	}

	@Override
	public boolean withdrawCoins(int amount, boolean simulate) {
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)){
			IItemHandler handler = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(countCoins(handler) >= amount){
				NonNullList<ItemStack> valuables = listValuablesSorted(handler);
				boolean changed = false;
				while(valuables.size() > 0) {
					changed = false;
					Iterator<ItemStack> it = valuables.iterator();
					while(it.hasNext()) {
						ItemStack stack = it.next();
						IEconomyValuable ev = (IEconomyValuable) stack.getItem();
						final int oldStackValue = ev.getEconomyValue(stack);
						IEconomyValuable.Result res = ev.shrinkStackValue(stack, amount, simulate);
						amount = res.notConsumed;
						if(amount <= 0) {
							return true;
						}else if(oldStackValue > res.newValue) {
							it.remove();
							changed = true;
						}
					}
					if(!changed) {
						return withdrawCoinsWithChange(handler, amount, simulate);
					}
				}
				return amount <= 0;
			}
		}
		return false;
	}
	
	private boolean withdrawCoinsWithChange(IItemHandler handler, int amount, boolean simulate) {
		NonNullList<ItemStack> valuables = listValuablesSorted(handler);
		Iterator<ItemStack> it = valuables.iterator();
		while(it.hasNext()) {
			ItemStack stack = it.next();
			if(stack.getItem() == MinaItems.GOLDEN_COIN) {//TODO: Make compatible with other items
				final int unit = MinaItems.GOLDEN_COIN.getUnitCoinValue(stack);
				final int value = MinaItems.GOLDEN_COIN.getCoinValue(stack);
				if(amount <= value) {
					int leftover = amount % unit;
					int stackSize = stack.getCount() - (amount / unit);
					if(!simulate)stack.setCount(stackSize);
					amount = leftover;
					if(amount > 0 && amount <= unit && stackSize > 0) {
						int change = unit - amount;
						if(!simulate) {
							stack.shrink(1);
							giveCoins(change);
						}
						amount = 0;
						return true;
					}
				}else {
					int leftover = value % unit;
					if(!simulate)stack.shrink(value / unit);
					amount -= value - leftover;
				}
			}
		}
		return amount == 0;
	}

	@Override
	public boolean depositCoins(int amount, boolean simulate) {
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)){
			IItemHandler handler = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			NonNullList<ItemStack> valuables = listValuables(handler);
			boolean changed = false;
			while(valuables.size() > 0) {
				changed = false;
				Iterator<ItemStack> it = valuables.iterator();
				while(it.hasNext()) {
					ItemStack stack = it.next();
					IEconomyValuable ev = (IEconomyValuable) stack.getItem();
					final int oldStackValue = ev.getEconomyValue(stack);
					IEconomyValuable.Result res = ev.growStackValue(stack, amount, simulate);
					amount = res.notConsumed;
					if(amount <= 0) {
						return true;
					}else if(oldStackValue < res.newValue) {
						it.remove();
						changed = true;
					}
				}
				if(!changed) {
					return false;
				}
			}
			if(amount > 0 && !simulate) {
				giveCoins(amount);
			}
			return true;
		}
		return false;
	}
	
	protected abstract void giveCoins(int amount);

}
