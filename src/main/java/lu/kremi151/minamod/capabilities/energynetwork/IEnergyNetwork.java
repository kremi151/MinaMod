package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyNetwork extends IEnergyStorage{

	void registerClient(BlockPos pos, EnumFacing face);
	
}
