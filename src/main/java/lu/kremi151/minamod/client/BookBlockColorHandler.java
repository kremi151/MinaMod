package lu.kremi151.minamod.client;

import lu.kremi151.minamod.block.tileentity.TileEntityBook;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum BookBlockColorHandler implements IBlockColor{
	INSTANCE;

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		if(tintIndex == 1) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te.getClass() == TileEntityBook.class) {
				return ((TileEntityBook)te).getBookColor();
			}
		}
		return MinaUtils.COLOR_WHITE;
	}

}
