package lu.kremi151.minamod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoodContainer extends ItemFood{
	
	private final ItemStack container_item;
	
	public ItemFoodContainer(int amount, float saturation, boolean isWolfFood, Item container_item) {
		this(amount, saturation, isWolfFood, new ItemStack(container_item, 1));
	}

	public ItemFoodContainer(int amount, float saturation, boolean isWolfFood, ItemStack container_item) {
		super(amount, saturation, isWolfFood);
		this.container_item = container_item;
	}
	
	public ItemStack getFoodContainer(){
		return container_item.copy();
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		player.inventory.addItemStackToInventory(getFoodContainer());
    }
	
}
