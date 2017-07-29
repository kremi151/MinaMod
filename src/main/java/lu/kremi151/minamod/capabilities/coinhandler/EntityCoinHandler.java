package lu.kremi151.minamod.capabilities.coinhandler;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class EntityCoinHandler extends BaseInventoryCoinHandler{
	
	private final Entity entity;

	public EntityCoinHandler(Entity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	protected void giveCoins(int amount) {
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler inv = (IItemHandler) entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			while(amount > 0){
				final int originAmount = Math.min(MinaItems.GOLDEN_COIN.getItemStackLimit(), amount);
				ItemStack stack = ItemHandlerHelper.insertItem(inv, new ItemStack(MinaItems.GOLDEN_COIN, originAmount), false);
				amount -= originAmount;
				amount += stack.getCount();
				if(!stack.isEmpty()) {
					break;
				}
			}
		}
		while(amount > 0){
			ItemStack stack = new ItemStack(MinaItems.GOLDEN_COIN, Math.min(MinaItems.GOLDEN_COIN.getItemStackLimit(), amount));
			MinaUtils.dropItem(stack, entity.world, entity.posX, entity.posY + 0.5, entity.posZ).setDefaultPickupDelay();
			amount -= stack.getCount();
		}
	}

}
