package lu.kremi151.minamod.block;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaLootTableList;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntitySieve;
import lu.kremi151.minamod.packet.message.MessageUpdateTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSieve extends Block{
	
	public static final PropertyEnum<MaterialType> MATERIAL = PropertyEnum.<MaterialType>create("material", MaterialType.class);
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 5);
	
	protected static final AxisAlignedBB SIEVE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.6875D, 0.875D);
	
	public BlockSieve() {
		super(Material.IRON);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(1.0f);
		setResistance(5.0f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0).withProperty(MATERIAL, MaterialType.AIR));
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return SIEVE_AABB;
	}
	
	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        //if(!world.isRemote){
        	if (player.isSneaking()) {
            	return false;
            }
    		TileEntitySieve te = (TileEntitySieve) world.getTileEntity(pos);
    		ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
    		boolean creative = player.capabilities.isCreativeMode;
    		
    		if(!held.isEmpty() && (held.getItem() instanceof ItemBlock) && te.isSieveEmpty()){
    			ItemBlock ib = (ItemBlock) held.getItem();
    			BlockSieve.MaterialType newmat = null;
    			if(ib.getBlock() == Blocks.SAND){
    				if(held.getMetadata() == 0){
    					newmat = BlockSieve.MaterialType.SAND;
    				}else if(held.getMetadata() == 1){
    					newmat = BlockSieve.MaterialType.RED_SAND;
    				}
    			}else if(ib.getBlock() == Blocks.GRAVEL){
    				newmat = BlockSieve.MaterialType.GRAVEL;
    			}else if(ib.getBlock() == Blocks.SOUL_SAND){
    				newmat = BlockSieve.MaterialType.SOUL_SAND;
    			}
    			if(newmat != null){
    				te.setNewMaterial(newmat);
    				//world.setBlockState(pos, state.getActualState(world, pos));//TODO: optimize & fix
    				world.notifyBlockUpdate(pos, state, state.getActualState(world, pos), 3);
    				if(!creative)held.shrink(1);
    				//MinaMod.getMinaMod().getPacketDispatcher().sendToAllAround(new MessageUpdateTileEntity(world, pos, true), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 80.0));
    			}
    		}
        //}
        //world.notifyBlockUpdate(pos, state, state.getActualState(world, pos), 3);
        return true;
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntitySieve te = (TileEntitySieve)world.getTileEntity(pos);
		int level = 0;
		float levelF = te.getLevel();
		BlockSieve.MaterialType material = BlockSieve.MaterialType.AIR;
		if(levelF > 0.0f){
			level = MathHelper.clamp(MathHelper.floor(levelF / 0.25f) + 1, 1, 5);
			material = te.getActualMaterial();
		}
		return state.withProperty(MATERIAL, material).withProperty(LEVEL, level);
	}
	
    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, new IProperty[] {LEVEL, MATERIAL});
    }
    
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

	@Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntitySieve();
    }
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity)
    {
        return hasContent(world, state, pos) ? SoundType.SAND : SoundType.METAL;
    }
	
	/*@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos)
    {
        return hasContent(world, state, pos) ? -1.0f : 1.5f;
    }*/
	
	private boolean hasContent(IBlockAccess world, IBlockState state, BlockPos pos){
		return state.getActualState(world, pos).getValue(LEVEL) > 0;
	}
	
	private boolean hasContent(IBlockAccess world, BlockPos pos){
		return world.getBlockState(pos).getActualState(world, pos).getValue(LEVEL) > 0;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager)
    {
        return hasContent(worldObj, state, target.getBlockPos());
    }

    @SideOnly(Side.CLIENT)
	@Override
    public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager)
    {
        return hasContent(world, pos);
    }

	public static enum MaterialType implements IStringSerializable{
		AIR("air", null),
		SAND("sand", MinaLootTableList.SIEVE_SAND),
		RED_SAND("red_sand", MinaLootTableList.SIEVE_SAND),
		GRAVEL("gravel", MinaLootTableList.SIEVE_GRAVEL),
		SOUL_SAND("soul_sand", MinaLootTableList.SIEVE_SOUL_SAND);
		
		private final String name;
		private final ResourceLocation lootTable;
		
		private MaterialType(String name, @Nullable ResourceLocation lootTable){
			this.name = name;
			this.lootTable = lootTable;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Nullable
		public ResourceLocation getLootTable(){
			return lootTable;
		}
		
		public static MaterialType parse(String name){
			for(MaterialType type : values()){
				if(type.name.equalsIgnoreCase(name)){
					return type;
				}
			}
			return AIR;
		}
		
	}
}
