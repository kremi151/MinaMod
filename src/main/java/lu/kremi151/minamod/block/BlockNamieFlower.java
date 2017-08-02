package lu.kremi151.minamod.block;

import java.util.LinkedList;
import java.util.List;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockNamieFlower extends BlockCustomCrops{
	
	public BlockNamieFlower() {
		this.honeycombRegenerationChance = 0.05f;
	}

	@Override
	public ItemStack getCrop(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.NAMIE_FRUIT);
	}

	@Override
	public ItemStack getSeed(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.NAMIE_SEEDS);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new LinkedList<ItemStack>();
		if(getAge(state) >= getMaxAge()) {
			drops.add(getCrop(world, pos, state, fortune));
		}else {
			drops.add(getSeed(world, pos, state, fortune));
		}
		return drops;
	}

}
