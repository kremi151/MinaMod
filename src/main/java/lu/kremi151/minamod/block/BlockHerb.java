package lu.kremi151.minamod.block;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import lu.kremi151.minamod.block.tileentity.TileEntityHerbCrop;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemHerbGuide;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHerb extends BlockCustomCrops{
	
	public static final PropertyEnum<EnumHerb> TYPE = PropertyEnum.<EnumHerb>create("type", EnumHerb.class, new Predicate<EnumHerb>(){

		@Override
		public boolean apply(EnumHerb input) {
			return input.isPlantable();
		}
		
	});

	public BlockHerb(){
		this.setTickRandomly(true);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(TYPE, tryGetType(world, pos));
	}
	
	private EnumHerb tryGetType(IBlockAccess world, BlockPos pos){
		TileEntityHerbCrop te = (TileEntityHerbCrop) world.getTileEntity(pos);
		if(te != null){
			EnumHerb type = te.getType();
			return type.isPlantable()?type:EnumHerb.GRAY;
		}else{
			return EnumHerb.GRAY;
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityHerbCrop();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, TYPE});
    }
	
	@Override
	protected Item getSeed(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return MinaItems.HERB;
	}

	@Override
	protected Item getCrop(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return MinaItems.HERB;
	}

	@Override
	protected int getCropMetadata(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return ((TileEntityHerbCrop)world.getTileEntity(pos)).getType().getHerbId();
	}

	@Override
	protected int getSeedMetadata(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return ((TileEntityHerbCrop)world.getTileEntity(pos)).getType().getHerbId();
	}
	
	@Override
	public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		EnumHerb mutation = ((TileEntityHerbCrop)world.getTileEntity(pos)).getMutation();
		if(mutation != null){
			ret.add(new ItemStack(MinaItems.HERB, 1, mutation.getHerbId()));
		}
		return ret;
	}
	
	@Override
    public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
		return net.minecraftforge.common.EnumPlantType.Crop;
    }
	
	@Override
	protected void onGrown(World worldIn, BlockPos pos, IBlockState state, Random rand){
		TileEntityHerbCrop hc = (TileEntityHerbCrop) worldIn.getTileEntity(pos);
		if(!isMaxAge(state) && hc.getMutation() == null && rand.nextFloat() <= hc.getMutability()){
			LinkedList<EnumFacing> faces = new LinkedList<EnumFacing>(Arrays.asList(EnumFacing.HORIZONTALS));
			while(faces.size() > 0){
				int idx = rand.nextInt(faces.size());
				EnumFacing f = faces.get(idx);
				faces.remove(idx);
				BlockPos rpos = pos.offset(f);
				IBlockState rstate = worldIn.getBlockState(rpos);
				if(rstate.getBlock() == this){
					rstate = getActualState(rstate, worldIn, rpos);
					hc.setMutation(EnumHerb.mutate(hc.getType(), rstate.getValue(TYPE), rand));
					break;
				}
			}
		}
	}

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }
    
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        ItemStack guide = player.getHeldItem(EnumHand.MAIN_HAND);
        if(guide.isEmpty() || guide.getItem() != MinaItems.HERB_GUIDE){
        	guide = player.getHeldItem(EnumHand.OFF_HAND);
        }
        if(!guide.isEmpty() && guide.getItem() == MinaItems.HERB_GUIDE && ItemHerbGuide.percentageCompleted(guide) == 1.0f && this.RANDOM.nextInt(1000) == 0){
        	spawnAsEntity(world, pos, new ItemStack(MinaItems.HERB, 1, EnumHerb.GOLD.getHerbId()));
        }
        world.setBlockToAir(pos);
    }
}
