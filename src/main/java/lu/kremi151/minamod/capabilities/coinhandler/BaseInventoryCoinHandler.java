package lu.kremi151.minamod.capabilities.coinhandler;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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
		final int l = handler.getSlots();
		int count = 0;
		for(int i = 0 ; i < l ; i++){
			ItemStack stack = handler.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem() == MinaItems.GOLDEN_COIN){
					count += stack.getCount();
				}else if(stack.getItem() == MinaItems.COIN_BAG){
					count += stack.getMetadata();
				}
			}
		}
		return count;
	}

	@Override
	public boolean hasCoins(int amount) {
		return getAmountCoins() >= amount;
	}

	@Override
	public boolean withdrawCoins(int amount) {
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)){
			IItemHandler handler = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(countCoins(handler) >= amount){
				final int l = handler.getSlots();
				for(int i = 0 ; i < l && amount > 0 ; i++){
					ItemStack stack = handler.getStackInSlot(i);
					if(!stack.isEmpty()){
						if(stack.getItem() == MinaItems.GOLDEN_COIN){
							int maxShrink = (stack.getCount() > amount)?amount:stack.getCount();
							amount -= maxShrink;
							stack.shrink(maxShrink);
						}else if(stack.getItem() == MinaItems.COIN_BAG){
							int maxShrink = (stack.getMetadata() > amount)?amount:stack.getMetadata();
							amount -= maxShrink;
							stack.setItemDamage(stack.getMetadata() - maxShrink);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean depositCoins(int amount) {
		if(provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)){
			IItemHandler handler = provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			final int l = handler.getSlots();
			for(int i = 0 ; i < l && amount > 0 ; i++){
				ItemStack stack = handler.getStackInSlot(i);
				if(!stack.isEmpty() && stack.getItem() == MinaItems.COIN_BAG){
					int maxGrow = Short.MAX_VALUE - stack.getMetadata();
					int grow = (maxGrow > amount)?amount:maxGrow;
					amount -= grow;
					stack.setItemDamage(stack.getMetadata() + grow);
				}
			}
			if(amount > 0){
				giveCoins(amount);
			}
			return true;
		}
		return false;
	}
	
	protected abstract void giveCoins(int amount);

}
