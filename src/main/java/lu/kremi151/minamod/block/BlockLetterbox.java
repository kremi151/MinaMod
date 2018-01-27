package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityLetterbox;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLetterbox extends BlockCustomHorizontal{
	
	protected static final AxisAlignedBB bb_e_w = new AxisAlignedBB(0.125d, 0d, 0.25d, 0.875d, 0.875d, 0.75d);
	protected static final AxisAlignedBB bb_n_s = new AxisAlignedBB(0.25d, 0f, 0.125d, 0.75d, 0.875d, 0.875d);

    public static final PropertyBool EMPTY = PropertyBool.create("empty");
	
	public BlockLetterbox() {
		super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EMPTY, true));
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD);
	}

    @Override
    protected BlockStateContainer createBlockState()
    {
    	return  new BlockStateContainer(this, new IProperty[] {FACING, EMPTY});
    }
    
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(EMPTY, ((TileEntityLetterbox)world.getTileEntity(pos)).isEmpty());
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
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityLetterbox();
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		EnumFacing facing = (EnumFacing) state.getValue(FACING);
		if(facing==EnumFacing.EAST||facing==EnumFacing.WEST){
			return bb_e_w;
		}else{
			return bb_n_s;
		}
	}

	@Override
    public boolean hasComparatorInputOverride(IBlockState ibs)
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(IBlockState ibs, World worldIn, BlockPos pos)
    {
		TileEntityLetterbox te = (TileEntityLetterbox) worldIn.getTileEntity(pos);
        return Container.calcRedstoneFromInventory(te.getStorageInventory());
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        if(!world.isRemote){
        	if (player.isSneaking()) {
            	return false;
            }
    		TileEntityLetterbox te = (TileEntityLetterbox) world.getTileEntity(pos);
    		if(te.isOwnerSet()){
    			te.setOwner(player.getGameProfile());
    		}
    	    player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdLetterbox, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if(!worldIn.isRemote){
			TileEntityLetterbox tileentity = (TileEntityLetterbox) worldIn.getTileEntity(pos);

			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity.getInputInventory());
			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity.getStorageInventory());
	        worldIn.updateComparatorOutputLevel(pos, this);
		}

        super.breakBlock(worldIn, pos, state);
    }
	
}
