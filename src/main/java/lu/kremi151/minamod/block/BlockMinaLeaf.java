package lu.kremi151.minamod.block;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
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

public class BlockMinaLeaf extends BlockLeaves{

    public final PropertyEnum<BlockMinaPlanks.EnumType> TYPE;
    private final BlockMinaPlanks.EnumType supportedTypes[];
    private final HashMap<Integer, Integer> supportedTypesMetaLookup = new HashMap<>();
    private final String variantNames[];
	
	public BlockMinaLeaf(Predicate<BlockMinaPlanks.EnumType> predicate){
		Set<BlockMinaPlanks.EnumType> values = new TreeSet<>();
		for(BlockMinaPlanks.EnumType type : BlockMinaPlanks.EnumType.values()){
			if(predicate.test(type)){
				values.add(type);
				supportedTypesMetaLookup.put(type.ordinal(), values.size() - 1);
			}
		}
		this.supportedTypes = values.toArray(new BlockMinaPlanks.EnumType[values.size()]);
		this.variantNames = new String[supportedTypes.length];
		for(int i = 0 ; i < supportedTypes.length ; i++)this.variantNames[i] = supportedTypes[i].getName();
		this.TYPE = PropertyEnum.create("type", BlockMinaPlanks.EnumType.class, supportedTypes);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
	    for(int i = 0 ; i < supportedTypes.length ; i++)list.add(new ItemStack(itemIn, 1, i));
	}
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.isRemote)
        {
        	if(state.getValue(TYPE) == BlockMinaPlanks.EnumType.COTTON && worldIn.isAirBlock(pos.down()) && rand.nextInt(60) == 0){
        		ItemStack is = new ItemStack(MinaItems.COTTON, 1);
        		MinaUtils.dropItem(is, worldIn, pos.getX(), pos.getY() - 1.0d, pos.getZ());
        	}
        }
    }

    @Override
    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
    {
    	BlockMinaPlanks.EnumType type = state.getValue(TYPE);
        if (type == BlockMinaPlanks.EnumType.CHESTNUT && worldIn.rand.nextInt(chance) == 0)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(MinaItems.CHESTNUT));
        }else if (type == BlockMinaPlanks.EnumType.CHERRY && worldIn.rand.nextInt(chance) == 0)
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
	public int getRenderColor(IBlockState state) {
		if (state.getBlock() != this) {
			return MinaUtils.COLOR_WHITE;
		} else {
			return state.getValue(TYPE).getLeafColor();
		}
	}

	@Override
	public EnumType getWoodType(int meta) {
		throw new RuntimeException("Unsupported operation");
	}
	
	public BlockMinaPlanks.EnumType getMinaWoodType(int meta) {
		return getStateFromMeta(meta).getValue(TYPE);
	}
	
	public String[] getVariantNames(){
		return variantNames;
	}
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState()
        		.withProperty(DECAYABLE, Boolean.valueOf((meta & 1) == 1))
        		.withProperty(CHECK_DECAY, Boolean.valueOf((meta & 2) > 0))
        		.withProperty(TYPE, supportedTypes[Math.min((meta >> 2) & 3, supportedTypes.length)]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (((Boolean)state.getValue(DECAYABLE)).booleanValue())
        {
            i |= 1;
        }

        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        {
            i |= 2;
        }
        
        int typeMeta = supportedTypesMetaLookup.get(state.getValue(TYPE));
        i |= (typeMeta & 3) << 2;

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE, TYPE});
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
        return java.util.Arrays.asList(new ItemStack(this, 1));
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
