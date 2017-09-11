package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCombiner extends Item{
	
	public ItemCombiner() {
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxDamage(20);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(handIn == EnumHand.MAIN_HAND) {
			playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdCombiner, worldIn, playerIn.inventory.currentItem, 0, 0);
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}else {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
    }

}
