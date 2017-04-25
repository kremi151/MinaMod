package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBambusCrop extends BlockCustomBush{
	
	protected static final AxisAlignedBB blockBounds = new AxisAlignedBB(3d / 16d, 0d, 3d / 16d, 13d / 16d, 1d, 13d / 16d);

	public BlockBambusCrop() {
		super(Material.PLANTS);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(null);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds;
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        Block u = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock();
        return (u == this) || (u == Blocks.GRASS)|| (u == Blocks.DIRT);
    }
	
	@Override
	public Item getItemDropped(IBlockState bs, Random a2, int fortune){
		return MinaItems.BAMBUS;
	}
	
	@Override
	public int quantityDropped(Random r){
		return 1;
	}
	
//	@Override
//	public void onNeighborBlockChange(World world, int x, int y, int z, Block nblock) {
////		if(!canBlockStay(world,x,y,z)){
////			destroyMe(world,x,y,z);super.checkAndDropBlock(p_149855_1_, p_149855_2_, p_149855_3_, p_149855_4_);
////		}
//		super.checkAndDropBlock(world, x, y, z);
//	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos origin){
		if(world.getBlockState(pos).getBlock() == this &! canBlockStay(world, pos, state)){
			this.checkAndDropBlock(world, pos, state);
		}
	}

	@Override
	public int getLightOpacity(IBlockState ibs){
		return 0;
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random r)
    {
    	super.updateTick(world, pos, state, r);
    	
        if (world.getLightFromNeighbors(pos.up()) >= 9)
        {
            int l = getBambusGrowth(world, pos);
            
            if (l < 3)
            {

                if (r.nextInt(4) == 0){
                    if(world.isAirBlock(pos.up())){
                    	world.setBlockState(pos.up(), this.getDefaultState());
                    }
                }
                if(r.nextInt(8) == 0){
                	BlockPos rpos = new BlockPos(pos.offset(EnumFacing.getHorizontal(r.nextInt(4))));
                	if(world.isAirBlock(rpos) && canBlockStay(world, rpos, state)){
                		world.setBlockState(rpos, this.getDefaultState());
                	}
                }
            }
        }
    }
    
    public int getBambusGrowth(World world, BlockPos pos){
    	Block bambus = world.getBlockState(pos).getBlock();
    	Block under = world.getBlockState(pos.down()).getBlock();
    	if(bambus == this){
    		if(under != this){
    			return 0;
    		}else{
    			return getBambusGrowth(world, pos.down()) + 1;
    		}
    	}
    	return -1;
    }
	
}
