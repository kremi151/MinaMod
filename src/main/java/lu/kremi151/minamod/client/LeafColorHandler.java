package lu.kremi151.minamod.client;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockMinaLeaf;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum LeafColorHandler implements IItemColor, IBlockColor{
	INSTANCE;

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(stack.getItem().getRegistryName().equals(MinaBlocks.MINA_LEAVES_A.getRegistryName())){
			return BlockMinaPlanks.EnumType.byMetadata(stack.getMetadata()).getLeafColor();
		}else if(stack.getItem().getRegistryName().equals(MinaBlocks.PALM_LEAVES.getRegistryName())){
			return BlockMinaPlanks.EnumType.PALM.getLeafColor();
		}
		
		return MinaUtils.COLOR_WHITE;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Block b = state.getBlock();
		if(b == MinaBlocks.MINA_LEAVES_A){
			return state.getValue(BlockMinaLeaf.A.VARIANT).getLeafColor();
		}else if(b == MinaBlocks.PALM_LEAVES){
			return BlockMinaPlanks.EnumType.PALM.getLeafColor();
		}
		return MinaUtils.COLOR_WHITE;
	}
	
}
