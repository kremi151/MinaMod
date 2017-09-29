package lu.kremi151.minamod.capabilities.energynetwork;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyNetworkProvider extends IEnergyStorage{
	
	@CapabilityInject(IEnergyNetworkProvider.class)
	public static final Capability<IEnergyNetworkProvider> CAPABILITY = null;
	
	IEnergyNetwork getNetwork();
	
	/**
	 * Checks if a network has been set for this provider. This method shall NOT create a network if it is absent!!!
	 * @return
	 */
	boolean hasNetwork();
	
	void onNeighbourNetworkChanged(BlockPos neighbor, IEnergyNetwork newNetwork);

}
