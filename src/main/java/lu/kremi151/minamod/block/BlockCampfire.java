package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntityCampfire;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCampfire extends Block{
	
	private static final String ITEM_COOKING_TAG = "cooking";
	
	public static final PropertyBool IGNITED = PropertyBool.create("ignited");
	
	protected static final AxisAlignedBB boundings = new AxisAlignedBB(0.1875d, 0d, 0.1875d, 0.8125d, 0.500d, 0.8125d);

	public BlockCampfire() {
		super(Material.WOOD, MapColor.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(IGNITED, false));
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setTickRandomly(true);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityCampfire();
    }
	
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(IGNITED, (meta & 1) > 0);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
    	int meta = 0;
    	meta |= (state.getValue(IGNITED)?1:0);
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {IGNITED});
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boundings;
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
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
		return state.getValue(IGNITED) ? 15 : 0;
    }
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if(state.getValue(IGNITED) && canDie(worldIn, pos)){
			worldIn.setBlockState(pos, state.withProperty(IGNITED, false));
		}
    }
	
	@Override
	public int tickRate(World worldIn)
    {
        return 30;
    }
	
    private boolean canDie(World worldIn, BlockPos pos)
    {
        return worldIn.isRaining() && (worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south()));
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity e){
		if (!world.isRemote && state.getValue(IGNITED))
        {
			if(e instanceof EntityItem){
				EntityItem ei = (EntityItem)e;
				
				if(!ei.getEntityItem().isEmpty()){
					((TileEntityCampfire)world.getTileEntity(pos)).trackItem(ei);
				}else{
					e.setFire(5);
				}
				
			}else if(e.posY >= (double)pos.getY() + boundings.maxY){
				e.setFire(5);
			}
        }
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		ItemStack heldMain = player.getHeldItemMainhand();
		ItemStack heldOff = player.getHeldItemOffhand();
		if((!heldMain.isEmpty() && heldMain.getItem() == Items.FLINT_AND_STEEL) || (!heldOff.isEmpty() && heldOff.getItem() == Items.FLINT_AND_STEEL)){
			world.setBlockState(pos, state.withProperty(IGNITED, true));
			return true;
		}else{
			return false;
		}
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
    	if (rand.nextInt(24) == 0)
        {
            world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
    	if(state.getValue(IGNITED)){
        	double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + 0.5D;
            double z = (double)pos.getZ() + 0.5D;
            for(int n = 0 ; n <3 ; n++){
                double ox = 0.22D * (rand.nextDouble() - 0.5D);
                double oy = 0.17D * (rand.nextDouble() - 0.5D);
                double oz = 0.22D * (rand.nextDouble() - 0.5D);

                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + ox, y + oy, z + oz, 0.0D, 0.0D, 0.0D, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, x + ox, y + oy, z + oz, 0.0D, 0.0D, 0.0D, new int[0]);
            }
    	}
	}

}
