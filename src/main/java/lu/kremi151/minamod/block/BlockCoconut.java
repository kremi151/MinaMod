package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoconut extends BlockFalling{
	
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);
    
    protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.3125D, 0.3125D, 0.625D, 0.6875D, 0.6875D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.375D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.625D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.375D, 0.6875D, 0.6875D);

	public BlockCoconut(){
		super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setCreativeTab(CreativeTabs.FOOD);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)getActualState(state, source, pos).getValue(FACING);

        switch (enumfacing)
        {
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
                return AABB_NORTH;
            case UP:
            default:
                return AABB_UP;
        }
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && !isAttachedToHead(worldIn, pos))
        {
            super.updateTick(worldIn, pos, state, rand);
        }
    }
	
	private EnumFacing getAttachedHeadSide(IBlockAccess world, BlockPos pos){
		for(EnumFacing facing : EnumFacing.HORIZONTALS){
			IBlockState state = world.getBlockState(pos.offset(facing));
			if(state.getBlock() == MinaBlocks.LOG_PALM && state.getValue(BlockPalmLog.HEAD)){
				return facing;
			}
		}
		return null;
	}
	
	private boolean isAttachedToHead(IBlockAccess world, BlockPos pos){
		return getAttachedHeadSide(world, pos) != null;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		EnumFacing nfacing = placer.getHorizontalFacing().getOpposite();
        if(nfacing != EnumFacing.DOWN){
        	return this.getDefaultState().withProperty(FACING, nfacing);
        }else{
        	return this.getDefaultState();
        }
    }
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
		EnumFacing attached = getAttachedHeadSide(worldIn, pos);
        if(attached != null){
        	return state.withProperty(FACING, attached.getOpposite());
        }else{
        	return state.withProperty(FACING, EnumFacing.UP);
        }
    }

	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
    {
		EnumFacing actual = state.getValue(FACING);
        switch (rot)
        {
            case CLOCKWISE_180:
                if(actual.getHorizontalIndex() != -1){
                	return state.withProperty(FACING, actual.getOpposite());
                }else{
                	return state;
                }
            case COUNTERCLOCKWISE_90:
            	if(actual.getHorizontalIndex() != -1){
            		int hi = actual.getHorizontalIndex() - 1;
            		if(hi == 0)hi = 3;
            		return state.withProperty(FACING, EnumFacing.getHorizontal(hi));
            	}else{
                	return state;
                }
            case CLOCKWISE_90:
            	if(actual.getHorizontalIndex() != -1){
            		int hi = actual.getHorizontalIndex() + 1;
            		if(hi == 4)hi = 0;
            		return state.withProperty(FACING, EnumFacing.getHorizontal(hi));
            	}else{
                	return state;
                }
            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
	@Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
		EnumFacing actual = state.getValue(FACING);
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                if(actual == EnumFacing.NORTH || actual == EnumFacing.SOUTH){
                	return state.withProperty(FACING, actual.getOpposite());
                }else{
                	return state;
                }
            case FRONT_BACK:
            	if(actual == EnumFacing.WEST || actual == EnumFacing.EAST){
                	return state.withProperty(FACING, actual.getOpposite());
                }else{
                	return state;
                }
            default:
                return super.withMirror(state, mirrorIn);
        }
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){}
}
