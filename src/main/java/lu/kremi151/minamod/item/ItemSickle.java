package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaEnchantments;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return efficiency;
        }
        return (state.getBlock() instanceof IPlantable) ? this.efficiency : 1.0F;
    }
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
    {
		stack.damageItem(1, entityLiving);
        if(!worldIn.isRemote && blockIn.getBlock() instanceof IPlantable){
        	final int level = EnchantmentHelper.getEnchantmentLevel(MinaEnchantments.HARVEST_RANGE, stack);
        	final int radius = (level >= 2) ? 2 : 1;
        	for(int i = -radius ; i <= radius ; i++){
        		for(int j = -radius ; j <= radius ; j++){
        			if(i != 0 || j != 0){
        				tryHarvest(worldIn, pos.add(i, 0, j));
        			}
            	}
        	}
        	if(level == 1) {
        		tryHarvest(worldIn, pos.add(radius + 1, 0, 0));
        		tryHarvest(worldIn, pos.add(-(radius + 1), 0, 0));
        		tryHarvest(worldIn, pos.add(0, 0, radius + 1));
        		tryHarvest(worldIn, pos.add(0, 0, -(radius + 1)));
        	}
        }

        return true;
    }
	
	private void tryHarvest(World worldIn, BlockPos pos) {
		if(worldIn.getBlockState(pos).getBlock() instanceof IPlantable){
			worldIn.destroyBlock(pos, true);
		}
	}

}
