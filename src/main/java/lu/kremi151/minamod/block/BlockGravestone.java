package lu.kremi151.minamod.block;

import lu.kremi151.minamod.block.tileentity.TileEntityGravestone;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGravestone extends Block{
	
	private static final AxisAlignedBB GRAVESTONE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 0.8125D, 0.625D);

	public BlockGravestone() {
		super(Material.ROCK);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityGravestone();
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		if(!worldIn.isRemote){
			if(tileentity instanceof TileEntityGravestone) {
				NonNullList<ItemStack> items = ((TileEntityGravestone)tileentity).getItems();
				final double x = (double)pos.getX();
				final double y = (double)pos.getY();
				final double z = (double)pos.getZ();
				for (int i = 0; i < items.size(); ++i)
		        {
		            ItemStack itemstack = items.get(i);

		            if (!itemstack.isEmpty())
		            {
		            	InventoryHelper.spawnItemStack(worldIn, x, y, z, itemstack);
		            }
		        }
			}
		}

        super.breakBlock(worldIn, pos, state);
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
        return GRAVESTONE_AABB;
    }

}
