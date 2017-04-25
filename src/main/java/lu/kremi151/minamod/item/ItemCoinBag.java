package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCoinBag extends Item{

	public ItemCoinBag(){
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
    public boolean isDamageable()
    {
        return false;
    }
	
	@Override
    public int getMetadata(int damage)
    {
        return damage;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		tooltip.add(I18n.translateToLocalFormatted("gui.coin_bag.amount", stack.getMetadata()));
    }
	
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if(hand == EnumHand.MAIN_HAND){
    		playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdCoinBag, worldIn, 0, 0, 0);
    		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    	}
    	return super.onItemRightClick(worldIn, playerIn, hand);
    }
	
}
