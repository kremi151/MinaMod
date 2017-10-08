package lu.kremi151.minamod.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class BaseTileEntity extends TileEntity{

	protected void sync() {
		final IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}
}
