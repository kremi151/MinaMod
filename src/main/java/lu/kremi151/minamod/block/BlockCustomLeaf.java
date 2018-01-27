package lu.kremi151.minamod.block;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomLeaf extends BlockLeaves
{
    public static final PropertyEnum<BlockMinaPlanks.EnumType> VARIANT = PropertyEnum.<BlockMinaPlanks.EnumType>create("variant", BlockMinaPlanks.EnumType.class, new Predicate<BlockMinaPlanks.EnumType>()
    {
        @Override
		public boolean apply(BlockMinaPlanks.EnumType type)
        {
            return type.getMetadata() < 4;
        }
    });

    public BlockCustomLeaf()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockMinaPlanks.EnumType.PEPPEL).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.isRemote)
        {
        	if(state.getValue(VARIANT) == BlockMinaPlanks.EnumType.COTTON && worldIn.isAirBlock(pos.down()) && rand.nextInt(40) == 0){
        		ItemStack is = new ItemStack(MinaItems.COTTON, 1);
        		MinaUtils.dropItem(is, worldIn, pos.getX(), pos.getY() - 1.0d, pos.getZ());
        	}
        }
    }

    @Override
    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
    {
        if (state.getValue(VARIANT) == BlockMinaPlanks.EnumType.CHESTNUT && worldIn.rand.nextInt(chance) == 0)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(MinaItems.CHESTNUT));
        }else if (state.getValue(VARIANT) == BlockMinaPlanks.EnumType.CHERRY && worldIn.rand.nextInt(chance) == 0)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(MinaItems.CHERRY));
        }
    }

    @Override
    protected int getSaplingDropChance(IBlockState state)
    {
        return 25;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, BlockMinaPlanks.EnumType.PEPPEL.getMetadata()));
        list.add(new ItemStack(this, 1, BlockMinaPlanks.EnumType.COTTON.getMetadata()));
        list.add(new ItemStack(this, 1, BlockMinaPlanks.EnumType.CHESTNUT.getMetadata()));
        list.add(new ItemStack(this, 1, BlockMinaPlanks.EnumType.CHERRY.getMetadata()));
    }
    
    @SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		if (state.getBlock() != this) {
			return MinaUtils.COLOR_WHITE;
		} else {
			BlockMinaPlanks.EnumType enumtype = (BlockMinaPlanks.EnumType) state.getValue(VARIANT);
			return enumtype.getLeafColor();
		}
	}

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMetadata());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, this.getMinaWoodType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMetadata();

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
    @Deprecated()
    public net.minecraft.block.BlockPlanks.EnumType getWoodType(int meta)
    {
        throw new UnsupportedOperationException("much function. very bad. wow.");
    }
    
    public BlockMinaPlanks.EnumType getMinaWoodType(int meta)
    {
        return BlockMinaPlanks.EnumType.byMetadata((meta & 3) % 4);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, CHECK_DECAY, DECAYABLE});
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return ((BlockMinaPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return java.util.Arrays.asList(new ItemStack(this, 1, world.getBlockState(pos).getValue(VARIANT).getMetadata()));
    }
    
    /*
     * Begin of hacks for fancy / fast graphics
     */
    
    @Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return Blocks.LEAVES.getBlockLayer();
	}
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.LEAVES.isOpaqueCube(null);
    }

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setGraphicsLevel(boolean fancy){}

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return Blocks.LEAVES.shouldSideBeRendered(Blocks.LEAVES.getDefaultState(), blockAccess, pos, side);
    }

}
