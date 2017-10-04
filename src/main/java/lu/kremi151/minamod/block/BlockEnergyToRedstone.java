package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.block.tileentity.TileEntityEnergyToRedstone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnergyToRedstone extends BlockCustomDirectional{
	
	public static final PropertyInteger OUTPUT = PropertyInteger.create("output", 0, 15);
	private static final AxisAlignedBB AABB_H = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);
	private static final AxisAlignedBB AABB_V = new AxisAlignedBB(0.1785, 0, 0.1785, 0.8125, 1, 0.8125);

	public BlockEnergyToRedstone() {
		super(Material.IRON);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		if(state.getValue(FACING).getAxis() == EnumFacing.Axis.Y) {
			return AABB_V;
		}else {
			return AABB_H;
		}
	}

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, getAdjustedFacing(pos, placer));
    }
	
	private static EnumFacing getAdjustedFacing(BlockPos pos, EntityLivingBase placer) {
		EnumFacing face = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		if(face.getHorizontalIndex() != -1) {
			return face.getOpposite();
		}else {
			return face;
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityEnergyToRedstone();
    }

	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getStrongPower(blockState, blockAccess, pos, side);
    }

	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

	@Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return (blockState.getValue(FACING).getOpposite() == side) ? getActualState(blockState, blockAccess, pos).getValue(OUTPUT).intValue() : 0;
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		int output = 0;
		if(te instanceof TileEntityEnergyToRedstone) {
			output = ((TileEntityEnergyToRedstone)te).getOutput();
		}
		return state.withProperty(OUTPUT, MathHelper.clamp(output, 0, 15));
	}
	
	@Override
    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, new IProperty[] {FACING, OUTPUT});
    }
	
}
