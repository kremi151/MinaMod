package lu.kremi151.minamod.interfaces;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Interface which marks blocks not having a {@link net.minecraft.tileentity.TileEntity} providing {@link net.minecraftforge.energy.IEnergyStorage} to be connectable to cables
 * @author michm
 *
 */
public interface IEnergySupplier {

	default boolean canCableConnect(EnumFacing face, TileEntity entity, BlockPos pos, IBlockAccess world) {
		return true;
	}
	
}
