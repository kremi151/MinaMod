package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.IEconomyValuable;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCoinBag extends Item implements IEconomyValuable{

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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.AQUA + I18n.translateToLocalFormatted("gui.coin_bag.amount", getEconomyValue(stack)));
		tooltip.add(I18n.translateToLocal("item.coin_bag.lore.rightclick"));
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

	@Override
	public int getEconomyValue(ItemStack stack) {
		return stack.getMetadata();
	}

	@Override
	public Result shrinkStackValue(ItemStack stack, int amount, boolean simulate) {
		if(stack.getMetadata() - amount >= 0) {
			int newDamage = stack.getItemDamage() - amount;
			if(!simulate)stack.setItemDamage(newDamage);
			return new Result(0, newDamage);
		}else {
			final int left = amount - stack.getItemDamage();
			if(!simulate)stack.setItemDamage(0);
			return new Result(left, 0);
		}
	}

	@Override
	public Result growStackValue(ItemStack stack, int amount, boolean simulate) {
		if(stack.getMetadata() + amount <= Short.MAX_VALUE) {
			int newDamage = stack.getItemDamage() + amount;
			if(!simulate)stack.setItemDamage(newDamage);
			return new Result(0, newDamage);
		}else {
			final int left = amount - (Short.MAX_VALUE - stack.getItemDamage());
			if(!simulate)stack.setItemDamage(Short.MAX_VALUE);
			return new Result(left, 0);
		}
	}

	@Override
	public boolean hasEconomyValue(ItemStack stack) {
		return getEconomyValue(stack) > 0;
	}
	
}
