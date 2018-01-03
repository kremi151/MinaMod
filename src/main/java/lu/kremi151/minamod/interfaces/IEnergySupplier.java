package lu.kremi151.minamod.interfaces;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IEnergySupplier {

	default boolean canCableConnect(EnumFacing face, TileEntity entity, BlockPos pos, IBlockAccess world) {
		return true;
	}
	
}
