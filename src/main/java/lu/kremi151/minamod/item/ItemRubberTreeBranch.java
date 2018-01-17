package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRubberTreeBranch extends ItemBlock{

	public ItemRubberTreeBranch() {
		super(MinaBlocks.RUBBER_TREE);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        
        if(block == Blocks.GRASS || block == Blocks.DIRT) {
        	return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }else {
        	return EnumActionResult.FAIL;
        }
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item.rubber_tree_branch";
    }

	@Override
	public String getUnlocalizedName()
    {
        return "item.rubber_tree_branch";
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public CreativeTabs getCreativeTab()
    {
        return CreativeTabs.DECORATIONS;
    }

}
