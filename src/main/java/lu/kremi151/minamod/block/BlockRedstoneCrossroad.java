package lu.kremi151.minamod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneCrossroad extends Block{
	
    protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);

    public static final PropertyBool INVERT_X = PropertyBool.create("invert_x");
    public static final PropertyBool INVERT_Z = PropertyBool.create("invert_z");
    public static final PropertyBool POWER_X = PropertyBool.create("power_x");
    public static final PropertyBool POWER_Z = PropertyBool.create("power_z");

	public BlockRedstoneCrossroad() {
		super(Material.CIRCUITS, MapColor.RED);
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return REDSTONE_DIODE_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
    	return  new BlockStateContainer(this, new IProperty[] {INVERT_X, INVERT_Z, POWER_X, POWER_Z});
    }
    
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState()
        		.withProperty(POWER_X, (meta & 1) > 0)
        		.withProperty(POWER_Z, (meta & 2) > 0)
        		.withProperty(INVERT_X, (meta & 4) > 0)
        		.withProperty(INVERT_Z, (meta & 8) > 0);
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(POWER_X) ? 1 : 0)
        		| (state.getValue(POWER_Z) ? 2 : 0)
        		| (state.getValue(INVERT_X) ? 4 : 0)
        		| (state.getValue(INVERT_Z) ? 8 : 0);
    }
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
		return ((side == EnumFacing.SOUTH && state.getValue(INVERT_Z) && state.getValue(POWER_Z))
				|| (side == EnumFacing.WEST && !state.getValue(INVERT_X) && state.getValue(POWER_X))
				|| (side == EnumFacing.NORTH && !state.getValue(INVERT_Z) && state.getValue(POWER_Z))
				|| (side == EnumFacing.EAST && state.getValue(INVERT_X) && state.getValue(POWER_X))) ? 15 : 0;
    }
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getWeakPower(blockState, blockAccess, pos, side);
    }
	
	private int calculateInputStrength(World worldIn, BlockPos pos, EnumFacing face)
    {
        BlockPos blockpos = pos.offset(face);
        int i = worldIn.getRedstonePower(blockpos, face);

        if (i >= 15)
        {
            return i;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }
	
	public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isFullyOpaque();
    }
	
	/**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (this.canBlockStay(worldIn, pos))
        {
            this.updateState(worldIn, pos, state);
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }
	
	private boolean shouldBePowered(World worldIn, BlockPos pos, EnumFacing face)
    {
        return this.calculateInputStrength(worldIn, pos, face) > 0;
    }

	private void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
		EnumFacing h = getHorizontalFacing(state.getValue(INVERT_X));
		EnumFacing v = getVerticalFacing(state.getValue(INVERT_Z));

    	boolean flag_h = this.shouldBePowered(worldIn, pos, h.getOpposite());
    	boolean flag_v = this.shouldBePowered(worldIn, pos, v.getOpposite());

        if (state.getValue(POWER_X) != flag_h && !worldIn.isBlockTickPending(pos, this))
        {
            int i = -1;

            /*if (this.isFacingTowardsRepeater(worldIn, pos, state))
            {
                i = -3;
            }
            else*/ if (state.getValue(POWER_X))
            {
                i = -2;
            }

            worldIn.updateBlockTick(pos, this, 1, i);
        }

        if (state.getValue(POWER_Z) != flag_v && !worldIn.isBlockTickPending(pos, this))
        {
            int i = -1;

            /*if (this.isFacingTowardsRepeater(worldIn, pos, state))
            {
                i = -3;
            }
            else*/ if (state.getValue(POWER_Z))
            {
                i = -2;
            }

            worldIn.updateBlockTick(pos, this, 1, i);
        }
    }

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		EnumFacing h = getHorizontalFacing(state.getValue(INVERT_X));
		EnumFacing v = getVerticalFacing(state.getValue(INVERT_Z));
		
		boolean flag_h = this.shouldBePowered(worldIn, pos, h.getOpposite());
		boolean flag_v = this.shouldBePowered(worldIn, pos, v.getOpposite());

        if (state.getValue(POWER_X) && !flag_h)
        {
        	state = state.withProperty(POWER_X, false);
            worldIn.setBlockState(pos, state, 2);
        }
        else if (!state.getValue(POWER_X))
        {
        	state = state.withProperty(POWER_X, true);
        	worldIn.setBlockState(pos, state, 2);

            if (!flag_h)
            {
                worldIn.updateBlockTick(pos, this, 1, -1);
            }
        }

        if (state.getValue(POWER_Z) && !flag_v)
        {
            worldIn.setBlockState(pos, state.withProperty(POWER_Z, false), 2);
        }
        else if (!state.getValue(POWER_Z))
        {
        	worldIn.setBlockState(pos, state.withProperty(POWER_Z, true), 2);

            if (!flag_v)
            {
                worldIn.updateBlockTick(pos, this, 1, -1);
            }
        }
        
    }
	
	private EnumFacing getHorizontalFacing(boolean invert){
		return invert ? EnumFacing.WEST : EnumFacing.EAST;
	}
	
	private EnumFacing getVerticalFacing(boolean invert){
		return invert ? EnumFacing.SOUTH : EnumFacing.NORTH;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
	public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }

}
