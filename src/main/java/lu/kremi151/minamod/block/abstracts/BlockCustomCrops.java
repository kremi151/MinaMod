package lu.kremi151.minamod.block.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockCustomCrops extends BlockCrops {

	@Override
	public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();

		Random rand = world instanceof World ? ((World) world).rand : RANDOM;

		int count = quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; i++) {
			ItemStack stack = getStackDropped(state, world, pos, rand, fortune);
			if (!stack.isEmpty()) {
				ret.add(stack);
			}
		}

		int age = ((Integer) state.getValue(AGE)).intValue();

		if (age >= getMaxAge()) {
			int k = 3 + fortune;

			for (int i = 0; i < 3 + fortune; ++i) {
				if (rand.nextInt(2 * getMaxAge()) <= age) {
					ret.add(new ItemStack(getCrop(state, world, pos, rand, fortune), 1, getCropMetadata(state, world, pos, rand, fortune)));
				}
			}
		}
		return ret;
	}

	@Deprecated
	protected Item getSeed(IBlockState state) {
		return getSeed();
	}

	@Deprecated
	protected Item getCrop(IBlockState state) {
		return getCrop();
	}

	@Deprecated
	protected int getCropMetadata(IBlockState state) {
		return 0;
	}

	@Deprecated
	protected int getSeedMetadata(IBlockState state) {
		return 0;
	}
	
	protected Item getSeed(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return getSeed(state);
	}

	protected Item getCrop(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return getCrop(state);
	}

	protected int getCropMetadata(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return getCropMetadata(state);
	}

	protected int getSeedMetadata(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return getSeedMetadata(state);
	}

	@Override
	@Deprecated
	public final Item getItemDropped(IBlockState state, Random rand, int fortune) {
		throw new UnsupportedOperationException("This method is deprecated. Use the other one.");
	}
	
	public Item getItemDropped(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCrop(state, world, pos, rand, fortune) : this.getSeed(state, world, pos, rand, fortune);
	}
	
	protected ItemStack getStackDropped(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune){
		return new ItemStack(getItemDropped(state, world, pos, rand, fortune), 1, damageDropped(state, world, pos, rand, fortune));
	}

	@Override
	@Deprecated
	public final int damageDropped(IBlockState state) {
		throw new UnsupportedOperationException("This method is deprecated. Use the other one.");
	}
	
	public int damageDropped(IBlockState state, IBlockAccess world, BlockPos pos, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCropMetadata(state, world, pos, rand, fortune) : this.getSeedMetadata(state, world, pos, rand, fortune);
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == Blocks.FARMLAND || state.getBlock() == Blocks.GRASS;
	}
	
	protected void onGrown(World worldIn, BlockPos pos, IBlockState state, Random rand){}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		try{
			int age = getAge(state);
			
			super.updateTick(worldIn, pos, state, rand);
			
			state = worldIn.getBlockState(pos);
			
			if(age < getAge(state)){
				onGrown(worldIn, pos, state, rand);
			}
		}catch(IllegalArgumentException e){
			MinaMod.getLogger().error("Wrong ticked crop block", e);
		}
    }
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
		int age = getAge(state);
		
		super.grow(worldIn, rand, pos, state);
		
		state = worldIn.getBlockState(pos);
		if(age < getAge(state)){
			onGrown(worldIn, pos, state, rand);
		}
    }
}
