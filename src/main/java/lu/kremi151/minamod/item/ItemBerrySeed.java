package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class ItemBerrySeed extends Item implements net.minecraftforge.common.IPlantable{

    public ItemBerrySeed()
    {
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
        {
            worldIn.setBlockState(pos.up(), this.getPlantForMeta(itemstack.getMetadata()));
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		if(isInCreativeTab(tab)) {
			for(int n = 0 ; n <3 ; n++){
				items.add(new ItemStack(this,1,n));
			}
		}
	}
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        switch(stack.getMetadata()){
        case 1:
        	return "item.kevikus_seeds";
        case 2:
        	return "item.tracius_seeds";
        default:
        	return "item.doge_seeds";
        }
    }
    
    private IBlockState getPlantForMeta(int meta){
    	switch(meta){
    	case 1:
    		return MinaBlocks.KEVIKUS_CROP.getDefaultState();
    	case 2:
    		return MinaBlocks.TRACIUS_CROP.getDefaultState();
    	default:
    		return MinaBlocks.DOGE_CROP.getDefaultState();
    	}
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return MinaBlocks.KEVIKUS_CROP.getDefaultState();
	}
	
}
