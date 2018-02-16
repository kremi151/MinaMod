package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.BlockHoneycomb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemEmptyPot extends Item{

	public ItemEmptyPot(){
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	ItemStack itemStackIn = playerIn.getHeldItem(hand);
        RayTraceResult mop = this.rayTrace(worldIn, playerIn, false);
        if(mop != null){
        	if(mop.typeOfHit == RayTraceResult.Type.BLOCK){
        		BlockPos pos = mop.getBlockPos();
        		IBlockState ibs = worldIn.getBlockState(pos);
        		if(ibs.getBlock() == MinaBlocks.HONEYCOMB){
        			if(ibs.getValue(BlockHoneycomb.HAS_HONEY)){
        				worldIn.setBlockState(pos, ibs.withProperty(BlockHoneycomb.HAS_HONEY, false));
        				ItemStack fullPot = new ItemStack(MinaItems.HONEY_POT,1,0);
        				if(playerIn.getRNG().nextInt(30) == 0)fullPot.setItemDamage(1);
        				itemStackIn.shrink(1);
        				if (itemStackIn.getCount() <= 0)
        		        {
        		            return new ActionResult(EnumActionResult.SUCCESS, fullPot);
        		        }
        		        else
        		        {
        		            if (!playerIn.inventory.addItemStackToInventory(fullPot))
        		            {
        		            	playerIn.dropItem(fullPot, false);
        		            }

        		            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        		        }
        			}
        		}
        	}
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }
}
