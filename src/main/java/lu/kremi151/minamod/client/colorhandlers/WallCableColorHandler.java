package lu.kremi151.minamod.client.colorhandlers;

import lu.kremi151.minamod.block.tileentity.TileEntityWallCable;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public enum WallCableColorHandler implements IBlockColor{
	INSTANCE;

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityWallCable) {
			IBlockState model = ((TileEntityWallCable)te).getWallModel();
			if(model != null) {
				try {
					int res = Minecraft.getMinecraft().getBlockColors().colorMultiplier(model, worldIn, pos, tintIndex);
					return (res != -1) ? res : MinaUtils.COLOR_WHITE;
				}catch(Exception e) {
					System.err.println("Error while trying to resolve block color for wall cable model " + model + " at " + pos);
					e.printStackTrace();
				}
			}
		}
		return MinaUtils.COLOR_WHITE;
	}

}
