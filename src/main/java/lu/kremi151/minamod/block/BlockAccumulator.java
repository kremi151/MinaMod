package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.block.tileentity.TileEntityAccumulator;
import lu.kremi151.minamod.block.tileentity.TileEntitySolarPanel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAccumulator extends BlockCustomHorizontal{
	
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.9375, 0.9375);

	public BlockAccumulator() {
		super(Material.IRON, MapColor.SNOW);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityAccumulator();
    }
	
	/*@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		int position = 2;
		if(te instanceof TileEntitySolarPanel) {
			position = ((TileEntitySolarPanel)te).getPanelPosition();
		}
		return state.withProperty(POSITION, position);
	}*/

}
