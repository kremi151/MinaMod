package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityGenerator;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenerator extends BlockCustomHorizontal{
	
	public final static PropertyBool HEATING = PropertyBool.create("heating");

	public BlockGenerator() {
		super(Material.IRON, MapColor.BLACK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(HEATING, false));
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityGenerator();
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

            if (tileentity instanceof TileEntityGenerator)
            {
            	playerIn.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdGenerator, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getActualState(state, world, pos).getValue(HEATING) ? 10 : 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
    	TileEntity tes = worldIn.getTileEntity(pos);
    	if(tes instanceof TileEntityGenerator) {
    		state = state.withProperty(HEATING, ((TileEntityGenerator)tes).getHeating() > 0.0f);
    	}
        return state;
    }
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, HEATING});
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
    	if (rand.nextInt(24) == 0)
        {
            world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
    	TileEntity te = world.getTileEntity(pos);
    	if(te instanceof TileEntityGenerator && ((TileEntityGenerator)te).getHeating() > 0.0f){
        	double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + 0.5D;
            double z = (double)pos.getZ() + 0.5D;
            for(int n = 0 ; n <3 ; n++){
                double ox = (1.0D + (0.4D * rand.nextDouble())) * (rand.nextDouble() - 0.5D);
                double oy = 0.25D * (rand.nextDouble() - 0.5D);
                double oz = (1.0D + (0.4D * rand.nextDouble())) * (rand.nextDouble() - 0.5D);

                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + ox, y + oy, z + oz, 0.0D, 0.0D, 0.0D, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, x + ox, y + oy, z + oz, 0.0D, 0.0D, 0.0D, new int[0]);
            }
    	}
	}

}
