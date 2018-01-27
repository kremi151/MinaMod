package lu.kremi151.minamod.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.BlockMinaPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMinaLeaf extends BlockMinaLeafBase
{	
	private final String variantNames[], unlocalizedNames[];
	private final Map<BlockMinaPlanks.EnumType, Integer> internalMetadataMap;

    private BlockMinaLeaf(PropertyEnum<BlockMinaPlanks.EnumType> variantProperty, BlockMinaPlanks.EnumType defaultVariant)
    {
    	super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(variantProperty, defaultVariant).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
        variantNames = new String[variantProperty.getAllowedValues().size()];
        if(variantNames.length > 4)throw new IllegalStateException("Maximum 4 variants are supported");
        unlocalizedNames = new String[variantProperty.getAllowedValues().size()];
        int idx = 0;
        HashMap<BlockMinaPlanks.EnumType, Integer> imm = new HashMap<>();
        for(BlockMinaPlanks.EnumType type : variantProperty.getAllowedValues()){
        	imm.put(type, idx);
        	variantNames[idx] = "leaves_" + type.getName();
        	unlocalizedNames[idx++] = type.getUnlocalizedName();
        	
        	BlockMinaLeafBase.registerLeafBlock(type, this.getDefaultState().withProperty(variantProperty, type));
        }
        this.internalMetadataMap = Collections.unmodifiableMap(imm);
    }
    
    protected abstract PropertyEnum<BlockMinaPlanks.EnumType> getVariantProperty();
    protected abstract int getPartialMeta(BlockMinaPlanks.EnumType type);
    
    public String[] getVariantNames(){
    	return variantNames;
    }
    
    public String[] getUnlocalizedNames(){
    	return unlocalizedNames;
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        return ((BlockMinaPlanks.EnumType)state.getValue(getVariantProperty())).getMetadata();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state) & 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
    	for(int i = 0 ; i < unlocalizedNames.length; i++){
    		list.add(new ItemStack(this, 1, i));
    	}
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, internalMetadataMap.get(state.getValue(getVariantProperty())));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(getVariantProperty(), this.getMinaWoodType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = getPartialMeta((BlockMinaPlanks.EnumType)state.getValue(getVariantProperty())) & 3;

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
    protected abstract BlockStateContainer createBlockState();

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, internalMetadataMap.get(state.getValue(getVariantProperty()))));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this, 1, internalMetadataMap.get(world.getBlockState(pos).getValue(getVariantProperty()))));
    }
    
    public static final class A extends BlockMinaLeaf{
    	
		public static final PropertyEnum<BlockMinaPlanks.EnumType> VARIANT = PropertyEnum.<BlockMinaPlanks.EnumType>create("variant", BlockMinaPlanks.EnumType.class, type -> type.ordinal() < 4);
    	
    	public A(){
    		super(VARIANT, BlockMinaPlanks.EnumType.PEPPEL);
    	}

		@Override
		protected PropertyEnum<EnumType> getVariantProperty() {
			return VARIANT;
		}
		
		@Override
		public BlockMinaPlanks.EnumType getMinaWoodType(int meta)
	    {
	        return BlockMinaPlanks.EnumType.byMetadata((meta & 3));
	    }

	    @Override
	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {VARIANT, CHECK_DECAY, DECAYABLE});
	    }

		@Override
		protected int getPartialMeta(EnumType type) {
			return type.getMetadata();
		}
		
		@Override
		protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
	    {
			if (state.getValue(VARIANT) == BlockMinaPlanks.EnumType.CHERRY && worldIn.rand.nextInt(chance) == 0)
	        {
	            spawnAsEntity(worldIn, pos, new ItemStack(MinaItems.CHERRY));
	        }else if (state.getValue(VARIANT) == BlockMinaPlanks.EnumType.CHESTNUT && worldIn.rand.nextInt(chance) == 0)
	        {
	            spawnAsEntity(worldIn, pos, new ItemStack(MinaItems.CHESTNUT));
	        }
	    }
		
		@Override
		public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
			super.updateTick(worldIn, pos, state, rand);
			if(!worldIn.isRemote && state.getValue(VARIANT) == BlockMinaPlanks.EnumType.COTTON && worldIn.isAirBlock(pos.down()) && rand.nextInt(100) == 0){
				spawnAsEntity(worldIn, pos.down(), new ItemStack(MinaItems.COTTON));
			}
	    }
    	
    }
    
    @Deprecated
    /**
     * Not used at the moment
     * @author michm
     *
     */
    private static final class B extends BlockMinaLeaf{
    	
		public static final PropertyEnum<BlockMinaPlanks.EnumType> VARIANT = PropertyEnum.<BlockMinaPlanks.EnumType>create("variant", BlockMinaPlanks.EnumType.class, type -> type.ordinal() >= 5 && type.ordinal() < 9);
    	
    	public B(){
    		super(VARIANT, BlockMinaPlanks.EnumType.PALM);
    	}

		@Override
		protected PropertyEnum<EnumType> getVariantProperty() {
			return VARIANT;
		}
		
		@Override
		public BlockMinaPlanks.EnumType getMinaWoodType(int meta)
	    {
	        return BlockMinaPlanks.EnumType.byMetadata((meta & 3) + 5);
	    }

	    @Override
	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {VARIANT, CHECK_DECAY, DECAYABLE});
	    }

		@Override
		protected int getPartialMeta(EnumType type) {
			return type.getMetadata() - 5;
		}
    	
    }

}
