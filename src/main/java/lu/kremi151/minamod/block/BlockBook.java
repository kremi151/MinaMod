package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityBook;
import lu.kremi151.minamod.util.ReflectionLoader;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBook extends BlockCustomHorizontal{

	private static final AxisAlignedBB BOOK_AABB_NORTH_SOUTH = new AxisAlignedBB(0.1875D, 0.0D, 0.125D, 0.8125D, 0.25D, 0.875D);
	private static final AxisAlignedBB BOOK_AABB_EAST_WEST = new AxisAlignedBB(0.125D, 0.0D, 0.1875D, 0.875D, 0.25D, 0.8125D);

	public BlockBook() {
		super(Material.WOOD);
	}

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityBook)
        {
        	TileEntityBook teBook = (TileEntityBook) tileentity;
        	ItemStack book = teBook.getBookItem();
        	if(!book.isEmpty()) {
        		if (!worldIn.isRemote)
                {
        			ReflectionLoader.ItemWrittenBook_resolveContents((ItemWrittenBook) book.getItem(), book, playerIn);
                }

                MinaMod.getProxy().openBook(book);
        	}else {
        		//TODO: Show message
        	}
        }

        return true;
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
		if(!worldIn.isRemote){
			if(tileentity instanceof TileEntityBook) {
				ItemStack book = ((TileEntityBook)tileentity).getBookItem();
				if(!book.isEmpty())InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), book);
			}
		}

        super.breakBlock(worldIn, pos, state);
    }
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityBook();
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
		switch(state.getValue(FACING)) {
		case EAST:
		case WEST: 
			return BOOK_AABB_EAST_WEST;
		default: 
			return BOOK_AABB_NORTH_SOUTH;
		}
    }

}
