package lu.kremi151.minamod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDimmableLight extends Block{
	
	public static final PropertyInteger LIGHT = PropertyInteger.create("light", 0, 15);
	public static final PropertyBool IS_ON = PropertyBool.create("is_on");

	public BlockDimmableLight() {
		super(Material.GLASS, MapColor.GRAY);
		this.setSoundType(SoundType.GLASS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LIGHT, 0));
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(IS_ON, state.getValue(LIGHT) > 0);
    }
	
	@Override
	public int getLightValue(IBlockState state)
    {
        return state.getValue(LIGHT);
    }
	
	private int getLightIntensity(BlockPos pos, World world){
		int max = 0;
		for(EnumFacing face : EnumFacing.VALUES){
			max = Math.max(max, world.getRedstonePower(pos.offset(face), face));
		}
		return max;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
        	int intensity = getLightIntensity(pos, worldIn);
        	if(intensity > 0){
        		worldIn.setBlockState(pos, state.withProperty(LIGHT, intensity), 2);
        	}
        }
    }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
        	int intensity = getLightIntensity(pos, worldIn);
        	if(intensity != state.getValue(LIGHT)){
        		worldIn.setBlockState(pos, state.withProperty(LIGHT, intensity), 2);
        	}
        }
    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]{LIGHT, IS_ON});
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LIGHT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LIGHT);
    }

}
