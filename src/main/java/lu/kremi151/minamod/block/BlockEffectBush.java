package lu.kremi151.minamod.block;

import java.util.Optional;
import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.enums.EnumParticleEffect;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEffectBush extends BlockCustomBush{
	
	public static final PropertyEnum<BlockEffectBush.EnumType> VARIANT = PropertyEnum.<BlockEffectBush.EnumType>create("variant", BlockEffectBush.EnumType.class);
	
	public BlockEffectBush(){
		super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockEffectBush.EnumType.POISONOUS));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (BlockEffectBush.EnumType type : BlockEffectBush.EnumType.values())
        {
            list.add(new ItemStack(this, 1, type.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockEffectBush.EnumType.byMetadata(meta));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((BlockEffectBush.EnumType)state.getValue(VARIANT)).getMapColor();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockEffectBush.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return MinaUtils.fullBlockBounds;
	}
	
//	@Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
//    {
//        return blockBounds;
//    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getCollisionBoundingBox(IBlockState worldIn, IBlockAccess pos, BlockPos state)
    {
        return MinaUtils.emptyBlockBounds;
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity e){
		if (!world.isRemote)
        {
			if(e instanceof EntityLivingBase){
				EntityLivingBase l = (EntityLivingBase) e;
				Optional<Potion> effect = ((BlockEffectBush.EnumType)state.getValue(VARIANT)).getEffect();
				if(effect.isPresent()){
					l.addPotionEffect(new PotionEffect(effect.get(), 250));
				}
			}
        }
	}
	
	@Override
    public boolean isCollidable()
    {
        return true;
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

//	@Override
//    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
//    {
//        return true;
//    }
	
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
	
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random r){
    	super.updateTick(world, pos, state, r);
    	
    	EnumType variant = state.getValue(VARIANT);
    	boolean withered = variant == EnumType.WITHERED;
    	
    	int chance = withered ? 5 : (world.isRaining() ? 3 : 7);
    	
        if(world.getGameRules().getBoolean("minamod.bushSpreading") && r.nextInt(chance) == 0){
        	BlockPos npos = pos.offset(EnumFacing.getHorizontal(r.nextInt(4)));
        	if((variant.canSpread() && world.isAirBlock(npos) && canBlockStay(world, npos, state))
        			|| (withered && world.getBlockState(npos).getBlock() == MinaBlocks.EFFECT_BUSH && r.nextInt(world.getBlockState(npos).getValue(VARIANT).getWitheringFactor()) == 0)){
        		world.setBlockState(npos, state);
        	}
        }else if(!withered && r.nextInt(variant.getWitheringFactor() * 100) == 0){
        	world.setBlockState(pos, state.withProperty(VARIANT, BlockEffectBush.EnumType.WITHERED));
        }
        
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		BlockEffectBush.EnumType type = ((BlockEffectBush.EnumType)state.getValue(VARIANT));
    	if(type.hasSpores() && rand.nextInt(5) == 0){
    		MinaMod.getProxy().spawnParticleEffect(EnumParticleEffect.SPORE, world, (double)pos.getX() + rand.nextDouble(), (double)pos.getY() + 0.8, (double)pos.getZ() + rand.nextDouble(), type.getSporeColorRed(), type.getSporeColorGreen(), type.getSporeColorBlue());
    	}
	}
    
    public static enum EnumType implements IStringSerializable
    {
        POISONOUS(0, "poisonous", MapColor.GRASS, MinaUtils.convertRGBToDecimal(129, 204, 104), true),
        SPEEDY(1, "speedy", MapColor.GRASS, MinaUtils.convertRGBToDecimal(71, 222, 222), true),
        REGENERATING(2, "regenerating", MapColor.GRASS, MinaUtils.convertRGBToDecimal(255, 255, 48), true),
        WITHERED(3, "withered", "withered", MapColor.BLACK, MinaUtils.COLOR_BLACK, false);
    	
    	static{
    		POISONOUS.setPotionEffect(MobEffects.POISON);
    		
    		SPEEDY.setPotionEffect(MobEffects.SPEED);
    		SPEEDY.setWitheringFactor(6);
    		
    		REGENERATING.setPotionEffect(MobEffects.REGENERATION);
    		REGENERATING.setWitheringFactor(5);
    		
    		WITHERED.setHasSpores(false);
    	}

        private static final BlockEffectBush.EnumType[] META_LOOKUP = new BlockEffectBush.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        /** The color that represents this entry on a map. */
        private final MapColor mapColor;
        private final int sporeColor;
        private boolean hasSpores = true;
        private boolean canSpread = true;
        private final float sporeColorRF, sporeColorGF, sporeColorBF;
        private Potion do_not_use_internally_Effect = null;
        private int witheringFactor = 8;

        private EnumType(int metaIn, String nameIn, MapColor mapColorIn, int leafColor, boolean canSpread)
        {
            this(metaIn, nameIn, nameIn, mapColorIn, leafColor, canSpread);
        }

        private EnumType(int metaIn, String nameIn, String unlocalizedNameIn, MapColor mapColorIn, int sporeColor, boolean canSpread)
        {
            this.meta = metaIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.mapColor = mapColorIn;
            this.sporeColor = sporeColor;
            this.sporeColorRF = (float)MinaUtils.extractRedFromColor(sporeColor) / 255f;
            this.sporeColorGF = (float)MinaUtils.extractGreenFromColor(sporeColor) / 255f;
            this.sporeColorBF = (float)MinaUtils.extractBlueFromColor(sporeColor) / 255f;
        }
        
        private void setPotionEffect(Potion effect){
        	this.do_not_use_internally_Effect = effect;
        }
        
        private void setHasSpores(boolean v){
        	this.hasSpores = v;
        }
        
        private void setWitheringFactor(int v){
        	this.witheringFactor = v;
        }

        public int getMetadata()
        {
            return this.meta;
        }
        
        public Optional<Potion> getEffect(){
        	return Optional.ofNullable(this.do_not_use_internally_Effect);
        }

        /**
         * The color which represents this entry on a map.
         */
        public MapColor getMapColor()
        {
            return this.mapColor;
        }
        
        public int getWitheringFactor(){
        	return this.witheringFactor;
        }
        
        public int getSporeColor(){
        	return this.sporeColor;
        }
        
        public float getSporeColorRed(){
        	return sporeColorRF;
        }
        
        public float getSporeColorGreen(){
        	return sporeColorGF;
        }
        
        public float getSporeColorBlue(){
        	return sporeColorBF;
        }
        
        public boolean hasSpores(){
        	return hasSpores;
        }
        
        public boolean canSpread(){
        	return canSpread;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        public static BlockEffectBush.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (BlockEffectBush.EnumType BlockPoisonousBush$enumtype : values())
            {
                META_LOOKUP[BlockPoisonousBush$enumtype.getMetadata()] = BlockPoisonousBush$enumtype;
            }
        }
    }
}
