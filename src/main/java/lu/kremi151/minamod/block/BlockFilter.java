package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityFilter;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFilter extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.UP);
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.25d, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D);

    public BlockFilter()
    {
        super(Material.IRON, MapColor.GRAY);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, Boolean.valueOf(true)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch(state.getValue(FACING)) {
        	case NORTH: return NORTH_AABB;
        	case EAST: return EAST_AABB;
        	case SOUTH: return SOUTH_AABB;
        	case WEST: return WEST_AABB;
        	default: return DOWN_AABB;
        }
    }
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();

        if (enumfacing == EnumFacing.UP)
        {
            enumfacing = EnumFacing.DOWN;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, Boolean.valueOf(true));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFilter();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFilter)
            {
                ((TileEntityFilter)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.updateState(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFilter)
            {
            	playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdFilter, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.updateState(worldIn, pos, state);
    }

    private void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = !worldIn.isBlockPowered(pos);

        if (flag != ((Boolean)state.getValue(ENABLED)).booleanValue())
        {
            worldIn.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)), 4);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityFilter)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFilter)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
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

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    public static EnumFacing getFacing(int meta)
    {
        return EnumFacing.getFront(meta & 7);
    }

    public static boolean isEnabled(int meta)
    {
        return (meta & 8) != 8;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ENABLED, Boolean.valueOf(isEnabled(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (!((Boolean)state.getValue(ENABLED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, ENABLED});
    }

}
