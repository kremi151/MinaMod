package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStrawberryCrop extends BlockCustomCrops{

	@Override
	public ItemStack getCrop(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.STRAWBERRY);
	}

	@Override
	public ItemStack getSeed(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.STRAWBERRY);
	}
}
