package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockChiliCrop extends BlockCustomCrops{
	
	public BlockChiliCrop() {
		this.honeycombRegenerationChance = 0.0f;
		this.honeycombRegenerationTries = 0;
	}

	@Override
	public ItemStack getCrop(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.CHILI);
	}

	@Override
	public ItemStack getSeed(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.CHILI);
	}

}
