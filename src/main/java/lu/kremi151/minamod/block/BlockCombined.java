package lu.kremi151.minamod.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCombined extends BlockCustom
{
    public static final PropertyEnum<EnumBlockMode> TYPE = PropertyEnum.<EnumBlockMode>create("type", EnumBlockMode.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockCombined(Material materialIn)
    {
        this(materialIn, materialIn.getMaterialMapColor());
    }

    public BlockCombined(Material material, MapColor mapColor)
    {
        super(material, mapColor);
        this.setLightOpacity(255);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumBlockMode.FULL));
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return this.isFullBlock(state) ? FULL_BLOCK_AABB : (state.getValue(TYPE) == EnumBlockMode.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF);
    }

    /**
     * Checks if an IBlockState represents a block that is opaque and a full cube.
     */
    @Override
    public boolean isTopSolid(IBlockState state)
    {
    	EnumBlockMode type = state.getValue(TYPE);
        return type.isFull() || type == EnumBlockMode.TOP;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return this.isFullBlock(state);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if ( state.isOpaqueCube() )
            return true;

        EnumBlockMode type = state.getValue(TYPE);
        return (type == EnumBlockMode.TOP && face == EnumFacing.UP) || (type == EnumBlockMode.BOTTOM && face == EnumFacing.DOWN);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	if(meta == 0){
    		return this.getDefaultState().withProperty(TYPE, EnumBlockMode.FULL);
    	}else{
    		IBlockState iblockstate = this.getDefaultState().withProperty(TYPE, EnumBlockMode.BOTTOM);
            return this.isFullBlock(iblockstate) ? iblockstate : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(TYPE, EnumBlockMode.TOP));
    	}
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return state.getValue(TYPE) == EnumBlockMode.DOUBLE ? 2 : 1;
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE) == EnumBlockMode.FULL ? 0 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return state.getValue(TYPE).isFull();
    }
    
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return state.getValue(TYPE).isFull();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (this.isFullBlock(blockState))
        {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side))
        {
            return false;
        }
        /*else if (false) // Forge: Additional logic breaks doesSideBlockRendering and is no longer useful.
        {
            IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
            boolean flag = isHalfSlab(iblockstate) && iblockstate.getValue(TYPE) == EnumBlockMode.TOP;
            boolean flag1 = isHalfSlab(blockState) && blockState.getValue(TYPE) == EnumBlockMode.TOP;
            return flag1 ? (side == EnumFacing.DOWN ? true : (side == EnumFacing.UP && super.shouldSideBeRendered(blockState, blockAccess, pos, side) ? true : !isHalfSlab(iblockstate) || !flag)) : (side == EnumFacing.UP ? true : (side == EnumFacing.DOWN && super.shouldSideBeRendered(blockState, blockAccess, pos, side) ? true : !isHalfSlab(iblockstate) || flag));
        }*/
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @SideOnly(Side.CLIENT)
    protected static boolean isHalfSlab(IBlockState state)
    {
        return !state.getValue(TYPE).isFull();
    }
    
    @Override
    public int getLightOpacity(IBlockState state)
    {
        return state.getValue(TYPE).isFull() ? 255 : 128;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	list.add(new ItemStack(itemIn, 1, 0));//Full
    	list.add(new ItemStack(itemIn, 1, 1));//Slab
    }

	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, EnumBlockMode.getByMeta(meta));
    }
	
	private IBlockState getStateFromItemMeta(int meta)
    {
        if(meta == 1){
        	return this.getDefaultState().withProperty(TYPE, EnumBlockMode.BOTTOM);
        }else{
        	return this.getDefaultState().withProperty(TYPE, EnumBlockMode.FULL);
        }
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE).getMeta();
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TYPE});
    }

    public static enum EnumBlockMode implements IStringSerializable
    {
    	FULL(0, "full", true),
        TOP(1, "top", false),
        BOTTOM(2, "bottom", false),
        DOUBLE(3, "double", true);

    	private final int meta;
        private final String name;
        private boolean full;

        private EnumBlockMode(int meta, String name, boolean full)
        {
        	this.meta = meta;
            this.name = name;
            this.full = full;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
        
        public boolean isFull(){
        	return full;
        }
        
        public int getMeta(){
        	return meta;
        }
        
        public static EnumBlockMode getByMeta(int meta){
        	for(EnumBlockMode type : values()){
        		if(type.meta == meta){
        			return type;
        		}
        	}
        	return FULL;
        }
    }

}
