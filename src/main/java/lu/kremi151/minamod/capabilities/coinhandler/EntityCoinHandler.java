package lu.kremi151.minamod.capabilities.coinhandler;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityCoinHandler extends BaseInventoryCoinHandler{
	
	private Entity entity;

	public EntityCoinHandler(Entity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	protected void giveCoins(int amount) {
		while(amount > 0){
			ItemStack stack = new ItemStack(MinaItems.GOLDEN_COIN, amount % 65);
			entity.entityDropItem(stack, 0.3f);
			amount -= 64;
		}
	}

}
