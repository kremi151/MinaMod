package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMilkBottle extends ItemFood{
	public ItemMilkBottle() {
		super(2,0.6F,false);
		this.setCreativeTab(CreativeTabs.FOOD);
		this.setContainerItem(Items.GLASS_BOTTLE);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.DRINK;
    }
	
	@Override
	public void onFoodEaten(ItemStack is, World w, EntityPlayer p){
		p.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE,1));
		super.onFoodEaten(is, w, p);
	}

}
