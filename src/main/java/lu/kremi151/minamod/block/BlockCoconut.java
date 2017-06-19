package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCoconut extends BlockFalling{
	
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    
    private static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
    private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.3125D, 0.3125D, 0.625D, 0.6875D, 0.6875D, 1.0D);
    private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.375D);
    private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.625D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D);
    private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.375D, 0.6875D, 0.6875D);

	public BlockCoconut(){
		super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setCreativeTab(CreativeTabs.FOOD);
        this.setTickRandomly(true);
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
        if (!worldIn.isRemote)
        {
        	if(worldIn.isAirBlock(pos.down()) && !isAttachedToHead(worldIn, pos)){
                super.updateTick(worldIn, pos, state, rand);
        	}else if(worldIn.getBlockState(pos.down()).getBlock() == Blocks.SAND && rand.nextInt(20) == 0){
        		if(state.getValue(AGE) >= 3){
        			worldIn.setBlockState(pos, MinaBlocks.SAPLING.getDefaultState().withProperty(BlockMinaSapling.TYPE, BlockMinaPlanks.EnumType.PALM));
        		}else{
        			worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) + 1));
        		}
        	}
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
        return this.getDefaultState().withProperty(AGE, meta & 3);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(AGE);
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, AGE});
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand){}
}
