package lu.kremi151.minamod.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPumpkinLantern extends BlockCustomHorizontal{
	
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.25D, 0.125D, 0.0D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.25D, 0.125D, 0.25D, 0.75D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.25D, 0.125D, 0.25D, 1.0D, 1.0D, 0.75D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.125D, 0.25D, 0.75D, 1.0D, 0.75D);

	public BlockPumpkinLantern() {
		super(Material.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(1.0F);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
		return 14;
    }

    @Override
    public boolean isOpaqueCube(IBlockState bs)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState bs)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch(state.getValue(FACING)) {
        	case EAST: return EAST_AABB;
        	case SOUTH: return SOUTH_AABB;
        	case WEST: return WEST_AABB;
        	default: return NORTH_AABB;
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        //return canPlaceBlockOnSide(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty(FACING, facing) : this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        if( canPlaceBlockOnSide(worldIn, pos, facing)) {
        	return this.getDefaultState().withProperty(FACING, facing);
        }else {
        	return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        }
    }

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return canAttachTo(worldIn, pos, side.getOpposite());
    }

	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (canAttachTo(worldIn, pos, enumfacing))
            {
                return true;
            }
        }

        return false;
    }

    protected static boolean canAttachTo(World worldIn, BlockPos pos, EnumFacing direction)
    {
    	if(direction.getHorizontalIndex() != -1) {
    		BlockPos blockpos = pos.offset(direction);
            return worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, direction.getOpposite());
    	}else {
    		return false;
    	}
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

}
