package lu.kremi151.minamod.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockQuicksand extends BlockFalling{

	public BlockQuicksand() {
		super(Material.SAND);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.SAND);
		this.setHardness(0.7f);
	}
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(!isFallingQuicksand(entityIn)){
        	entityIn.setInWeb();
        }else{
        	entityIn.setDead();
        	worldIn.setBlockState(new BlockPos(entityIn).up(), ((EntityFallingBlock)entityIn).getBlock());
        }
    }
	
	private boolean isFallingQuicksand(Entity entity){
		return entity instanceof EntityFallingBlock && ((EntityFallingBlock)entity).getBlock().getBlock() == this;
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public int getDustColor(IBlockState p_189876_1_)
    {
        return -2370656;
    }

}
