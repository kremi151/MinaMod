package lu.kremi151.minamod.block;

import java.util.Random;

import javax.annotation.Nullable;

import lu.kremi151.minamod.block.BlockMinaPlanks.EnumType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPalmLeaves extends BlockMinaLeafBase{
	
    public static final PropertyEnum<BlockCombined.EnumBlockMode> TYPE = PropertyEnum.<BlockCombined.EnumBlockMode>create("type", BlockCombined.EnumBlockMode.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    @Deprecated
    private static final Block slave = new Block(Material.AIR);
    
    public BlockPalmLeaves(){
    	super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
    	this.registerLeafBlock(EnumType.PALM, this.getDefaultState());
    }

	@Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE) == BlockCombined.EnumBlockMode.FULL ? 0 : 1;
    }
	
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return Items.AIR;
	}
	
	@Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return quantityDropped(state);
    }
	
	private int quantityDropped(IBlockState state){
		return state.getValue(TYPE) == BlockCombined.EnumBlockMode.DOUBLE ? 2 : 1;
	}

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), quantityDropped(state), damageDropped(state));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TYPE, CHECK_DECAY, DECAYABLE});
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, BlockCombined.EnumBlockMode.getByMeta(meta & 3)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(TYPE).getMeta();

        if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
        {
            i |= 4;
        }

        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    @Override
    public BlockMinaPlanks.EnumType getMinaWoodType(int meta){
    	return BlockMinaPlanks.EnumType.PALM;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), quantityDropped(state), damageDropped(state)));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
    	IBlockState state = world.getBlockState(pos);
        return NonNullList.withSize(1, new ItemStack(this, quantityDropped(state), damageDropped(state)));
    }

    //Slab specific part
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return this.isFullBlock(state) ? FULL_BLOCK_AABB : (state.getValue(TYPE) == BlockCombined.EnumBlockMode.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF);
    }
    
    @Override
    public boolean isFullyOpaque(IBlockState state)
    {
    	BlockCombined.EnumBlockMode type = state.getValue(TYPE);
        return type.isFull() || type == BlockCombined.EnumBlockMode.TOP;
    }
    
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
            return slave.doesSideBlockRendering(state, world, pos, face);

        BlockCombined. EnumBlockMode type = state.getValue(TYPE);
        return (type == BlockCombined.EnumBlockMode.TOP && face == EnumFacing.UP) || (type == BlockCombined.EnumBlockMode.BOTTOM && face != EnumFacing.DOWN);
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	if(meta == 0){
    		return this.getDefaultState().withProperty(TYPE, BlockCombined.EnumBlockMode.FULL);
    	}else{
    		IBlockState iblockstate = this.getDefaultState().withProperty(TYPE, BlockCombined.EnumBlockMode.BOTTOM);
            return this.isFullBlock(iblockstate) ? iblockstate : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(TYPE, BlockCombined.EnumBlockMode.TOP));
    	}
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
        /*if (this.isFullBlock(blockState))
        {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side))
        {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);*/
    	//return slave.shouldSideBeRendered(blockState, blockAccess, pos, side);
    	return !net.minecraft.client.Minecraft.getMinecraft().gameSettings.fancyGraphics && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : shouldSlabSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    private boolean shouldSlabSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
    	if(!net.minecraft.client.Minecraft.getMinecraft().gameSettings.fancyGraphics){
    		if (this.isFullBlock(blockState))
            {
                return slave.shouldSideBeRendered(blockState, blockAccess, pos, side);
            }
            else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !slave.shouldSideBeRendered(blockState, blockAccess, pos, side))
            {
                return false;
            }
            return slave.shouldSideBeRendered(blockState, blockAccess, pos, side);
    	}else{
    		return true;
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	list.add(new ItemStack(itemIn, 1, 0));//Full
    	list.add(new ItemStack(itemIn, 1, 1));//Slab
    }
    //
}
