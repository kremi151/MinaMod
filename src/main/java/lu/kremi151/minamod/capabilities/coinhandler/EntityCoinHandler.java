package lu.kremi151.minamod.capabilities.coinhandler;

import java.util.ListIterator;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
		NonNullList<ItemStack> coinStacks = NonNullList.create();
		MinaItems.GOLDEN_COIN.regroupToCoins(coinStacks, amount);
		ListIterator<ItemStack> it = coinStacks.listIterator();
		if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler inv = (IItemHandler) entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			while(it.hasNext()) {
				ItemStack stack = ItemHandlerHelper.insertItem(inv, it.next(), false);
				if(!stack.isEmpty()) {
					it.set(stack);
					break;
				}
			}
		}
		while(it.hasNext()) {
			ItemStack stack = it.next();
			MinaUtils.dropItem(stack, entity.world, entity.posX, entity.posY + 0.5, entity.posZ).setDefaultPickupDelay();
		}
	}

}
