package lu.kremi151.minamod.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockCustomDirectional extends BlockDirectional{

	protected BlockCustomDirectional(Material materialIn) {
		super(materialIn);
	}

	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.isSideSolid(pos.down(), EnumFacing.UP);
    }

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
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

}
