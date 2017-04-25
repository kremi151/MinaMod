package lu.kremi151.minamod.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class ItemSickle extends ItemTool{

	public ItemSickle(Item.ToolMaterial material) {
		super(0.5F, -3.5F, material, null);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return efficiencyOnProperMaterial;
        }
        return (state.getBlock() instanceof IPlantable) ? this.efficiencyOnProperMaterial : 1.0F;
    }
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
    {
		stack.damageItem(1, entityLiving);
        if(!worldIn.isRemote && blockIn.getBlock() instanceof IPlantable){
        	for(int i = -1 ; i <= 1 ; i++){
        		for(int j = -1 ; j <= 1 ; j++){
        			if(i != 0 || j != 0){
        				BlockPos npos = pos.add(i, 0, j);
                		if(worldIn.getBlockState(npos).getBlock() instanceof IPlantable){
                			worldIn.destroyBlock(npos, true);
                		}
        			}
            	}
        	}
        }

        return true;
    }

}
