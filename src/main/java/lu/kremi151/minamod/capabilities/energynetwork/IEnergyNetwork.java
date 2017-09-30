package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyNetwork extends IEnergyStorage{

	void registerClient(BlockPos pos, EnumFacing face);
	boolean unregisterClient(BlockPos pos, EnumFacing face);
	void registerNetworkBlock(BlockPos pos);
	boolean unregisterNetworkBlock(BlockPos pos);
	IEnergyNetwork copy();
	void reset();
	
}
