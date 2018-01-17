package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaSounds;
import lu.kremi151.minamod.interfaces.IDrillItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDrill extends Item{

	public ItemDrill() {
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(40);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + (stack.getItemDamage() >= stack.getMaxDamage() ? "drill_broken" : "drill");
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItemDamage() < stack.getMaxDamage()) {
			ItemStack stack1 = player.getHeldItem((hand==EnumHand.MAIN_HAND)?EnumHand.OFF_HAND:EnumHand.MAIN_HAND);
			if(!stack1.isEmpty() && stack1.getItem() instanceof IDrillItem && ((IDrillItem)stack1.getItem()).onDrillUsed(worldIn, pos, player, stack1)) {
				stack.damageItem(1, player);
				player.playSound(MinaSounds.soundDrillTurn, 1.0f, 1.0f);
				return EnumActionResult.SUCCESS;
			}else {
				return EnumActionResult.FAIL;
			}
		}else {
			return EnumActionResult.FAIL;
		}
    }
	
	
}
