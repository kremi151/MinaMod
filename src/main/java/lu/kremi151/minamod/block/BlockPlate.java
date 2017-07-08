package lu.kremi151.minamod.block;

import lu.kremi151.minamod.block.tileentity.TileEntityPlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlate extends BlockCustomHorizontal{
	
	protected static final AxisAlignedBB blockBounds = new AxisAlignedBB(0.125d, 0d, 0.125d, 0.875d, 0.125d, 0.875d);

	public BlockPlate() {
		super(Material.GLASS);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(1.0F);
		setSoundType(SoundType.GLASS);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}
	
	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState ibs)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityPlate();
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntityPlate te = (TileEntityPlate) worldIn.getTileEntity(pos);
		if(!te.getItem().isEmpty()){
			EntityItem e = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ());
			e.setItem(te.getItem());
			worldIn.spawnEntity(e);
		}
		super.breakBlock(worldIn, pos, state);
    }

}
