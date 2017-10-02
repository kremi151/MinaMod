package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.tileentity.TileEntityWallCable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockWallCable extends Block{

	public static final IUnlistedProperty<Integer> MODEL_ID = new IntUProperty("model_id");
	public static final IUnlistedProperty<Integer> MODEL_META = new IntUProperty("model_meta");

	public BlockWallCable() {
		super(Material.ROCK, MapColor.GRAY);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityWallCable();
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityWallCable) {
			IBlockState model = ((TileEntityWallCable)te).getWallModel();
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY() + 0.5d, pos.getZ(), new ItemStack(MinaBlocks.CABLE));
			if(model != null) {
				worldIn.setBlockState(pos, model);
			}else {
				worldIn.setBlockToAir(pos);
			}
		}
		super.breakBlock(worldIn, pos, state);
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] {MODEL_ID, MODEL_META});
    }
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
		TileEntity te = world.getTileEntity(pos);
		int mid = 0, mmeta = 0;
		if(te instanceof TileEntityWallCable) {
			TileEntityWallCable te1 = (TileEntityWallCable) te;
			IBlockState wm = te1.getWallModel();
			mid = wm != null ? Block.getIdFromBlock(wm.getBlock()) : 0;
			mmeta = wm != null ? wm.getBlock().getMetaFromState(wm) : 0;
		}
        return ((IExtendedBlockState)state).withProperty(MODEL_ID, mid).withProperty(MODEL_META, mmeta);
    }
	
	private static class IntUProperty implements IUnlistedProperty<Integer>{
		
		private final String name;
		
		private IntUProperty(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isValid(Integer value) {
			return true;
		}

		@Override
		public Class<Integer> getType() {
			return Integer.class;
		}

		@Override
		public String valueToString(Integer value) {
			return String.valueOf(value.intValue());
		}
		
	}

}
