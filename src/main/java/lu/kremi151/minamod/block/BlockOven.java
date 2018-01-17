package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityOven;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOven extends BlockCustomHorizontal{
	
	private static final PropertyBool COOKING = PropertyBool.create("cooking");

	public BlockOven() {
		super(Material.IRON);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(COOKING, false));
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getActualState(state, world, pos).getValue(COOKING) ? 10 : 0;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        if(!world.isRemote){
        	if (player.isSneaking()) {
            	return false;
            }
    	    player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdOven, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		boolean cooking = false;
		if(te instanceof TileEntityOven) {
			cooking = ((TileEntityOven)te).isCooking();
		}
		return state.withProperty(COOKING, cooking);
	}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityOven)
        {
            TileEntityOven oven = (TileEntityOven) tileentity;
            if(!oven.getInputInventory().getStackInSlot(0).isEmpty()) {
            	InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), oven.getInputInventory().getStackInSlot(0));
            }
            if(!oven.getOutputInventory().getStackInSlot(0).isEmpty()) {
            	InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), oven.getOutputInventory().getStackInSlot(0));
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }
	
	@Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityOven) {
        	return MinaUtils.calcRedstoneFromItemHandlers(((TileEntityOven) te).getInputInventory(), ((TileEntityOven) te).getOutputInventory());
        }else {
        	return 0;
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
        return new TileEntityOven();
    }
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, new IProperty[] {COOKING, FACING});
    }

}
