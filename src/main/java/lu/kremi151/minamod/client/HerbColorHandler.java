package lu.kremi151.minamod.client;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockHerb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HerbColorHandler implements IBlockColor{

	private static HerbColorHandler i = null;
	
	private HerbColorHandler(){}
	
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		if(state.getBlock() == MinaBlocks.HERB_CROP && tintIndex == 1){
			state = MinaBlocks.HERB_CROP.getActualState(state, worldIn, pos);
			return state.getValue(BlockHerb.TYPE).getTint();
		}
		return 0;
	}
	
	public static HerbColorHandler get(){
		if(i == null){
			i = new HerbColorHandler();
		}
		return i;
	}

}
