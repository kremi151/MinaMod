package lu.kremi151.minamod.client;

import lu.kremi151.minamod.MinaBlocks;
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
public class LeafColorHandler implements IItemColor, IBlockColor{
	
	private static LeafColorHandler instance = null;
	
	private LeafColorHandler(){}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if(stack.getItem().getRegistryName().equals(MinaBlocks.LEAVES_PEPPEL.getRegistryName())){
			return BlockMinaPlanks.EnumType.PEPPEL.getLeafColor();
		}else if(stack.getItem().getRegistryName().equals(MinaBlocks.LEAVES_CHERRY.getRegistryName())){
			return BlockMinaPlanks.EnumType.CHERRY.getLeafColor();
		}else if(stack.getItem().getRegistryName().equals(MinaBlocks.LEAVES_CHESTNUT.getRegistryName())){
			return BlockMinaPlanks.EnumType.CHESTNUT.getLeafColor();
		}else if(stack.getItem().getRegistryName().equals(MinaBlocks.LEAVES_COTTON.getRegistryName())){
			return BlockMinaPlanks.EnumType.COTTON.getLeafColor();
		}
		return MinaUtils.COLOR_WHITE;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex) {
		Block b = state.getBlock();
		if(b == MinaBlocks.LEAVES_PEPPEL){
			return BlockMinaPlanks.EnumType.PEPPEL.getLeafColor();
		}else if(b == MinaBlocks.LEAVES_CHERRY){
			return BlockMinaPlanks.EnumType.CHERRY.getLeafColor();
		}else if(b == MinaBlocks.LEAVES_CHESTNUT){
			return BlockMinaPlanks.EnumType.CHESTNUT.getLeafColor();
		}else if(b == MinaBlocks.LEAVES_COTTON){
			return BlockMinaPlanks.EnumType.COTTON.getLeafColor();
		}
		return MinaUtils.COLOR_WHITE;
	}
	
	public static LeafColorHandler get(){
		if(instance == null){
			instance = new LeafColorHandler();
		}
		return instance;
	}

}
